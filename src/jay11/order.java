package jay11;

import java.util.InputMismatchException;
import java.util.Scanner;

public class order {

    private Scanner sc = new Scanner(System.in);

    public void otransaction() {
        String response;
        do {
            displayMenu();
            int action = getUserChoice(1, 5);

            switch (action) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    viewOrder();
                    break;
                case 3:
                    updateOrder();
                    break;
                case 4:
                    deleteOrder();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid selection. Please try again.");
            }

            response = getContinueResponse();

        } while (response.equalsIgnoreCase("yes"));
    }

    private void displayMenu() {
        System.out.println("\n---------------------------");
        System.out.println("Order PANEL:");
        System.out.println("1. Add Order");
        System.out.println("2. View Order");
        System.out.println("3. Update Order");
        System.out.println("4. Delete Order");
        System.out.println("5. Exit");
    }

    private int getUserChoice(int min, int max) {
        int choice = 0;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("Enter Choice (" + min + "-" + max + "): ");
            try {
                choice = sc.nextInt();
                if (choice >= min && choice <= max) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice! Please select a number between " + min + " and " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                sc.nextLine(); 
            }
        }
        return choice;
    }

    private String getContinueResponse() {
        String response = null;
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
        return response;
    }

    public void addOrder() {
        config conf = new config();

        
        System.out.println("Available Customers:");
        conf.viewRecords("SELECT * FROM tbl_customer", new String[]{"Customer ID", "Customer Name"}, new String[]{"c_id", "c_name"});

        
        int cid = getValidCustomerId(conf);

        
        System.out.println("Available Products:");
        conf.viewRecords("SELECT * FROM tbl_product", new String[]{"Product ID", "Product Name"}, new String[]{"p_id", "p_name"});

        
        int pid = getValidProductId(conf);

        
        String odate = getValidOrderDate();

        
        Double oamount = getValidOrderAmount();

       
        String ostatus = getValidOrderStatus();

       
        String sql = "INSERT INTO tbl_order (c_id, p_id, o_date, o_amount, o_status) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, cid, pid, odate, oamount, ostatus);

        System.out.println("Order successfully added.");
    }

    private int getValidCustomerId(config conf) {
        int cid;
        while (true) {
            System.out.print("Enter the ID of the Customer: ");
            if (sc.hasNextInt()) {
                cid = sc.nextInt();
                String bsql = "SELECT c_id FROM tbl_customer WHERE c_id = ?";
                if (conf.getSingleValue(bsql, cid) != 0) {
                    sc.nextLine(); 
                    return cid;
                }
                System.out.println("Customer does not exist. Please select a valid Customer ID.");
            } else {
                System.out.println("Invalid input! Customer ID must be a number.");
                sc.next();  
            }
        }
    }

    private int getValidProductId(config conf) {
        int pid;
        while (true) {
            System.out.print("Enter the ID of the Product: ");
            if (sc.hasNextInt()) {
                pid = sc.nextInt();
                String psql = "SELECT p_id FROM tbl_product WHERE p_id = ?";
                if (conf.getSingleValue(psql, pid) != 0) {
                    sc.nextLine();  
                    return pid;
                }
                System.out.println("Product does not exist. Please select a valid Product ID.");
            } else {
                System.out.println("Invalid input! Product ID must be a number.");
                sc.next(); 
            }
        }
    }

    private String getValidOrderDate() {
        String odate;
        while (true) {
            System.out.print("Enter Order Date (YYYY-MM-DD): ");
            odate = sc.nextLine().trim();
            if (odate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return odate; 
            } else {
                System.out.println("Invalid date format. Please enter in the format YYYY-MM-DD.");
            }
        }
    }

    private Double getValidOrderAmount() {
        Double oamount;
        while (true) {
            System.out.print("Enter Order Amount: ");
            if (sc.hasNextDouble()) {
                oamount = sc.nextDouble();
                if (oamount > 0) {
                    sc.nextLine();  
                    return oamount;
                } else {
                    System.out.println("Amount must be a positive number.");
                }
            } else {
                System.out.println("Invalid input! Order Amount must be a number.");
                sc.next(); 
            }
        }
    }

    private String getValidOrderStatus() {
        String ostatus;
        while (true) {
            System.out.print("Enter Order Status (e.g., Pending, Completed, Canceled): ");
            ostatus = sc.nextLine().trim();
            if (ostatus.equalsIgnoreCase("Pending") || ostatus.equalsIgnoreCase("Completed") || ostatus.equalsIgnoreCase("Canceled")) {
                return ostatus; 
            } else {
                System.out.println("Invalid status. Please enter one of the following: Pending, Completed, Canceled.");
            }
        }
    }

    public static void viewOrder() {
        config conf = new config();
        String query = "SELECT * FROM tbl_order " +
                       "JOIN tbl_customer ON tbl_order.c_id = tbl_customer.c_id " +
                       "JOIN tbl_product ON tbl_order.p_id = tbl_product.p_id";
        String[] headers = {"Order ID", "Customer Name", "Product Name", "Order Date", "Order Amount", "Order Status"};
        String[] columns = {"o_id", "c_name", "p_name", "o_date", "o_amount", "o_status"};

        conf.viewRecords(query, headers, columns);
    }

    public void updateOrder() {
        config conf = new config();
        int oid = getValidOrderId(conf);
        int cid = getValidCustomerId(conf);
        int pid = getValidProductId(conf);
        String odate = getValidOrderDate();
        Double oamount = getValidOrderAmount();
        String ostatus = getValidOrderStatus();

        
        String sql = "UPDATE tbl_order SET c_id = ?, p_id = ?, o_date = ?, o_amount = ?, o_status = ? WHERE o_id = ?";
        conf.updateRecord(sql, cid, pid, odate, oamount, ostatus, oid);

        System.out.println("Order updated successfully.");
    }

    private int getValidOrderId(config conf) {
        int oid;
        while (true) {
            System.out.print("Enter ID of order to update: ");
            if (sc.hasNextInt()) {
                oid = sc.nextInt();
                sc.nextLine();
                if (conf.getSingleValue("SELECT o_id FROM tbl_order WHERE o_id = ?", oid) != 0) {
                    return oid;  
                } else {
                    System.out.println("Selected ID doesn't exist! Please enter a valid ID.");
                }
            } else {
                System.out.println("Invalid input! ID must be a number.");
                sc.nextLine();  
            }
        }
    }

    public void deleteOrder() {
        config conf = new config();
        int oid;
        while (true) {
            System.out.print("Enter Order ID to Delete: ");
            if (sc.hasNextInt()) {
                oid = sc.nextInt();
                sc.nextLine();
                if (conf.getSingleValue("SELECT o_id FROM tbl_order WHERE o_id = ?", oid) != 0) {
                    break;  
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