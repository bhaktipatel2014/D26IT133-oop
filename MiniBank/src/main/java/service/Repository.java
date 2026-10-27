package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.annotation.Id;

/** Generic in-memory repository whose entity key is declared with @Id. */
public final class Repository<T> {
    private final Map<String, T> items = new HashMap<>();

    public T save(T entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null.");
        items.put(readId(entity), entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) save(entity);
    }

    public T findById(String id) { return items.get(id); }

    public ArrayList<T> findAll() { return new ArrayList<>(items.values()); }

    public boolean delete(String id) { return items.remove(id) != null; }

    public int size() { return items.size(); }

    private String readId(T entity) {
        for (Class<?> type = entity.getClass(); type != null && type != Object.class;
             type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (!field.isAnnotationPresent(Id.class)) continue;
                try {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value == null || value.toString().isBlank())
                        throw new IllegalArgumentException("@Id field cannot be blank.");
                    return value.toString();
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot read @Id field " + field.getName(), e);
                }
            }
        }
        throw new IllegalArgumentException("Entity type " + entity.getClass().getName()
                + " must declare an @Id field.");
    }

    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) throw new IllegalArgumentException("Values cannot be empty.");
        double total = 0;
        for (N value : values) total += value.doubleValue();
        return total / values.length;
    }
}
