import java.util.*;
     record vehicle(String number ,String type) {}
        class Tollbooth{
            public static void main(String[]args){
                Scanner sc = new Scanner(System.in);
                int Tollbooth = 0;
                int bikecount = 0;
                int carcount = 0;
                int truckcount = 0;
                while (true){
                    System.out.println("Enter the vehicle number (or done): ");
                    String number = sc.nextLine();
                    if(number.equalsIgnoreCase("done")){
                        break;
                    }
                    System.out.println("Enter the vehicle type (bike/car/truck) :");
                    String type = sc.nextLine().toLowerCase();
                    vehicle v = new vehicle(number,type );
                    
                    int toll = switch (v.type()){
                        case "bike" ->{
                             bikecount ++;
                             yield 20;
                        }
                        case "car" -> {
                            carcount++;
                            yield 50;
                        }
                        case "truck" -> {
                            truckcount++;
                             yield 150;
                        }
                     
                        default ->{
                            System.out.println("invalid vehicle type.");
                            yield 0;
                        }
                    };
                    Tollbooth += toll;
                    System.out.println("Total toll: " + Tollbooth);

                
                }
        
            if (bikecount >= carcount && bikecount >= truckcount){
                 System.out.println("most frequent type is bike");
            }else if(carcount >=bikecount && carcount >=truckcount){
                 System.out.println("most frequent type is car");
            
            }else if (truckcount >= bikecount && truckcount >= carcount){
                    System.out.println("most frequent type is truck ");
            }else {
                System.out.println("there is no frequent vehicle.");
            }
            
         
             

        }
    
        }
