package bank;

public class MiniBank {

    public static void main(String[] args) {

        System.out.println("Validator Tests:");

        System.out.println("Mobile valid: "
                + Validator.isValidMobile("9876543210"));

        System.out.println("Mobile invalid: "
                + Validator.isValidMobile("1234567890"));

        System.out.println("Email valid: "
                + Validator.isValidEmail("dhruva@gmail.com"));

        System.out.println("Email invalid: "
                + Validator.isValidEmail("dhruva@gmail"));

        System.out.println("PAN valid: "
                + Validator.isValidPan("ABCDE1234F"));

        System.out.println("PAN invalid: "
                + Validator.isValidPan("ABC123"));

        System.out.println("IFSC valid: "
                + Validator.isValidIfsc("SBIN0001234"));

        System.out.println("IFSC invalid: "
                + Validator.isValidIfsc("SBI123"));

        System.out.println();

        System.out.println("Command Parser:");

        Command command =
                CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Type: " + command.type());
        System.out.println("Account Number: "
                + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        System.out.println();

        Account account =
                new Account("Dhruva", 5000);

        System.out.println(
                StatementFormatter.buildStatement(account));
    }
}