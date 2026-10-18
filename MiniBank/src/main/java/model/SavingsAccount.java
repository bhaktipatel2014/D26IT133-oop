
package model;

public class SavingsAccount extends Account {

    private long minBalance;

    public SavingsAccount(String ownerName, double balance,
                          long minBalance) {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return amount >= 0 && balance - amount >= minBalance;
    }
}
