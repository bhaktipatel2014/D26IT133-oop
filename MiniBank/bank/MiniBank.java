package bank;

public class Customer implements Cloneable {
    private String name;
    private Address address;

    public Customer(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    // Static nested class for Address
    public static class Address implements Cloneable {
        private String line;
        private String city;
        private String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        @Override
        public Address clone() {
            try {
                return (Address) super.clone();
            } catch (CloneNotSupportedException e) {
                return new Address(this.line, this.city, this.pincode);
            }
        }
    }

    @Override
    public Customer clone() {
        try {
            Customer cloned = (Customer) super.clone();
            // Perform deep clone for mutable reference objects
            if (this.address != null) {
                cloned.address = this.address.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            return new Customer(this.name, this.address != null ? this.address.clone() : null);
        }
    }
}