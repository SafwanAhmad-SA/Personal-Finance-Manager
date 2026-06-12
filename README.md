# 💰 Personal Finance Manager

A Java-based **Personal Finance Management System** that helps users track their income and expenses. Users can record *Cash In* and *Cash Out* transactions, view summaries, calculate their net balance, and have all data saved automatically to a file so nothing is lost when the program closes. The project includes both a **console (text-based)** interface and a **graphical user interface (GUI)**.

---

## ✨ Features

- ➕ **Add Cash In** — record income transactions
- ➖ **Add Cash Out** — record expense transactions
- 🗑️ **Delete Transactions** — remove a transaction by its serial number (serials auto-reorder afterwards)
- 📋 **View Transactions** — list all Cash In, all Cash Out, or every transaction
- 📊 **Financial Summary** — see total income, total expense, and current net balance
- 💾 **Automatic Saving** — all data is stored in `transactions.txt` and reloaded on startup
- 🖥️ **Two Interfaces** — a console version and a GUI version

---

## 🛠️ Built With

- **Language:** Java
- **GUI:** Java Swing
- **Storage:** Plain text file (`transactions.txt`)

---

## 📁 Project Structure

```
Personal-Finance-Manager/
├── README.md
├── src/
│   ├── Main.java             # Console application entry point + menu
│   ├── FinanceManager.java   # Core logic: add, delete, save, load, summaries
│   ├── Transaction.java      # Transaction data model
│   └── FinanceGUI.java       # Graphical user interface (Swing)
├── data/
│   └── transactions.txt      # Saved transaction data
└── docs/
    ├── Personal_Finance_Management_System_Report.docx
    └── Personal_Finance_Management_System_Presentation_5_.pptx
```

> **Note:** If you keep all files in the root folder instead of using `src/`, `data/`, and `docs/`, the run commands below stay the same — just make sure the `.java` files are in the same folder when you compile.

---

## 🚀 How to Run

### Requirements
- Java Development Kit (JDK) installed (JDK 8 or higher)

### Console Version

1. Open a terminal in the folder containing the `.java` files.
2. Compile the source files:
   ```bash
   javac Main.java FinanceManager.java Transaction.java
   ```
3. Run the program:
   ```bash
   java Main
   ```
4. Use the on-screen menu (options 1–7) to manage your transactions.

### GUI Version

1. Compile the GUI along with the supporting files:
   ```bash
   javac FinanceGUI.java FinanceManager.java Transaction.java
   ```
2. Run it:
   ```bash
   java FinanceGUI
   ```

---

## 📖 How It Works

Each transaction stores a **serial number**, a **name** (description), an **amount**, and a **type** (`CASH IN` or `CASH OUT`). Transactions are kept in a list while the program runs and written to `transactions.txt` in the format:

```
serial|name|amount|type
```

When the program starts, it reads this file back so your previous data is restored automatically.

---

## 📸 Preview

The graphical interface showing the dashboard, transaction entry, and summary cards.

<img width="1918" height="1011" alt="image" src="https://github.com/user-attachments/assets/625c966c-6911-490e-a2b0-92dc9cb67846" />



## 📄 Documentation

- 📘 **Project Report** — see the `docs/` folder
- 📊 **Presentation Slides** — see the `docs/` folder

---

## 👤 Author

*Safwan Ahmad*

---

## 📝 License

This project was created for educational purposes.
