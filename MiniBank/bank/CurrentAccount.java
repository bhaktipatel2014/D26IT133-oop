
package bank;

public class CurrentAccount extends Account {

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
    public boolean canWithdraw(long amount) {
        return amount >= 0 && balance - amount >= -overdraftLimit;
    }
}
