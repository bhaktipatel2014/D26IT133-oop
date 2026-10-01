public class driver_4 {
    public static void main(String[] args) {

        String[] passwords = {
            "bhakti",
            "bhakti24",
            "Bhakti@3124"
        };

        for (String pw : passwords) {
            System.out.println(pw + " -> " + PasswordChecker.strength(pw));
        }
    }
}
