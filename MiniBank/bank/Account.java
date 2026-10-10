package bank;

public class Account {

    private static int nextAccountNumber = 1001;

    private int accountNumber;
    private String ownerName;
    private double balance;

    public Account(String ownerName) {
        this.accountNumber = nextAccountNumber++;
        this.ownerName = ownerName;
        this.balance = 0;
    }

    public Account(String ownerName, double balance) {
        this.accountNumber = nextAccountNumber++;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Owner Name: " + ownerName
                + ", Balance: " + balance;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof Account) {
            Account other = (Account) o;
            return this.accountNumber == other.accountNumber;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(accountNumber);
    }
}


