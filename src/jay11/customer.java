
package jay11;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class customer {
   

    Scanner sc = new Scanner(System.in);
    public void ctransaction() {
         Scanner sc = new Scanner(System.in);
          
        String response = null;
        do {
            System.out.println("\n---------------------------");
            System.out.println("Customer PANEL:");
            System.out.println("1. Add Customer:");
            System.out.println("2. View Customer:");
            System.out.println("3. Update Customer:");
            System.out.println("4. Delete Customer:");
            System.out.println("5. Exit:");

            int act = 0;

            boolean validChoice = false;
            while (!validChoice) {
                System.out.println("Enter Choice (1-5 only ):");
                try {
                    act = sc.nextInt();

                    if (act >= 1 && act <= 5) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice! Please select a number between 1 to 5.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println(" Please enter a number between 1 to 5.");
                    sc.nextLine();  
                }
            }

            switch (act) {
                case 1:
                    addcustomer();
                    break;
                case 2:
                    viewcustomer();
                    break;
                case 3:
                     viewcustomer();
                    updatecustomer();
                     viewcustomer();
                    break;
                case 4:
                     viewcustomer();
                    deletecustomer();
                     viewcustomer();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Unexpected error.");
            }

            boolean validResponse = false;
            while (!validResponse) {
                System.out.print("Do you want to continue? (yes/no): ");
                response = sc.next();
                if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                    validResponse = true;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }

        } while (response.equalsIgnoreCase("yes"));
    }

    
    public void addcustomer() {
        String cname;
       
        while (true) {
            System.out.print("Enter Customer Name: ");
            cname = sc.nextLine().trim();
            if (!cname.isEmpty() && cname.length() > 1 && Pattern.matches("^[a-zA-Z\\s]+$", cname)) {
                break;  
            } else {
                System.out.println("Invalid name. (letters and spaces only, and must be more than one character).");
            }
        }

        String ccontact;
     
        while (true) {
            System.out.print("Enter Customer Contact: ");
            ccontact = sc.nextLine().trim();
            if (isPositiveInteger(ccontact)) {
                break;
            } else {
                System.out.println("Invalid Contact. Please input a number (e.g., 09657398014).");
            }
        }

        String cadd;
       
        while (true) {
            System.out.print("Enter Customer Address: ");
            cadd = sc.nextLine().trim();
            if (cadd.isEmpty()) {
                System.out.println("Address cannot be empty.");
            } else if (cadd.length() == 1 && Character.isLetter(cadd.charAt(0))) {
                System.out.println("Address cannot be a single letter.");
            } else if (cadd.matches("\\d+")) {
                System.out.println("Address cannot be a number.");
            } else {
                break;  
            }
        }

      
        String qry = "INSERT INTO tbl_customer (c_name, c_contact, c_address) VALUES (?, ?, ?)";
        config conf = new config();
        conf.addRecord(qry, cname, ccontact, cadd);
    }

    
    private boolean isPositiveInteger(String str) {
        return str.matches("\\d+");
    }

  
    public static void viewcustomer() {
        String qry = "SELECT * FROM tbl_customer";
        String[] headers = {"c_id", "c_name", "c_contact", "c_address"};
        String[] columns = {"c_id", "c_name", "c_contact", "c_address"};
        config conf = new config();
        conf.viewRecords(qry, headers, columns);
    }

   
    public void updatecustomer() {
        config conf = new config();
        int cid = 0;
        boolean validID = false;
      
        while (!validID) {
            System.out.print("Enter Customer ID to update: ");
            if (sc.hasNextInt()) {
                cid = sc.nextInt();
                sc.nextLine(); 
                if (conf.getSingleValue("SELECT c_id FROM tbl_customer WHERE c_id = ?", cid) != 0) {
                    validID = true;  
                } else {
                    System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.nextLine(); 
            }
        }

      
        String cname;
        while (true) {
            System.out.print("Enter New Customer Name: ");
            cname = sc.nextLine().trim();
            if (!cname.isEmpty() && cname.length() > 1 && Pattern.matches("^[a-zA-Z\\s]+$", cname)) {
                break;
            } else {
                System.out.println("Invalid name. (letters and spaces only, and must be more than one character).");
            }
        }

        System.out.print("Enter New Customer Contact: ");
        String ccontact = sc.nextLine().trim();
        while (!isPositiveInteger(ccontact)) {
            System.out.println("Invalid contact. Please enter a valid phone number.");
            System.out.print("Enter New Customer Contact: ");
            ccontact = sc.nextLine().trim();
        }

        System.out.print("Enter New Customer Address: ");
        String cadd = sc.nextLine().trim();
        while (cadd.isEmpty()) {
            System.out.println("Address cannot be empty.");
            System.out.print("Enter New Customer Address: ");
            cadd = sc.nextLine().trim();
        }

        String qry = "UPDATE tbl_customer SET c_name = ?, c_contact = ?, c_address = ? WHERE c_id = ?";
        conf.updateRecord(qry, cname, ccontact, cadd, cid);
    }

    
    public void deletecustomer() {
        config conf = new config();
        int cid = 0;
        while (true) {
            System.out.print("Enter Customer ID to delete: ");
            if (sc.hasNextInt()) {
                cid = sc.nextInt();
                if (conf.getSingleValue("SELECT c_id FROM tbl_customer WHERE c_id = ?", cid) != 0) {
                    break;
                } else {
                    System.out.println("Customer ID doesn't exist. Try again.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.next();  
            }
        }

        String qry = "DELETE FROM tbl_customer WHERE c_id = ?";
        conf.deleteRecord(qry, cid);
        System.out.println("Customer with ID " + cid + " has been deleted.");
    }
}

  