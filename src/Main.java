import java.util.Scanner;

// Main class of the Personal Finance Manager application
public class Main {

    public static void main(String[] args) {

        // Used to read user input
        Scanner scanner = new Scanner(System.in);

        // Handles all transaction operations
        FinanceManager manager = new FinanceManager();

        int choice = 0;

        System.out.println("\n  ╔══════════════════════════════════╗");
        System.out.println("  ║   PERSONAL FINANCE MANAGER       ║");
        System.out.println("  ╚══════════════════════════════════╝");

        // Display menu until the user chooses Exit
        while (choice != 7) {

            System.out.println("\n  ──────────────────────────────────");
            System.out.println("  1. Cash In");
            System.out.println("  2. Cash Out");
            System.out.println("  3. Delete Transaction");
            System.out.println("  4. All Cash In");
            System.out.println("  5. All Cash Out");
            System.out.println("  6. Show All Transactions");
            System.out.println("  7. Exit");
            System.out.println("  ──────────────────────────────────");
            System.out.print("  Enter your choice (1-7): ");

            // Validate menu input
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear input buffer
            } else {
                System.out.println("\n  ERROR! Please type a number between 1 and 7.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {

                // Add a Cash In transaction
                case 1:
                    System.out.println("\n  --- CASH IN ---");
                    System.out.print("  Enter Name   : ");
                    String cashInName = scanner.nextLine();

                    System.out.print("  Enter Amount : ");
                    if (scanner.hasNextDouble()) {
                        double cashInAmount = scanner.nextDouble();
                        scanner.nextLine();
                        manager.addCashIn(cashInName, cashInAmount);
                    } else {
                        System.out.println("\n  ERROR! Amount must be a number. Try again.");
                        scanner.nextLine();
                    }
                    break;

                // Add a Cash Out transaction
                case 2:
                    System.out.println("\n  --- CASH OUT ---");
                    System.out.print("  Enter Name   : ");
                    String cashOutName = scanner.nextLine();

                    System.out.print("  Enter Amount : ");
                    if (scanner.hasNextDouble()) {
                        double cashOutAmount = scanner.nextDouble();
                        scanner.nextLine();
                        manager.addCashOut(cashOutName, cashOutAmount);
                    } else {
                        System.out.println("\n  ERROR! Amount must be a number. Try again.");
                        scanner.nextLine();
                    }
                    break;

                // Delete a transaction by serial number
                case 3:
                    System.out.println("\n  --- DELETE TRANSACTION ---");
                    System.out.print("  Enter the Serial Number to delete: ");

                    if (scanner.hasNextInt()) {
                        int serialToDelete = scanner.nextInt();
                        scanner.nextLine();
                        manager.deleteTransaction(serialToDelete);
                    } else {
                        System.out.println("\n  ERROR! Serial number must be a whole number.");
                        scanner.nextLine();
                    }
                    break;

                case 4:
                    manager.showAllCashIn();
                    break;

                case 5:
                    manager.showAllCashOut();
                    break;

                case 6:
                    manager.showAllTransactions();
                    break;

                case 7:
                    System.out.println("\n  Goodbye! Thanks for using Personal Finance Manager.");
                    break;

                default:
                    System.out.println("\n  ERROR! Please choose a number between 1 and 7.");
            }
        }

        // Release scanner resources
        scanner.close();
    }
}