public class driverr {
    public static void main(String[] args) {

        card[] cards = new card[5];

        int count = 0;
        boolean duplicateFound = false;

        card card1 = new card("Ace", "Spades");

        // Check card1 against earlier cards
        for (int i = 0; i < count; i++) {
            if (card1.equals(cards[i])) {
                System.out.println("Duplicate found: " + card1);
                duplicateFound = true;
                break;
            }
        }

        if (!duplicateFound) {
            cards[count] = card1;
            count++;
        }

        card card2 = new card("King", "Hearts");

        for (int i = 0; i < count; i++) {
            if (card2.equals(cards[i])) {
                System.out.println("Duplicate found: " + card2);
                duplicateFound = true;
                break;
            }
        }

        if (!duplicateFound) {
            cards[count] = card2;
            count++;
        }

        card card3 = new card("Queen", "Diamonds");

        for (int i = 0; i < count; i++) {
            if (card3.equals(cards[i])) {
                System.out.println("Duplicate found: " + card3);
                duplicateFound = true;
                break;
            }
        }

        if (!duplicateFound) {
            cards[count] = card3;
            count++;
        }
        card card4 = new card("Ace", "Spades");

        duplicateFound = false;

        for (int i = 0; i < count; i++) {
            if (card4.equals(cards[i])) {
                System.out.println("Duplicate found: " + card4);
                duplicateFound = true;
                break;
            }
        }
        if (!duplicateFound) {
            cards[count] = card4;
            count++;
        }
    }
}
