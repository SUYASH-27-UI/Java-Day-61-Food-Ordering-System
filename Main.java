
import java.util.*;
import java.io.*;

class MenuItem {
    int id;
    String name;
    double price;

    MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class OrderItem {
    MenuItem item;
    int quantity;

    OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    double getTotal() {
        return item.price * quantity;
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<MenuItem> menu = new ArrayList<>();
    static ArrayList<OrderItem> cart = new ArrayList<>();

    static void loadMenu() {
        menu.add(new MenuItem(1, "Burger", 120));
        menu.add(new MenuItem(2, "Pizza", 250));
        menu.add(new MenuItem(3, "Sandwich", 100));
        menu.add(new MenuItem(4, "Pasta", 180));
        menu.add(new MenuItem(5, "Coffee", 80));
    }

    static void showMenu() {
        System.out.println("\n===== FOOD MENU =====");

        System.out.printf("%-8s %-15s %-10s%n",
                "ID", "Item", "Price");

        for (MenuItem item : menu) {
            System.out.printf("%-8d %-15s Rs. %.2f%n",
                    item.id, item.name, item.price);
        }
    }

    static void addToCart() {
        showMenu();

        System.out.print("\nEnter item ID: ");
        int id = readNumber();

        MenuItem selected = null;

        for (MenuItem item : menu) {
            if (item.id == id) {
                selected = item;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Invalid item ID!");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = readNumber();

        if (quantity <= 0) {
            System.out.println("Invalid quantity!");
            return;
        }

        cart.add(new OrderItem(selected, quantity));

        System.out.println("Item added to cart!");
    }

    static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        System.out.println("\n===== YOUR CART =====");

        for (OrderItem order : cart) {
            System.out.printf(
                "%s x %d = Rs. %.2f%n",
                order.item.name,
                order.quantity,
                order.getTotal()
            );
        }
    }

    static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        double subtotal = 0;

        System.out.println("\n===== FINAL BILL =====");

        for (OrderItem order : cart) {
            double total = order.getTotal();
            subtotal += total;

            System.out.printf(
                "%s x %d = Rs. %.2f%n",
                order.item.name,
                order.quantity,
                total
            );
        }

        double tax = subtotal * 0.05;
        double grandTotal = subtotal + tax;

        System.out.println("---------------------------");
        System.out.printf("Subtotal: Rs. %.2f%n", subtotal);
        System.out.printf("GST (5%%): Rs. %.2f%n", tax);
        System.out.printf("Grand Total: Rs. %.2f%n", grandTotal);

        saveOrder(subtotal, tax, grandTotal);

        cart.clear();

        System.out.println("\nOrder completed successfully!");
    }

    static void saveOrder(double subtotal,
                          double tax,
                          double grandTotal) {

        try (PrintWriter writer =
                 new PrintWriter(
                     new FileWriter("orders.txt", true))) {

            writer.println("\n===== NEW ORDER =====");

            for (OrderItem order : cart) {
                writer.printf(
                    "%s x %d = Rs. %.2f%n",
                    order.item.name,
                    order.quantity,
                    order.getTotal()
                );
            }

            writer.printf("Subtotal: Rs. %.2f%n", subtotal);
            writer.printf("GST: Rs. %.2f%n", tax);
            writer.printf("Grand Total: Rs. %.2f%n", grandTotal);

        } catch (IOException e) {
            System.out.println("Unable to save order!");
        }
    }

    static int readNumber() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {

        loadMenu();

        int choice;

        do {
            System.out.println("\n===== FOOD ORDERING SYSTEM =====");
            System.out.println("1. View Food Menu");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");

            choice = readNumber();

            switch (choice) {
                case 1:
                    showMenu();
                    break;

                case 2:
                    addToCart();
                    break;

                case 3:
                    viewCart();
                    break;

                case 4:
                    checkout();
                    break;

                case 5:
                    System.out.println("Thank you for ordering!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}
