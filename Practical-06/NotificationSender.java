@FunctionalInterface
interface Notifier {
    void send(String message);
}

// Marker interface
interface Urgent {
}

class EmailSender implements Urgent {

    private final Notifier notifier;

    EmailSender() {
        notifier = message ->
                System.out.println("Email: " + message);
    }

    public void send(String message) {
        notifier.send(message);
    }
}

class SmsSender implements Urgent {

    private final Notifier notifier;

    SmsSender() {
        notifier = message ->
                System.out.println("SMS: " + message);
    }

    public void send(String message) {
        notifier.send(message);
    }
}

public class NotificationSender {
    public static void main(String[] args) {

        EmailSender email = new EmailSender();
        SmsSender sms = new SmsSender();

        // Array containing both senders
        Object[] senders = {
            email,
            sms
        };

        String message = "Practical 6 notification";

        System.out.println("Broadcasting message:\n");

        for (Object sender : senders) {

            if (sender instanceof EmailSender) {

                EmailSender emailSender = (EmailSender) sender;

                // Send once
                emailSender.send(message);

                // Urgent sender -> send twice
                if (sender instanceof Urgent) {
                    emailSender.send(message);
                }

            } else if (sender instanceof SmsSender) {

                SmsSender smsSender = (SmsSender) sender;

                // Send once
                smsSender.send(message);

                // Urgent sender -> send twice
                if (sender instanceof Urgent) {
                    smsSender.send(message);
                }
            }
        }
    }
}
