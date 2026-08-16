import java.util.Objects;

public class point {
    private int x;
    private int y;
    public point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    // toString()
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    // equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        point other = (point) obj;

        return x == other.x && y == other.y;
    }
    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
