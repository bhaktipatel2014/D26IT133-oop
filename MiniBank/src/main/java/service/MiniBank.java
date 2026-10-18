package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.Premium;
import model.SavingsAccount;
import model.UnsafeAccountWorker;
import util.AnnotationValidator;
import util.StatementFormatter;
import static util.Validator.isValidEmail;
import static util.Validator.isValidMobile;

public class MiniBank {

    private record BankInfo(String name, String branch) {
        @Override
        public String toString() {
            return name + " - " + branch;
        }
    }

    private enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, LIST_ACCOUNTS, STATEMENT, CONCURRENCY_DEMO, TRANSACTION_PROCESSING_DEMO, EXIT
    }

    public static void main(String[] args) {
        List<Account> accounts = new ArrayList<>(List.of(
                new SavingsAccount("Dhruva", 5000, 1000),
                new CurrentAccount("Bhakti", 2000, 5000),
                new FixedDepositAccount("Aarya", 10000)));
        List<Customer> customers = new ArrayList<>();
        BankInfo bank = new BankInfo("MiniBank", "Charusat Branch");

        Account invalidAccount = new SavingsAccount("Validation Demo", -100, 0);
        String[] invalidErrors = AnnotationValidator.validate(invalidAccount);
        String[] validErrors = AnnotationValidator.validate(accounts.get(0));

        System.out.println("========================================");
        System.out.println("Account Validation");
        System.out.println("========================================");
        System.out.println("Invalid account: " + invalidAccount);
        System.out.println("Validation errors:");
        for (String error : invalidErrors) {
            System.out.println("  - " + error);
        }
        System.out.println("Valid account: " + accounts.get(0));
        System.out.println("Validation errors: "
                + (validErrors.length == 0 ? "none" : String.join(", ", validErrors)));

        System.out.println("\n================================");
        System.out.println(bank);
        System.out.println("================================\n");

        try (BankSession session = new BankSession()) {
            runMenu(session.scanner(), accounts, customers);
        }
    }

    private static void runMenu(Scanner input, List<Account> accounts,
                                List<Customer> customers) {
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
                case 6 -> MenuOption.STATEMENT;
                case 7 -> MenuOption.CONCURRENCY_DEMO;
                case 8 -> MenuOption.TRANSACTION_PROCESSING_DEMO;
                case 9 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                System.out.println("Invalid choice. Enter a number from 1 to 9.");
                continue;
            }

            try {
                switch (option) {
                    case OPEN_ACCOUNT -> openAccount(input, accounts, customers);
                    case DEPOSIT -> deposit(input, accounts);
                    case WITHDRAW -> withdraw(input, accounts);
                    case TRANSFER -> transfer(input, accounts);
                    case LIST_ACCOUNTS -> listAccounts(accounts);
                    case STATEMENT -> printStatement(input, accounts);
                    case CONCURRENCY_DEMO -> runConcurrencyDemo();
                    case TRANSACTION_PROCESSING_DEMO -> runTransactionProcessingDemo();
                    case EXIT -> {
                        running = false;
                        System.out.println("Thank you for banking with MiniBank. Goodbye!");
                    }
                }
            } catch (InsufficientFundsException e) {
                System.out.println("Transaction failed: short by " + e.getShortfall() + " rupees.");
            } catch (AccountNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (BankException e) {
                System.out.println("Transaction failed: " + e.getMessage());
            } finally {
                if (option == MenuOption.DEPOSIT || option == MenuOption.WITHDRAW
                        || option == MenuOption.TRANSFER) {
                    System.out.println("Transaction attempt finished.");
                }
            }
        }
    }

    private static void runConcurrencyDemo() {
        final int workerCount = 10;
        final int depositsPerWorker = 1000;
        final long depositAmount = 1;
        long expectedBalance = (long) workerCount * depositsPerWorker * depositAmount;

        System.out.println("\nThread Safety Demo");
        System.out.println("Workers: " + workerCount + ", deposits per worker: "
                + depositsPerWorker + ", amount: " + depositAmount);

        Account raceAccount = new SavingsAccount("Race Demo", 0, 0);
        try {
            runWorkers(raceAccount, workerCount, depositsPerWorker, depositAmount, false);
            System.out.println("Balance before synchronization: "
                    + raceAccount.getBalance() + " (expected " + expectedBalance + ")");

            Account safeAccount = new SavingsAccount("Synchronized Demo", 0, 0);
            runWorkers(safeAccount, workerCount, depositsPerWorker, depositAmount, true);
            System.out.println("Balance after synchronization: "
                    + safeAccount.getBalance() + " (expected " + expectedBalance + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Concurrency demo interrupted.");
        }
    }

    private static void runWorkers(Account account, int workerCount, int times,
                                   long amount, boolean synchronizedDeposit)
            throws InterruptedException {
        Thread[] workers = new Thread[workerCount];
        for (int i = 0; i < workerCount; i++) {
            Runnable task = synchronizedDeposit
                    ? new AccountWorker(account, times, amount)
                    : new UnsafeAccountWorker(account, times, amount);
            String prefix = synchronizedDeposit ? "safe-deposit-" : "race-deposit-";
            workers[i] = new Thread(task, prefix + (i + 1));
            workers[i].start();
        }
        for (Thread worker : workers) {
            worker.join();
        }
    }

    private static void runTransactionProcessingDemo() {
        System.out.println("\nTransaction Processing Demo");
        runProducerConsumerBatch();
        runDeadlockAndLockOrderingDemo();
    }

    private static void runProducerConsumerBatch() {
        final int transactionCount = 10;
        final long amountPerTransaction = 100;
        Account batchAccount = new SavingsAccount("Batch Demo", 0, 0);
        TransactionBuffer buffer = new TransactionBuffer(3);
        TransactionProcessor processor = new TransactionProcessor();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= transactionCount; i++) {
                    buffer.put(new AccountWorker(batchAccount, 1, amountPerTransaction));
                    System.out.println("Producer queued transaction " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "transaction-producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= transactionCount; i++) {
                    processor.submit(buffer.take());
                    System.out.println("Consumer submitted transaction " + i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "transaction-consumer");

        try {
            producer.start();
            consumer.start();
            producer.join();
            consumer.join();
            processor.stop();
            System.out.println("Batch completed. Final balance: "
                    + batchAccount.getBalance() + " (expected "
                    + transactionCount * amountPerTransaction + ")");
        } catch (InterruptedException e) {
            processor.stopNow();
            Thread.currentThread().interrupt();
            System.out.println("Transaction batch interrupted.");
        }
    }

    private static void runDeadlockAndLockOrderingDemo() {
        Account unsafeA = new CurrentAccount("Unsafe A", 1000, 5000);
        Account unsafeB = new CurrentAccount("Unsafe B", 1000, 5000);
        CountDownLatch bothThreadsHoldFirstLock = new CountDownLatch(2);

        Thread unsafeTransferOne = new Thread(
                () -> transferWithOppositeLockOrder(unsafeA, unsafeB, bothThreadsHoldFirstLock),
                "unsafe-A-to-B");
        Thread unsafeTransferTwo = new Thread(
                () -> transferWithOppositeLockOrder(unsafeB, unsafeA, bothThreadsHoldFirstLock),
                "unsafe-B-to-A");
        unsafeTransferOne.setDaemon(true);
        unsafeTransferTwo.setDaemon(true);
        unsafeTransferOne.start();
        unsafeTransferTwo.start();

        try {
            unsafeTransferOne.join(500);
            unsafeTransferTwo.join(500);
            System.out.println("Opposite lock order deadlocked: "
                    + (unsafeTransferOne.isAlive() && unsafeTransferTwo.isAlive()));

            Account safeA = new CurrentAccount("Safe A", 1000, 5000);
            Account safeB = new CurrentAccount("Safe B", 1000, 5000);
            Thread safeTransferOne = new Thread(
                    () -> transferWithOrderedLocks(safeA, safeB, 500), "safe-A-to-B");
            Thread safeTransferTwo = new Thread(
                    () -> transferWithOrderedLocks(safeB, safeA, 500), "safe-B-to-A");
            safeTransferOne.start();
            safeTransferTwo.start();
            safeTransferOne.join();
            safeTransferTwo.join();
            System.out.println("Ordered-lock transfers completed. Final balances: "
                    + safeA.getBalance() + " and " + safeB.getBalance());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Deadlock demonstration interrupted.");
        }
    }

    private static void transferWithOppositeLockOrder(Account from, Account to,
                                                      CountDownLatch ready) {
        synchronized (from) {
            ready.countDown();
            try {
                ready.await();
                synchronized (to) {
                    from.transfer(to, 100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BankException e) {
                System.out.println(Thread.currentThread().getName()
                        + " failed: " + e.getMessage());
            }
        }
    }

    private static void transferWithOrderedLocks(Account from, Account to, long amount) {
        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0
                ? from : to;
        Account second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                try {
                    from.transfer(to, amount);
                    System.out.println(Thread.currentThread().getName() + " completed.");
                } catch (BankException e) {
                    System.out.println(Thread.currentThread().getName()
                            + " failed: " + e.getMessage());
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("Main Menu");
        System.out.println("---------");
        System.out.println("1. Open account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. List accounts");
        System.out.println("6. View statement");
        System.out.println("7. Run concurrency demo");
        System.out.println("8. Process transactions");
        System.out.println("9. Exit");
    }

    private static void openAccount(Scanner input, List<Account> accounts,
                                    List<Customer> customers) {
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
        System.out.print("Email address: ");
        String email = input.nextLine().trim();
        if (!isValidEmail(email)) {
            System.out.println("Invalid email address.");
            return;
        }
        System.out.print("Mobile number: ");
        String mobile = input.nextLine().trim();
        if (!isValidMobile(mobile)) {
            System.out.println("Invalid mobile number. Use a 10-digit number starting with 6-9.");
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
        Customer customer = new Customer(owner, email, mobile);
        customers.add(customer);
        accounts.add(account);
        System.out.println("Customer created: " + customer.getCustomerId());
        System.out.println("Account opened: " + account);
        System.out.printf("Interest rate: %.1f%% per year%n", account.interestRate());
    }

    private static void deposit(Scanner input, List<Account> accounts)
            throws BankException {
        Account account = findAccount(input, accounts, "Account number: ");
        long amount = readLong(input, "Deposit amount: ");
        account.deposit(amount);
        System.out.println("Deposit successful. New balance: " + account.getBalance());
    }

    private static void withdraw(Scanner input, List<Account> accounts)
            throws BankException {
        Account account = findAccount(input, accounts, "Account number: ");
        long amount = readLong(input, "Withdrawal amount: ");
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account candidate, long requestedAmount) {
                return requestedAmount > 0 && candidate.canWithdraw(requestedAmount);
            }
        };
        WithdrawRule lambdaRule = (candidate, requestedAmount) ->
                requestedAmount > 0 && candidate.canWithdraw(requestedAmount);

        boolean anonymousAllowed = anonymousRule.allow(account, amount);
        boolean lambdaAllowed = lambdaRule.allow(account, amount);
        System.out.println("Anonymous WithdrawRule allows: " + anonymousAllowed);
        System.out.println("Lambda WithdrawRule allows: " + lambdaAllowed);

        if (anonymousAllowed && lambdaAllowed) {
            account.withdraw(amount);
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } else {
            account.withdraw(amount); // Throws a checked exception with the reason.
        }
    }

    private static void transfer(Scanner input, List<Account> accounts)
            throws BankException {
        Account from = findAccount(input, accounts, "From account number: ");
        Account to = findAccount(input, accounts, "To account number: ");
        long amount = readLong(input, "Transfer amount: ");
        from.transfer(to, amount);
        System.out.println("Transfer successful.");
    }

    private static void listAccounts(List<Account> accounts) {
        System.out.println("\nAccount list:");
        for (Account account : accounts.toArray(Account[]::new)) {
            System.out.printf("%s | interest %.1f%% per year | estimated yearly interest: %.2f%n",
                    account, account.interestRate(), account.yearlyInterest());
            if (account instanceof Premium) {
                System.out.println("  Premium account");
            }
            if (account instanceof FixedDepositAccount fixedDeposit) {
                System.out.println("  Fixed deposit is locked; withdrawals are not allowed.");
            }
        }
    }

    private static void printStatement(Scanner input, List<Account> accounts)
            throws AccountNotFoundException {
        Account account = findAccount(input, accounts, "Account number: ");
        System.out.print(StatementFormatter.buildStatement(account));
        System.out.printf("Yearly interest estimate: %.2f%n", account.yearlyInterest());
    }

    private static Account findAccount(Scanner input, List<Account> accounts, String prompt)
            throws AccountNotFoundException {
        System.out.print(prompt);
        String accountNumber = input.nextLine().trim();
        for (Account account : accounts) {
            if (account.getAccountNumber().equalsIgnoreCase(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException(accountNumber);
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
