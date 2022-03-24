import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class b1_4 {
    public static void main(String[] args) {
        HashMap<String, Integer> numbersDic = new HashMap<String, Integer>();
        Scanner inputScanner = null;
        try {
            inputScanner = new Scanner(new File("numbers.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File 'numbers.txt' not found!");
            System.exit(1);
        }

        while(inputScanner.hasNextLine()) {
            String line = inputScanner.nextLine();
            String[] data = line.split(" - ");
            numbersDic.put(data[1],  Integer.parseInt(data[0]));
        }
        inputScanner.close();

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("Input: ");
            String[] data = sc.nextLine().split(" ");
            if (data[0].equals("end")) {
                System.out.println("Exited.");
                break;
            }

            for(String str : data) {
                if(numbersDic.containsKey(str)) {
                    System.out.print(numbersDic.get(str) + " ");
                } else {
                    System.out.print(str + " ");
                }
            }
            System.out.println();
        }

        sc.close();
    }
}
