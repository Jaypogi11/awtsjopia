package jay11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Reports {
    
    private Scanner sc = new Scanner(System.in);
    private config conf = new config();

    public void generateReport() {
        String response;
        do {
            System.out.println("\n---------------------------------------------");
            System.out.println("               REPORT PANEL:");
            System.out.println("1. Overall Report");
            System.out.println("2. Individual Order Report");
            System.out.println("3. Individual Customer Details");
            System.out.println("4. Individual Product Info");
            System.out.println("5. Exit");
            System.out.println("\n---------------------------------------------");
            
            int choice = getChoice(1, 5);
            
            switch (choice) {
                case 1:
                    overallReport();
                    break;
                case 2:
                    individualOrderReport();
                    break;
                case 3:
                    individualCustomerDetails();
                    break;
                case 4:
                    individualProductInfo();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Unexpected error.");
            }
            
            System.out.print("Do you want to continue? (yes/no): ");
            response = sc.next();
        } while (response.equalsIgnoreCase("yes"));
    }

    private void overallReport() {
        String query = "SELECT o.o_id, c.c_name, p.p_name, o.o_date, o.o_amount, o.o_status " +
                       "FROM tbl_order o " +
                       "INNER JOIN tbl_customer c ON o.c_id = c.c_id " +
                       "INNER JOIN tbl_product p ON o.p_id = p.p_id";
        
        String[] headers = {"Order ID", "Customer Name", "Product Name", "Order Date", "Order Amount", "Order Status"};
        String[] columns = {"o_id", "c_name", "p_name", "o_date", "o_amount", "o_status"};
        
        conf.viewRecords(query, headers, columns);
    }
 
    private void individualOrderReport() {
       String display = "SELECT o.o_id, c.c_name " +
               "FROM tbl_order o " +
               "INNER JOIN tbl_customer c ON o.c_id = c.c_id";
       String[] dheaders = {"Order ID", "Customer Name"};
       String[] dcolumns = {"o_id", "c_name"};
       conf.viewRecords(display, dheaders, dcolumns);

        int orderId = getValidOrderId();
        String query = "SELECT o.o_id, c.c_name, p.p_name, o.o_date, o.o_amount, o.o_status " +
                       "FROM tbl_order o " +
                       "INNER JOIN tbl_customer c ON o.c_id = c.c_id " +
                       "INNER JOIN tbl_product p ON o.p_id = p.p_id " +
                       "WHERE o.o_id = ?";
        
        String[] headers = {"Order ID", "Customer Name", "Product Name", "Order Date", "Order Amount", "Order Status"};
        String[] columns = {"o_id", "c_name", "p_name", "o_date", "o_amount", "o_status"};
        
        conf.viewRecordsWithParams(query, headers, columns, orderId);
    }

   private void individualCustomerDetails() {
   
    String display = "SELECT c.c_id, c.c_name " +
                     "FROM tbl_customer c";
    String[] dheaders = {"Customer ID", "Customer Name"};
    String[] dcolumns = {"c_id", "c_name"};
    conf.viewRecords(display, dheaders, dcolumns);
    
    
    int customerId = getValidCustomerId();
    
  
    String query = "SELECT c.c_id, c.c_name, c.c_contact, c.c_address " +
                   "FROM tbl_customer c " +
                   "WHERE c.c_id = ?";
    
  
    int id = 0;
    String name = "";
    String contact = "";
    String address = "";

    
    try (Connection connection = conf.connectDB(); 
         PreparedStatement pstmt = connection.prepareStatement(query)) {
        
        pstmt.setInt(1, customerId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            id = rs.getInt("c_id");
            name = rs.getString("c_name");
            contact = rs.getString("c_contact");
            address = rs.getString("c_address");
        } else {
            System.out.println("Customer not found.");
            return;
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }

   
    System.out.println("\nCustomer Personal Details:");
    System.out.println("Customer ID: " + id);
    System.out.println("Customer Name: " + name);
    System.out.println("Contact: " + contact);
    System.out.println("Address: " + address);
}

private void individualProductInfo() {
    
    String display = "SELECT p.p_id, p.p_name " +
                     "FROM tbl_product p";
    String[] dheaders = {"Product ID", "Product Name"};
    String[] dcolumns = {"p_id", "p_name"};
    conf.viewRecords(display, dheaders, dcolumns);
    
    
    int productId = getValidProductId();
    
    
    String query = "SELECT p.p_id, p.p_name, p.p_price, p.p_stock " +
                   "FROM tbl_product p " +
                   "WHERE p.p_id = ?";
    
    String[] headers = {"Product ID", "Product Name", "Price", "Stock"};
    String[] columns = {"p_id", "p_name", "p_price", "p_stock"};
    
    
    conf.viewRecordsWithParams(query, headers, columns, productId);
}
    private int getValidOrderId() {
        int orderId;
        while (true) {
            System.out.print("Enter Order ID: ");
            if (sc.hasNextInt()) {
                orderId = sc.nextInt();
                if (conf.getSingleValue("SELECT o_id FROM tbl_order WHERE o_id = ?", orderId) != 0) {
                    return orderId;
                } else {
                    System.out.println("Order ID does not exist. Please try again.");
                }
            } else {
                System.out.println("Invalid input! Order ID must be a number.");
                sc.next(); 
            }
        }
    }

    private int getValidCustomerId() {
        int customerId;
        while (true) {
            System.out.print("Enter Customer ID: ");
            if (sc.hasNextInt()) {
                customerId = sc.nextInt();
                if (conf.getSingleValue("SELECT c_id FROM tbl_customer WHERE c_id = ?", customerId) != 0) {
                    return customerId;
                } else {
                    System.out.println("Customer ID does not exist. Please try again.");
                }
            } else {
                System.out.println("Invalid input! Customer ID must be a number.");
                sc.next(); 
            }
        }
    }

    private int getValidProductId() {
        int productId;
        while (true) {
            System.out.print("Enter Product ID: ");
            if (sc.hasNextInt()) {
                productId = sc.nextInt();
                if (conf.getSingleValue("SELECT p_id FROM tbl_product WHERE p_id = ?", productId) != 0) {
                    return productId;
                } else {
                    System.out.println("Product ID does not exist. Please try again.");
                }
            } else {
                System.out.println("Invalid input! Product ID must be a number.");
                sc.next(); 
            }
        }
    }

    private int getChoice(int min, int max) {
        int choice = -1;
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Enter Choice (" + min + "-" + max + "): ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice >= min && choice <= max) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice! Please select a number between " + min + " and " + max + ".");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                sc.next(); 
            }
        }
        return choice;
    }
}