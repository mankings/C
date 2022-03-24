import java.util.HashMap;
import java.util.Scanner;
import java.lang.Double;

public class b1_2 {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        String input = null;
        String[] data = null;
        String a = null, op = null, b = null;
        double result = 0;
        HashMap<String, Double> map = new HashMap<>();

        System.out.println("Operation: operand operator operand. Type 'end' to exit.");

        while (true) {
            input = sc.nextLine();
            if (input.equals("end"))
                break;

            data = input.split(" ");
            switch (data.length) {
                case 1:
                    a = data[0];
                    result = valueOf(a, map);
                    break;
                case 3:
                    a = data[0];
                    op = data[1];
                    b = data[2];
                    switch (op) {
                        case "+":
                            result = valueOf(a, map) + valueOf(b, map);
                            break;
                        case "-":
                            result = valueOf(a, map) - valueOf(b, map);
                            break;
                        case "*":
                            result = valueOf(a, map) * valueOf(b, map);
                            break;
                        case "/":
                            result = valueOf(a, map) / valueOf(b, map);
                            break;
                        case "=":
                            map.put(a, valueOf(b, map));
                            result = valueOf(b, map);
                            break;
                        default:
                            System.err.println("Error: invalid operator.");
                            System.exit(1);
                    }
                    break;
                case 5:
                    System.out.println("not done yet.");
                    break;
                default:
                    System.err.println("Error: Invalid input.");
                    System.exit(1);
            }

            System.out.printf("result = %f\n", result);
        }
        sc.close();
    }

    public static double valueOf(String str, HashMap<String, Double> map) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            if (map.containsKey(str)) {
                return map.get(str);
            } else {
                return 0;
            }
        }
    }
}
