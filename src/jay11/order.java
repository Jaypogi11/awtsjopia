
package jay11;

import java.util.InputMismatchException;
import java.util.Scanner;

public class order {

    static order or;
   
    private Scanner sc = new Scanner(System.in);

    public void otransaction() {
        String response = null;
        do {
            System.out.println("\n---------------------------");
            System.out.println("OrDEr PANEL:");
            System.out.println("1. Add OrDEr");
            System.out.println("2. View OrDEr");
            System.out.println("3. Update OrDEr");
            System.out.println("4. Delete OrDEr");
            System.out.println("5. Exit");
            
            int act = 0;
            boolean validChoice = false;
            while (!validChoice) {
                System.out.println("Enter Choice (1-5):");
                try {
                    act = sc.nextInt();
                    if (act >= 1 && act <= 5) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid choice! Please select a number between 1 to 5.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Oops! Please enter a number between 1 to 5.");
                    sc.nextLine();  // Clear the buffer
                }
            }

            switch (act) {
                case 1:
                    addorder();
                    vieworder();
                    break;
                case 2:
                    vieworder();
                    break;
                case 3:
                    updateorder();
                    break;
                case 4:
                    deleteorder();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid selection. Please try again.");
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

    // Method to add a new order
    public void addorder() {
        config conf = new config();

        // Process customer selection
        customer ct = new customer();
        ct.ctransaction();

        // Get valid customer ID
        int cid = getValidCustomerId(conf);

        // Show products for selection
        product pt = new product();
        pt.viewproduct();

        // Get valid product ID
        int pid = getValidProductId(conf);

        // Get valid order date
        String odate = getValidOrderDate();

        // Get valid order amount
        Double oamount = getValidOrderAmount();

        // Get valid order status
        String ostatus = getValidOrderStatus();

        // SQL query to insert the new order
        String sql = "INSERT INTO tbl_order (c_id, p_id, o_date, o_amount, o_status) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, cid, pid, odate, oamount, ostatus);

        System.out.println("Order successfully added.");
    }

    // Method to get a valid customer ID
    private int getValidCustomerId(config conf) {
        int cid;
        while (true) {
            System.out.print("Enter the ID of the Customer: ");
            if (sc.hasNextInt()) {
                cid = sc.nextInt();
                String bsql = "SELECT c_id FROM tbl_customer WHERE c_id = ?";
                if (conf.getSingleValue(bsql, cid) != 0) {
                    sc.nextLine();  // Clear the buffer
                    break;
                }
                System.out.println("Customer does not exist. Please select a valid Customer ID.");
            } else {
                System.out.println("Invalid input! Customer ID must be a number.");
                sc.next();  // Clear invalid input
            }
        }
        return cid;
    }

    // Method to get a valid product ID
    private int getValidProductId(config conf) {
        int pid;
        while (true) {
            System.out.print("Enter the ID of the Product: ");
            if (sc.hasNextInt()) {
                pid = sc.nextInt();
                String psql = "SELECT p_id FROM tbl_product WHERE p_id = ?";
                if (conf.getSingleValue(psql, pid) != 0) {
                    sc.nextLine();  // Clear the buffer
                    break;
                }
                System.out.println("Product does not exist. Please select a valid Product ID.");
            } else {
                System.out.println("Invalid input! Product ID must be a number.");
                sc.next();  // Clear invalid input
            }
        }
        return pid;
    }

    // Method to get a valid order date
    private String getValidOrderDate() {
        String odate;
        while (true) {
            System.out.print("Enter Order Date (YYYY-MM-DD): ");
            odate = sc.nextLine().trim();
            if (odate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                break;  // Valid date format
            } else {
                System.out.println("Invalid date format. Please enter in the format YYYY-MM-DD.");
            }
        }
        return odate;
    }

    // Method to get a valid order amount
    private Double getValidOrderAmount() {
        Double oamount;
        while (true) {
            System.out.print("Enter Order Amount: ");
            if (sc.hasNextDouble()) {
                oamount = sc.nextDouble();
                if (oamount > 0) {
                    sc.nextLine();  // Clear the buffer
                    break;
                } else {
                    System.out.println("Amount must be a positive number.");
                }
            } else {
                System.out.println("Invalid input! Order Amount must be a number.");
                sc.next();  // Clear invalid input
            }
        }
        return oamount;
    }

    // Method to get a valid order status
    private String getValidOrderStatus() {
        String ostatus;
        while (true) {
            System.out.print("Enter Order Status (e.g., Pending, Completed, Canceled): ");
            ostatus = sc.nextLine().trim();
            if (ostatus.equalsIgnoreCase("Pending") || ostatus.equalsIgnoreCase("Completed") || ostatus.equalsIgnoreCase("Canceled")) {
                break;  // Valid status
            } else {
                System.out.println("Invalid status. Please enter one of the following: Pending, Completed, Canceled.");
            }
        }
        return ostatus;
    }
   
    // View orders
    public void vieworder() {
        config conf = new config();
        String projectQuery = "SELECT * FROM tbl_order "
                + "JOIN tbl_customer ON tbl_order.c_id = tbl_customer.c_id "
                + "JOIN tbl_product ON tbl_order.p_id = tbl_product.p_id";
        String[] projectHeaders = {"Order ID", "Customer Name", "Product Name", "Order Date", "Order Amount", "Order Status"};
        String[] projectColumns = {"o_id", "c_name", "p_name", "o_date", "o_amount", "o_status"};

        conf.viewRecords(projectQuery, projectHeaders, projectColumns);
    }

    // Method to update an existing order
    public void updateorder() {
        config conf = new config();
        int oid = getValidOrderId(conf); 
        int cid = getValidCustomerId(conf);
        int pid = getValidProductId(conf);
        String odate = getValidOrderDate();
        Double oamount = getValidOrderAmount();
        String ostatus = getValidOrderStatus();

        // SQL query to update the order
        String sql = "UPDATE tbl_order SET c_id = ?, p_id = ?, o_date = ?, o_amount = ?, o_status = ? WHERE o_id = ?";
        conf.updateRecord(sql, cid, pid, odate, oamount, ostatus, oid);

        System.out.println("Order updated successfully.");
    }

    // Method to get valid order ID
    private int getValidOrderId(config conf) {
        int oid = 0;
        boolean validID = false;
        while (!validID) {
            System.out.print("Enter ID of order to update: ");
            if (sc.hasNextInt()) {
                oid = sc.nextInt();
                sc.nextLine(); 
                if (conf.getSingleValue("SELECT o_id FROM tbl_order WHERE o_id = ?", oid) != 0) {
                    validID = true; 
                } else {
                    System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.nextLine();
            }
        }
        return oid;
    }

    // Method to delete an order
    public void deleteorder() {
        config conf = new config();
        int oid = 0;
        boolean validID = false;

        while (!validID) {
            System.out.print("Enter Order ID to Delete: ");
            if (sc.hasNextInt()) {
                oid = sc.nextInt();
                sc.nextLine(); 
                if (conf.getSingleValue("SELECT o_id FROM tbl_order WHERE o_id = ?", oid) != 0) {
                    validID = true; 
                } else {
                    System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.nextLine(); 
            }
        }

        String qry = "DELETE FROM tbl_order WHERE o_id = ?";
        conf.deleteRecord(qry, oid);
        System.out.println("Order with ID " + oid + " has been deleted successfully.");
    }
}