import java.util.Objects;

public class card {
    private String rank;
    private String suit;
    // Constructor
    public card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }
    // toString()
    @Override
    public String toString() {
        return rank + " of " + suit;
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
        card other = (card) obj;
        return rank.equals(other.rank) && suit.equals(other.suit);
    }
    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
