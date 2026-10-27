public class thermostat {
    private String location;
    private int temperature;
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;
    public thermostat(String location , int startTemp){
        this.location = location;
    
        if(startTemp >= MIN && startTemp <= MAX){
            this.temperature = startTemp;
        }else {
            this.temperature = 22;
        }
    activeCount++;  
 } 
 thermostat(String location){
    this(location,22);
}
public void raise(){
    if(temperature < MAX){
        temperature++;
    }else{
        System.out.println("Already at maximum(30)");
    }
}
public void lower(){
    if(temperature > MIN){
        temperature--;
    }
    }
    public int getTemperature(){
        return temperature;
    }
      public static int getActiveCount() {
        return activeCount;
    }

    public static void main(String[] args) {

        thermostat t1 = new thermostat("Bedroom", 20);
        thermostat t2 = new thermostat("Living Room");
        for (int i = 0; i < 10; i++) {
            t1.raise();
            System.out.println("Temperature: " + t1.getTemperature());
        }

        for (int i = 0; i < 20; i++) {
            t1.lower();
            System.out.println("Temperature: " + t1.getTemperature());
        }
        System.out.println("Active thermostats: " + thermostat.getActiveCount());
    }
}
 
