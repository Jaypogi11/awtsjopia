
package jay11;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class product {
   

    Scanner sc = new Scanner(System.in);
    public void ptransaction() {
         Scanner sc = new Scanner(System.in);
          
        String response = null;
        do {
            System.out.println("\n---------------------------");
            System.out.println("PRoDuct PANEL:");
            System.out.println("1. Add PRoDuct:");
            System.out.println("2. View PRoDuct:");
            System.out.println("3. Update PRoDuct:");
            System.out.println("4. Delete PRoDuct:");
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
                    addproduct();
                    break;
                case 2:
                    viewproduct();
                    break;
                case 3:
                     viewproduct();
                    updateproduct();
                     viewproduct();
                    break;
                case 4:
                     viewproduct();
                    deleteproduct();
                     viewproduct();
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
 // Method to add a product
    public void addproduct() {
    
        
        String pname;
        while (true) {
            System.out.print("Enter Product Name: ");
            pname = sc.nextLine().trim();
            if (!pname.isEmpty() && pname.length() > 1 && Pattern.matches("^[a-zA-Z\\s]+$", pname)) {
                break;  // Valid product name
            } else {
                System.out.println("Invalid Product name. (letters and spaces only, and must be more than one character).");
            }
        }

        double pprice;
        while (true) {
            System.out.print("Enter Product Price: ");
            try {
                pprice = sc.nextDouble();
                if (pprice > 0) {
                    break;  // Valid price
                } else {
                    System.out.println("Price must be a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the price.");
                sc.nextLine();  // Clear the buffer
            }
        }

        double pstock;
        while (true) {
            System.out.print("Enter Product Stock: ");
            try {
                pstock = sc.nextDouble();
                if (pstock >= 0) {
                    break;  // Valid stock
                } else {
                    System.out.println("Stock must be a non-negative number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the stock.");
                sc.nextLine();  // Clear the buffer
            }
        }

        // Insert the product into the database
        String qry = "INSERT INTO tbl_product (p_name, p_price, p_stock) VALUES (?, ?, ?)";
        config conf = new config();
        conf.addRecord(qry, pname, pprice, pstock);
    }

    // Method to view all products
    public void viewproduct() {
        String qry = "SELECT * FROM tbl_product";
        String[] headers = {"p_id", "p_name", "p_price", "p_stock"};
        String[] columns = {"p_id", "p_name", "p_price", "p_stock"};
        config conf = new config();
        conf.viewRecords(qry, headers, columns);
    }

    // Method to update product information
    public void updateproduct() {
        config conf = new config();
        int pid = 0;
        boolean validID = false;

        while (!validID) {
            System.out.print("Enter Product ID to update: ");
            if (sc.hasNextInt()) {
                pid = sc.nextInt();
            
                if (conf.getSingleValue("SELECT p_id FROM tbl_product WHERE p_id = ?", pid) != 0) {
                    validID = true;  // Valid ID
                } else {
                    System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.nextLine();  // Clear the buffer
            }
        }

        // Input validation for product name
        sc.nextLine();  // Clear the buffer
        String pname;
        while (true) {
            System.out.print("Enter New Product Name: ");
            pname = sc.nextLine().trim();
            if (!pname.isEmpty() && pname.length() > 1 && Pattern.matches("^[a-zA-Z\\s]+$", pname)) {
                break;  // Valid name
            } else {
                System.out.println("Invalid name. (letters and spaces only, and must be more than one character).");
            }
        }

        // Input validation for product price
        double pprice;
        while (true) {
            System.out.print("Enter New Product Price: ");
            try {
                pprice = sc.nextDouble();
                if (pprice > 0) {
                    break;  // Valid price
                } else {
                    System.out.println("Price must be a positive number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the price.");
                sc.nextLine();  // Clear the buffer
            }
        }

        // Input validation for product stock
        double pstock;
        while (true) {
            System.out.print("Enter New Product Stock: ");
            try {
                pstock = sc.nextDouble();
                if (pstock >= 0) {
                    break;  // Valid stock
                } else {
                    System.out.println("Stock must be a non-negative number.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for the stock.");
                sc.nextLine();  // Clear the buffer
            }
        }

        // Update the product in the database
        String qry = "UPDATE tbl_product SET p_name = ?, p_price = ?, p_stock = ? WHERE p_id = ?";
        conf.updateRecord(qry, pname, pprice, pstock, pid);
    }

    // Method to delete a product
    public void deleteproduct() {
        config conf = new config();
        int pid = 0;
        while (true) {
            System.out.print("Enter Product ID to delete: ");
            if (sc.hasNextInt()) {
                pid = sc.nextInt();
                if (conf.getSingleValue("SELECT p_id FROM tbl_product WHERE p_id = ?", pid) != 0) {
                    break;  // Valid ID
                } else {
                    System.out.println("Product ID doesn't exist. Try again.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.next();  // Clear the buffer
            }
        }

        String qry = "DELETE FROM tbl_product WHERE p_id = ?";
        conf.deleteRecord(qry, pid);
        System.out.println("Product with ID " + pid + " has been deleted.");
    }
}


