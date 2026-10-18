package bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MiniBank {

    private record BankInfo(String name, String branch) {
        @Override
        public String toString() {
            return name + " - " + branch;
        }
    }

    private enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, LIST_ACCOUNTS, EXIT
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<Account> accounts = new ArrayList<>(List.of(
                new SavingsAccount("Dhruva", 5000, 1000),
                new CurrentAccount("Bhakti", 2000, 5000),
                new FixedDepositAccount("Aarya", 10000)));
        BankInfo bank = new BankInfo("MiniBank", "Charusat Branch");

        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt(input, "Choose an option: ");
            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.LIST_ACCOUNTS;
                case 6 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice. Enter a number from 1 to 6.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT -> openAccount(input, accounts);
                case DEPOSIT -> deposit(input, accounts);
                case WITHDRAW -> withdraw(input, accounts);
                case TRANSFER -> transfer(input, accounts);
                case LIST_ACCOUNTS -> listAccounts(accounts);
                case EXIT -> {
                    running = false;
                    System.out.println("Thank you for banking with MiniBank. Goodbye!");
                }
            }
        }
        input.close();
    }

    private static void printMenu() {
        System.out.println("\n1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. List accounts");
        System.out.println("6. Exit");
    }

    private static void openAccount(Scanner input, List<Account> accounts) {
        System.out.println("Account type: 1. Savings  2. Current  3. Fixed Deposit");
        int type = readInt(input, "Choose account type: ");
        if (type < 1 || type > 3) {
            System.out.println("Invalid account type.");
            return;
        }

        System.out.print("Owner name: ");
        String owner = input.nextLine().trim();
        if (owner.isEmpty()) {
            System.out.println("Owner name cannot be empty.");
            return;
        }
        long openingBalance = readLong(input, "Opening balance (rupees): ");
        if (openingBalance < 0) {
            System.out.println("Opening balance cannot be negative.");
            return;
        }

        Account account = switch (type) {
            case 1 -> new SavingsAccount(owner, openingBalance, 1000);
            case 2 -> new CurrentAccount(owner, openingBalance, 5000);
            default -> new FixedDepositAccount(owner, openingBalance);
        };
        accounts.add(account);
        System.out.println("Account opened: " + account);
        System.out.printf("Interest rate: %.1f%% per year%n", account.interestRate());
    }

    private static void deposit(Scanner input, List<Account> accounts) {
        Account account = findAccount(input, accounts, "Account number: ");
        if (account == null) return;
        long amount = readLong(input, "Deposit amount: ");
        if (account.deposit(amount)) {
            System.out.println("Deposit successful. New balance: " + account.getBalance());
        } else {
            System.out.println("Deposit amount must be greater than zero.");
        }
    }

    private static void withdraw(Scanner input, List<Account> accounts) {
        Account account = findAccount(input, accounts, "Account number: ");
        if (account == null) return;
        long amount = readLong(input, "Withdrawal amount: ");
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } else if (account instanceof FixedDepositAccount) {
            System.out.println("Withdrawal denied: fixed deposit is locked.");
        } else {
            System.out.println("Withdrawal denied: amount is invalid or account limit would be exceeded.");
        }
    }

    private static void transfer(Scanner input, List<Account> accounts) {
        Account from = findAccount(input, accounts, "From account number: ");
        if (from == null) return;
        Account to = findAccount(input, accounts, "To account number: ");
        if (to == null) return;
        if (from.equals(to)) {
            System.out.println("Source and destination must be different accounts.");
            return;
        }
        long amount = readLong(input, "Transfer amount: ");
        if (from.withdraw(amount)) {
            to.deposit(amount);
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Transfer denied: source account withdrawal limit exceeded.");
        }
    }

    private static void listAccounts(List<Account> accounts) {
        System.out.println("\nAccount list:");
        for (Account account : accounts.toArray(Account[]::new)) {
            System.out.printf("%s | interest %.1f%% per year%n",
                    account, account.interestRate());
            if (account instanceof FixedDepositAccount fixedDeposit) {
                System.out.println("  Fixed deposit is locked; withdrawals are not allowed.");
            }
        }
    }

    private static Account findAccount(Scanner input, List<Account> accounts, String prompt) {
        System.out.print(prompt);
        String accountNumber = input.nextLine().trim();
        for (Account account : accounts) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return account;
            }
        }
        System.out.println("Account not found: " + accountNumber);
        return null;
    }

    private static int readInt(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static long readLong(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
