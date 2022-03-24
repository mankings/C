import java.util.Scanner;
import java.util.Stack;

public class b1_3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Stack<Integer> stack = new Stack<Integer>();

        System.out.println("Notação pós-fixa");

        while (true) {
            String[] data = sc.nextLine().split(" ");
            if (data[0].equals("end")) {
                System.out.println("Exited.");
                break;
            }
            
            for (int i = 0; i <= data.length / 2; i++) {
                stack.push(Integer.parseInt(data[i]));
            }

            for (int i = 1 + (data.length / 2); i < data.length; i++) {
                String op = data[i];
                int result = 0;

                switch (op) {
                    case "+":
                        result = stack.pop() + stack.pop();
                        break;
                    case "-":
                        result = stack.pop() - stack.pop();
                        break;
                    case "*":
                        result = stack.pop() * stack.pop();
                        break;
                    case "/":
                        result = stack.pop() / stack.pop();
                        break;
                    default:
                        System.err.println("Error: invalid operator.");
                        System.exit(1);
                }

                stack.push(result);
            }

            System.out.println("Result: " + stack.peek());
        }

        sc.close();
    }
}
