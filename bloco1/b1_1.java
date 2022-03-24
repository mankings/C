import java.util.Scanner;

public class b1_1 {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        double a = 0, b = 0, result = 0;
        String op = null;

        System.out.print("Operation (number op number): ");

        if (sc.hasNextDouble()) {
            a = sc.nextDouble();
        } else {
            System.err.println("Error: First input needs to be a double");
            System.exit(1);
        }

        if (sc.hasNext()) {
            op = sc.next().trim();
        } else {
            System.err.println("Error: Second input needs to be the operator (+ - * /)");
            System.exit(1);
        }

        if (sc.hasNextDouble()) {
            b = sc.nextDouble();
        } else {
            System.err.println("Error: Third input needs to be a double");
            System.exit(1);
        }
        
        sc.close();

        switch (op) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = a / b;
                break;
            default:
                System.err.println("Error: invalid operator.");
                System.exit(1);
        }

        System.out.printf("%f %s %f = %f\n", a, op, b, result);
    }
}