public class cinemashow {
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;
    
    public cinemashow(String title , int capacity){
        this.title = title;
        this.capacity = capacity;
        this. seatsAvailable= capacity;
    }

    public cinemashow(String title){
        this(title,100);
    }
    public boolean book(int n){
        if( n <= seatsAvailable){
            seatsAvailable -= n ;
           totalBooked +=  n;
           return true; 
        }else{
            return false ;
        }
    }
    public void cancle(int n){
        seatsAvailable += n;
        seatsAvailable = capacity;
    }
    public int getSeatAvailable(){
        return seatsAvailable;
    }
    public static int getTotalBooked(){
        return totalBooked ;
    }
    public static void main(String []args){
        cinemashow show = new cinemashow("avatar", 50);
        System.out.println("Movie:" + show.title);
        System.out.println("Initial seats: " + show.getSeatAvailable());
        
        System.out.println("Book 20 seats: " + show.book(20));
        System.out.println("Seats available: " + show.getSeatAvailable());

        System.out.println("Book 15 seats: " + show.book(15));
        System.out.println("Seats available: " + show.getSeatAvailable());

        System.out.println("Book 20 seats: " + show.book(20));
        System.out.println("Seats available: " + show.getSeatAvailable());

        show.cancle(10);
        System.out.println("Cancel 10 seats");
        System.out.println("Seats available: " + show.getSeatAvailable());

        System.out.println("Book 10 seats: " + show.book(10));
        System.out.println("Seats available: " + show.getSeatAvailable());

        show.cancle(100);
        System.out.println("Cancel 100 seats");
        System.out.println("Seats available: " + show.getSeatAvailable());

        System.out.println("Total successfully booked seats: "
                + cinemashow.getTotalBooked());
    

    }
}
