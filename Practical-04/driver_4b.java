import java.util.Scanner;
public class driver_4b {
    public static void main(String[] args) {
        String[] logs = {
            "10:05 bhakti Hello there",
            "10:06 dhruva How are you",
            "10:07"
        };
        Scanner input = new Scanner(System.in);
        System.out.print("Enter keyword: ");
        String keyword = input.nextLine();
        System.out.println(ChatFilter.filter(logs, keyword));
        input.close();
    }
}
