package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import model.Account;

/** Runs bank transaction tasks on a fixed pool of four worker threads. */
public final class TransactionProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();

    public void submit(Runnable task) {
        executor.execute(task);
    }

    /** Registers an account in the shared thread-safe index and schedules its transaction. */
    public void submit(Account account, Runnable task) {
        sharedAccounts.put(account.getAccountNumber(), account);
        submit(task);
    }

    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }

    public void stop() throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Transaction workers did not stop.");
            }
        }
    }

    public void stopNow() {
        executor.shutdownNow();
    }
}
