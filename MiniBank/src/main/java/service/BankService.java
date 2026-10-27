package service;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.AnnotationValidator;
import util.StatementFormatter;
import util.Validator;

/** Coordinates account validation, persistence-independent operations, and audit logging. */
public final class BankService {
    private final Repository<Account> accounts;
    private final AccountStore accountStore;
    private final Path logFile;

    public BankService(Repository<Account> accounts, Path logFile) {
        this(accounts, logFile, new AccountStore());
    }

    public BankService(Repository<Account> accounts, Path logFile, AccountStore accountStore) {
        this.accounts = accounts;
        this.logFile = logFile;
        this.accountStore = accountStore;
    }

    public Account openAccount(String owner, String email, String mobile, int type,
                               long openingBalance) throws BankException {
        if (owner == null || owner.isBlank()) throw new InvalidAmountException("Owner name is required.");
        if (!Validator.isValidEmail(email)) throw new InvalidAmountException("Invalid email address.");
        if (!Validator.isValidMobile(mobile)) throw new InvalidAmountException("Invalid mobile number.");
        if (openingBalance < 0) throw new InvalidAmountException("Opening balance cannot be negative.");
        Account account = switch (type) {
            case 1 -> new SavingsAccount(owner, openingBalance, 1000);
            case 2 -> new CurrentAccount(owner, openingBalance, 5000);
            case 3 -> new FixedDepositAccount(owner, openingBalance);
            default -> throw new InvalidAmountException("Invalid account type.");
        };
        String[] errors = AnnotationValidator.validate(account);
        if (errors.length != 0) throw new InvalidAmountException(String.join(", ", errors));
        accounts.save(account);
        accountStore.add(account);
        return account;
    }

    public void register(Account account) {
        accounts.save(account);
        accountStore.add(account);
    }

    public void deposit(String accountNumber, long amount) throws BankException {
        Account account = requireAccount(accountNumber);
        account.deposit(amount);
        log("DEPOSIT", accountNumber, amount);
        accountStore.record("DEPOSIT " + accountNumber + " " + amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException {
        Account account = requireAccount(accountNumber);
        account.withdraw(amount);
        log("WITHDRAW", accountNumber, amount);
        accountStore.record("WITHDRAW " + accountNumber + " " + amount);
    }

    public void transfer(String fromNumber, String toNumber, long amount) throws BankException {
        Account from = requireAccount(fromNumber);
        Account to = requireAccount(toNumber);
        from.transfer(to, amount);
        log("WITHDRAW", fromNumber, amount);
        log("DEPOSIT", toNumber, amount);
        accountStore.record("WITHDRAW " + fromNumber + " " + amount);
        accountStore.record("DEPOSIT " + toNumber + " " + amount);
    }

    public String statement(String accountNumber) throws AccountNotFoundException {
        return StatementFormatter.buildStatement(requireAccount(accountNumber));
    }

    public List<Account> listAccounts() {
        return new ArrayList<>(accountStore.sortedByBalance());
    }

    public Repository<Account> getRepository() { return accounts; }

    private Account requireAccount(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.findById(accountNumber);
        if (account == null) throw new AccountNotFoundException(accountNumber);
        return account;
    }

    private void log(String type, String accountNumber, long amount) throws BankException {
        try {
            TransactionLog.append(logFile, type + " " + accountNumber + " " + amount);
        } catch (IOException e) {
            throw new BankException("Transaction completed but its log could not be written: "
                    + e.getMessage());
        }
    }
}
