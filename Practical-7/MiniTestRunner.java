import java.lang.annotation.*;
import java.lang.reflect.Method;

// Custom Run annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

// Class containing test methods
class MyTests {

    @Run
    public void testAddition() {
        System.out.println("Test Addition executed");
    }

    @Run
    public void testSubtraction() {
        System.out.println("Test Subtraction executed");
    }

    public void normalMethod() {
        System.out.println("This method should not run");
    }

    @Run
    public void testMultiplication() {
        System.out.println("Test Multiplication executed");
    }
}

// Mini test runner
public class MiniTestRunner {

    public static void main(String[] args) throws Exception {

        MyTests testObject = new MyTests();
        int count = 0;

        Method[] methods = MyTests.class.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {

                method.invoke(testObject);
                count++;
            }
        }

        System.out.println("Total tests executed: " + count);
    }
}