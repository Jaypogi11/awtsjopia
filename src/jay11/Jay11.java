
package jay11;

import java.util.Scanner;
 import java.util.InputMismatchException;


    
public class Jay11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        boolean exit = true;
        do {
            System.out.println("\n---------------------------------------------");
            System.out.println("Welcome to Coffee Shop:");
            System.out.println("");
            System.out.println("1. Customer:");
            System.out.println("2. Product:");
            System.out.println("3. Order:");
            System.out.println("4. Report:");
            System.out.println("5. Exit:");
            System.out.println("\n---------------------------------------------");
            
            int act = 0;

           
            boolean validChoice = false;
            while (!validChoice) {
                System.out.println("Enter Choice: Please Enter to (1-5):");
                try {
                    act = sc.nextInt();
                    
                    if (act >= 1 && act <= 5) {
                        validChoice = true; 
                    } else {
                        System.out.println("Invalid choice! Please select a number between 1 to 5.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Only numbers to 1 to 5 !!!!!!.");
                    sc.nextLine(); 
                }
            }

            switch (act) {
                case 1:
                    customer ct =new customer();
                    ct.ctransaction();
                    break;
                case 2:
                    product pt =new product();
                    pt.ptransaction();
                    break;
                case 3:
                    order or =new order();
                   or.otransaction();
                   
                    break;
                case 4:
                    Reports rt = new Reports();
                    rt.generateReport();
                 
                    break;
                case 5:
                    
                    boolean validExit = false;
                    while (!validExit) {
                        System.out.print("Exit Selected... Type 'yes' to confirm: ");
                        String resp = sc.next();
                        if (resp.equalsIgnoreCase("yes")) {
                            exit = false;
                            validExit = true;
                        } else {
                            System.out.println("Exit canceled. Returning to the menu.");
                            validExit = true; 
                        }
                    }
                    break;
                default:
                 
                    System.out.println("Unexpected error.");
            }
        } while (exit);

        sc.close();
        System.out.println("Thank you for using the Coffee Shop System.");
    }
}
