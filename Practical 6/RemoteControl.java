
interface Switchable {
    void on();
    void off();

    default void toggle() {
        on();
    }
}

class Fan implements Switchable {

    @Override
    public void on() {
        System.out.println("Fan is ON");
    }

    @Override
    public void off() {
        System.out.println("Fan is OFF");
    }
}

class Light implements Switchable {

    @Override
    public void on() {
        System.out.println("Light is ON");
    }

    @Override
    public void off() {
        System.out.println("Light is OFF");
    }
}

@FunctionalInterface
interface SwitchPermission {
    boolean maySwitchOn(Switchable device, int hour);
}

public class RemoteControl {

    public static void main(String[] args) {
        Switchable[] devices = {
            new Fan(),
            new Light()
        };
        System.out.println("Toggling devices:");

        for (Switchable device : devices) {
            device.toggle();
        }
        SwitchPermission anonymousPermission = new SwitchPermission() {

            @Override
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour < 22;
            }
        };
        SwitchPermission lambdaPermission =
                (device, hour) -> hour >= 8 && hour < 20;

        int hour = 19;
        System.out.println();
        System.out.println("Using anonymous class:");

        for (Switchable device : devices) {

            if (anonymousPermission.maySwitchOn(device, hour)) {
                device.on();
            } else {
                device.off();
            }
        }
        System.out.println();
        System.out.println("Using lambda expression:");

        for (Switchable device : devices) {

            if (lambdaPermission.maySwitchOn(device, hour)) {
                device.on();
            } else {
                device.off();
            }
        }
    }
}
