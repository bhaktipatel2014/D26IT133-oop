package bank;

import java.util.Scanner;

public class MiniBank {

    record BankInfo(String name, String branch) {
    }

    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT");

        System.out.println("----- " + bank.name() + " -----");
        System.out.println("Branch: " + bank.branch());

        Account[] accounts = new Account[3];

        accounts[0] = new Account("Dhruva", 5000);
        accounts[1] = new Account("Bhakti");
        accounts[2] = new Account("John", 3000);

        accounts[0].deposit(1000);
        accounts[1].deposit(2000);

        accounts[0].withdraw(2000);
        accounts[1].withdraw(500);
        accounts[2].withdraw(4000);

        System.out.println("\nAccount Details:");

        for (Account account : accounts) {
            System.out.println("Account Number: " + account.getAccountNumber());
            System.out.println("Owner: " + account.getOwnerName());
            System.out.println("Balance: " + account.getBalance());
            System.out.println();
        }

        int choice;

        do {
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            String result = switch (choice) {
                case 1 -> "Open Account - to be implemented in a later lab";
                case 2 -> "Deposit - to be implemented in a later lab";
                case 3 -> "Withdraw - to be implemented in a later lab";
                case 4 -> "Transfer - to be implemented in a later lab";
                case 5 -> "Exiting MiniBank...";
                default -> "Invalid choice";
            };

            System.out.println(result);

        } while (choice != 5);

        System.out.println("Thank you for using MiniBank.");

        sc.close();
    }
}