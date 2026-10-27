# MiniBank

MiniBank is a Java 17 console banking application built in layers. Its source is organized into `model`, `util`, and `service` packages under `src/main/java`.

## Build and run

```sh
./build.sh
java -jar build/minibank.jar
```

Run the Practical 14 JUnit 5 checks with Maven:

```sh
mvn test
```

## Features

- Open accounts, deposit, withdraw, transfer, list accounts, and view statements.
- Validate account/customer fields using runtime annotations and reflection.
- Persist accounts in `data/accounts.dat`; load on startup and save on exit.
- Append successful deposits, withdrawals, and both sides of transfers to `data/logs/transactions.log`.
- Generate an end-of-day deposits, withdrawals, net change, and log file metadata report from menu option 9.
- Use an `AccountStore` with a `HashMap`, history list, and balance comparator; `TransactionProcessor` indexes concurrently accessed accounts in a `ConcurrentHashMap`.
- Use a generic `Repository<T>` that reads entity keys from `@Id`.
- Run thread-safety, managed transaction pool, producer-consumer, and lock-order demonstrations.

Generated state and reports stay under `data/`, which is ignored by Git.

## Package layout

- `model`: accounts, customers, transactions, and account interfaces
- `model.annotation`: runtime annotations for identifiers and validation
- `exception`: checked bank exception hierarchy
- `util`: input and annotation validation, command parsing, and statement formatting
- `service`: bank service, collection store, generic repository, persistence, reporting, menu, and transaction processing
- `src/test/java`: JUnit 5 tests for deposits, insufficient funds, and concurrent deposits
