// Represents a single financial transaction
public class Transaction {

    // Transaction details
    private int serialNumber;
    private String name;
    private double amount;
    private String type; // CASH IN or CASH OUT

    // Constructor to initialize a transaction
    public Transaction(int serialNumber, String name, double amount, String type) {
        this.serialNumber = serialNumber;
        this.name = name;
        this.amount = amount;
        this.type = type;
    }

    // Getter methods
    public int getSerialNumber() {
        return serialNumber;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    // Updates serial number after transaction deletion
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    // Returns transaction details as a formatted string
    @Override
    public String toString() {
        return "  [" + serialNumber + "] " + type +
               " | Name: " + name +
               " | Amount: " + amount;
    }
}