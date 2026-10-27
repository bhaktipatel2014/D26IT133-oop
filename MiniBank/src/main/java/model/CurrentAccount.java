
package model;

public class CurrentAccount extends Account implements Premium {

    private static final long serialVersionUID = 1L;

    private long overdraftLimit;

    public CurrentAccount(String ownerName, double balance,
                          long overdraftLimit) {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public synchronized boolean canWithdraw(long amount) {
        return amount >= 0 && balance - amount >= -overdraftLimit;
    }
}
