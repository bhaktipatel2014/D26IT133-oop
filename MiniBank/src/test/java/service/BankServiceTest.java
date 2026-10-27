package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exception.InsufficientFundsException;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BankServiceTest {
    @TempDir Path temporaryDirectory;

    @Test
    void depositIncreasesTheAccountBalance() throws Exception {
        Account account = new SavingsAccount("Test", 5000, 0);
        BankService service = new BankService(new Repository<>(),
                temporaryDirectory.resolve("transactions.log"));
        service.register(account);

        service.deposit(account.getAccountNumber(), 250);

        assertEquals(5250, account.getBalance(), 0);
    }

    @Test
    void withdrawalOverAvailableBalanceThrows() throws Exception {
        Account account = new SavingsAccount("Test", 5000, 0);
        BankService service = new BankService(new Repository<>(),
                temporaryDirectory.resolve("transactions.log"));
        service.register(account);

        assertThrows(InsufficientFundsException.class,
                () -> service.withdraw(account.getAccountNumber(), 6000));
        assertEquals(5000, account.getBalance(), 0);
    }

    @Test
    void concurrentDepositsKeepTheBalanceCorrect() throws Exception {
        Account account = new SavingsAccount("Concurrent", 0, 0);
        int workers = 10;
        int depositsPerWorker = 1000;
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(workers);
        for (int i = 0; i < workers; i++) {
            executor.submit(() -> {
                start.await();
                for (int j = 0; j < depositsPerWorker; j++) account.deposit(1);
                return null;
            });
        }
        start.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals((double) workers * depositsPerWorker, account.getBalance(), 0);
    }
}
