package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Account;

/** Account lookup and transaction-history collections introduced in Practical 12. */
public final class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> transactionHistory = new ArrayList<>();

    public synchronized void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    public synchronized Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public synchronized List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public synchronized List<Account> sortedByBalance() {
        List<Account> result = getAll();
        result.sort(Comparator.comparingDouble(Account::getBalance).reversed()
                .thenComparing(Account::getAccountNumber));
        return result;
    }

    public synchronized void record(String line) {
        transactionHistory.add(line);
    }

    public synchronized List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public synchronized Account[] toArray() {
        return accounts.values().toArray(Account[]::new);
    }
}
