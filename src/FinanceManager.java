import java.util.ArrayList;
import java.io.*;

// Manages all transaction operations
public class FinanceManager {

    // Stores all transactions
    private ArrayList<Transaction> transactions;

    // Tracks the next available serial number
    private int nextSerial;

    // File used to save data permanently
    private final String FILE_NAME = "transactions.txt";

    // Constructor initializes transaction list and serial counter
    public FinanceManager() {
        transactions = new ArrayList<>();
        nextSerial = 1;
        loadFromFile(); // Load saved data when program starts
    }

    // Returns the live list of transactions (used by the GUI to display data)
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // Adds a new Cash In transaction
    public void addCashIn(String name, double amount) {
        Transaction t = new Transaction(nextSerial, name, amount, "CASH IN ");
        transactions.add(t);
        nextSerial++;

        System.out.println("\n  SUCCESS! Cash In added. Serial Number: #" + t.getSerialNumber());
        saveToFile();
    }

    // Adds a new Cash Out transaction
    public void addCashOut(String name, double amount) {
        Transaction t = new Transaction(nextSerial, name, amount, "CASH OUT");
        transactions.add(t);
        nextSerial++;

        System.out.println("\n  SUCCESS! Cash Out added. Serial Number: #" + t.getSerialNumber());
        saveToFile();
    }

    // Deletes a transaction by serial number
    public void deleteTransaction(int serial) {

        boolean found = false;

        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getSerialNumber() == serial) {
                transactions.remove(i);
                found = true;

                System.out.println("\n  SUCCESS! Transaction #" + serial + " has been deleted.");
                break;
            }
        }

        if (!found) {
            System.out.println("\n  ERROR! No transaction found with Serial #" + serial);
            return;
        }

        // Reassign serial numbers after deletion
        for (int i = 0; i < transactions.size(); i++) {
            transactions.get(i).setSerialNumber(i + 1);
        }

        nextSerial = transactions.size() + 1;
        saveToFile();
    }

    // Displays all Cash In transactions
    public void showAllCashIn() {
        System.out.println("\n  ========== ALL CASH IN ==========");

        boolean found = false;

        for (Transaction t : transactions) {
            if (t.getType().trim().equals("CASH IN")) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("  (No Cash In transactions yet)");
        }
    }

    // Displays all Cash Out transactions
    public void showAllCashOut() {
        System.out.println("\n  ========== ALL CASH OUT ==========");

        boolean found = false;

        for (Transaction t : transactions) {
            if (t.getType().trim().equals("CASH OUT")) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("  (No Cash Out transactions yet)");
        }
    }

    // Displays all transactions and financial summary
    public void showAllTransactions() {

        System.out.println("\n  ======================================");
        System.out.println("         ALL TRANSACTIONS               ");
        System.out.println("  ======================================");

        if (transactions.isEmpty()) {
            System.out.println("  (No transactions yet)");
        } else {
            for (Transaction t : transactions) {
                System.out.println(t);
            }
        }

        double totalCashIn = 0;
        double totalCashOut = 0;

        // Calculate total income and expense
        for (Transaction t : transactions) {
            if (t.getType().trim().equals("CASH IN")) {
                totalCashIn += t.getAmount();
            } else {
                totalCashOut += t.getAmount();
            }
        }

        // Calculate current balance
        double netBalance = totalCashIn - totalCashOut;

        System.out.println("  --------------------------------------");
        System.out.println("  Total Cash In  : " + totalCashIn);
        System.out.println("  Total Cash Out : " + totalCashOut);
        System.out.println("  Net Balance    : " + netBalance);
        System.out.println("  ======================================");
    }

    // Saves all transactions to a file
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Transaction t : transactions) {
                // Format: serial|name|amount|type
                writer.println(t.getSerialNumber() + "|" + t.getName() + "|"
                             + t.getAmount() + "|" + t.getType());
            }
        } catch (IOException e) {
            System.out.println("\n  ERROR! Could not save data.");
        }
    }

    // Loads transactions from the file (if it exists)
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // Nothing saved yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");
                if (parts.length < 4) {
                    continue; // Skip malformed lines
                }

                int serial = Integer.parseInt(parts[0]);
                String name = parts[1];
                double amount = Double.parseDouble(parts[2]);
                String type = parts[3];

                transactions.add(new Transaction(serial, name, amount, type));
                nextSerial = serial + 1;
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("\n  ERROR! Could not load saved data.");
        }
    }
}
