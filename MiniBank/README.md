# MiniBank

MiniBank is a Java 17 console banking application. Its source files are organized
into `model`, `util`, and `service` packages under `src/main/java`.

## Build and run

From this folder, run:

```bash
./build.sh
java -jar build/minibank.jar
```

The menu lets you open accounts, deposit, withdraw, transfer, list accounts,
view a statement, and exit. New accounts ask for a valid email and mobile number.
Withdrawal rules are demonstrated with both an anonymous class and a lambda.

## Package layout

- `model`: accounts, customers, transactions, and account interfaces
- `util`: validation, command parsing, and statement formatting
- `service`: the menu application and withdrawal rule
