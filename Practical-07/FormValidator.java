import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// Custom annotation for checking blank fields
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

// Custom annotation for maximum length
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

// Signup form class
class SignupForm {
    @NotBlank
    @MaxLength(20)
    String name;

    @NotBlank
    @MaxLength(30)
    String email;

    @NotBlank
    @MaxLength(10)
    String password;

    SignupForm(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}

// Validator class
class FormValidator {

    public static List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                // Check NotBlank annotation
                if (field.isAnnotationPresent(NotBlank.class)) {
                    if (value == null || value.toString().trim().isEmpty()) {
                        errors.add(field.getName() + " must not be blank");
                    }
                }

                // Check MaxLength annotation
                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength maxLength = field.getAnnotation(MaxLength.class);

                    if (value != null && value.toString().length() > maxLength.value()) {
                        errors.add(field.getName() + " must not exceed "
                                + maxLength.value() + " characters");
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Unable to validate field: " + field.getName());
            }
        }

        return errors;
    }

    public static void main(String[] args) {
        SignupForm form = new SignupForm("", "bhakti@example.com",
                "verylongpassword123");

        List<String> errors = validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid");
        } else {
            System.out.println("Validation Errors:");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}