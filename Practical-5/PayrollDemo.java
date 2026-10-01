abstract class Employee {
    String name;
    int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }
    abstract double monthlySalary();
}
class FullTime extends Employee {
    double salary;
    FullTime(String name, int id, double salary) {
        super(name, id);
        this.salary = salary;
    }
    double monthlySalary() {
        return salary;
    }
}
class PartTime extends Employee {
    double hours;
    double rate;

    PartTime(String name, int id, double hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }
    double monthlySalary() {
        return hours * rate;
    }
}
class Intern extends Employee {
    double stipend;
    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }
    double monthlySalary() {
        return stipend;
    }
}
public class PayrollDemo {
    public static void main(String[] args) {
        Employee[] employees = {
            new FullTime("rajesh", 101, 5000),
            new PartTime("ayush", 102, 80, 20),
            new Intern("raghvi", 103, 1500),
            new FullTime("pooja", 104, 4500)
        };
        double total = 0;
        for (Employee employee : employees) {
            double salary = employee.monthlySalary();
            System.out.println(employee.name + " (ID: " + employee.id + "): Salary = " + salary);
            if (employee instanceof Intern) {
                System.out.println("Intern");
            }
            total += salary;
        }
        System.out.println("Total payroll = " + total);
    }
}
