import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

// Swing GUI front-end for the Personal Finance Manager.
// Reuses the existing FinanceManager and Transaction classes unchanged.
public class FinanceGUI extends JFrame {

    // ---- Color palette ---------------------------------------------------
    private static final Color BG        = new Color(0xF4, 0xF6, 0xFA); // app background
    private static final Color CARD      = Color.WHITE;
    private static final Color INK       = new Color(0x1F, 0x2A, 0x37); // dark text
    private static final Color MUTED     = new Color(0x6B, 0x72, 0x80); // grey text
    private static final Color GREEN     = new Color(0x16, 0xA3, 0x4A); // cash in
    private static final Color GREEN_DK  = new Color(0x10, 0x7A, 0x37);
    private static final Color RED       = new Color(0xDC, 0x2A, 0x2A); // cash out
    private static final Color RED_DK    = new Color(0xA3, 0x1E, 0x1E);
    private static final Color BLUE      = new Color(0x2D, 0x5B, 0xFF); // primary/header
    private static final Color SLATE     = new Color(0x47, 0x55, 0x69); // neutral btn
    private static final Color LINE      = new Color(0xE3, 0xE8, 0xEF); // borders/grid
    private static final Color ROW_ALT   = new Color(0xF8, 0xFA, 0xFC); // zebra row
    private static final Color SEL       = new Color(0xDB, 0xE7, 0xFF); // selection

    private final FinanceManager manager = new FinanceManager();

    // Use the manager's real list (already loaded from transactions.txt on startup)
    // so the GUI shows saved data and stays in sync with what gets saved.
    private final ArrayList<Transaction> view = manager.getTransactions();

    private final JTextField nameField   = new JTextField();
    private final JTextField amountField = new JTextField();

    private final DefaultTableModel tableModel =
            new DefaultTableModel(new String[]{"Serial", "Type", "Name", "Amount"}, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
    private final JTable table = new JTable(tableModel);

    // Summary card value labels
    private final JLabel cashInValue  = new JLabel("0.00");
    private final JLabel cashOutValue = new JLabel("0.00");
    private final JLabel balanceValue = new JLabel("0.00");

    // Filter buttons (kept so we can highlight the active one)
    private JButton btnAll, btnIn, btnOut;
    private String filter = "ALL";

    public FinanceGUI() {
        super("Personal Finance Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(820, 640);
        setMinimumSize(new Dimension(700, 560));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        setContentPane(root);

        root.add(buildTitleBar(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(16, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout(0, 16));
        top.setOpaque(false);
        top.add(buildSummaryCards(), BorderLayout.NORTH);
        top.add(buildFormCard(), BorderLayout.SOUTH);

        body.add(top, BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);

        root.add(body, BorderLayout.CENTER);

        refresh();
    }

    // ---- Title bar -------------------------------------------------------
    private JComponent buildTitleBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // horizontal blue -> violet gradient
                g2.setPaint(new GradientPaint(0, 0, new Color(0x3B, 0x5B, 0xFF),
                        getWidth(), 0, new Color(0x7A, 0x4D, 0xFF)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bar.setBorder(new EmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Personal Finance Manager");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

        JLabel sub = new JLabel("Track your income and expenses");
        sub.setForeground(new Color(255, 255, 255, 210));
        sub.setFont(sub.getFont().deriveFont(Font.PLAIN, 13f));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(title);
        text.add(Box.createVerticalStrut(2));
        text.add(sub);

        bar.add(text, BorderLayout.WEST);
        return bar;
    }

    // ---- Summary cards ---------------------------------------------------
    private JComponent buildSummaryCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.add(summaryCard("TOTAL CASH IN", cashInValue, GREEN));
        row.add(summaryCard("TOTAL CASH OUT", cashOutValue, RED));
        row.add(summaryCard("NET BALANCE", balanceValue, BLUE));
        return row;
    }

    private JComponent summaryCard(String caption, JLabel valueLabel, Color accent) {
        RoundedPanel card = new RoundedPanel(16, CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel cap = new JLabel(caption);
        cap.setForeground(MUTED);
        cap.setFont(cap.getFont().deriveFont(Font.BOLD, 11f));

        valueLabel.setForeground(accent);
        valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 26f));

        // thin accent strip on the left
        JPanel strip = new JPanel();
        strip.setBackground(accent);
        strip.setPreferredSize(new Dimension(5, 0));

        JPanel inner = new JPanel(new BorderLayout(0, 6));
        inner.setOpaque(false);
        inner.add(cap, BorderLayout.NORTH);
        inner.add(valueLabel, BorderLayout.CENTER);
        inner.setBorder(new EmptyBorder(0, 12, 0, 0));

        card.add(strip, BorderLayout.WEST);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // ---- Form card (add a transaction) -----------------------------------
    private JComponent buildFormCard() {
        RoundedPanel card = new RoundedPanel(16, CARD);
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(18, 18, 18, 18));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(0, 0, 6, 10);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Add a Transaction");
        heading.setForeground(INK);
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 15f));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 4; g.insets = new Insets(0, 0, 12, 0);
        card.add(heading, g);

        g.gridwidth = 1; g.insets = new Insets(0, 0, 6, 10);

        // Name
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        card.add(fieldLabel("Name"), g);
        g.gridx = 1; g.weightx = 1;
        styleField(nameField);
        card.add(nameField, g);

        // Amount
        g.gridx = 2; g.weightx = 0;
        card.add(fieldLabel("Amount"), g);
        g.gridx = 3; g.weightx = 1;
        styleField(amountField);
        card.add(amountField, g);

        // Buttons with clear names
        JButton cashIn  = pillButton("\u2795  Add Cash In (Income)", GREEN, GREEN_DK);
        JButton cashOut = pillButton("\u2796  Add Cash Out (Expense)", RED, RED_DK);
        cashIn.addActionListener(e -> handleAdd(true));
        cashOut.addActionListener(e -> handleAdd(false));

        // Press Enter in either field to add an income (common case)
        amountField.addActionListener(e -> handleAdd(true));

        JPanel btns = new JPanel(new GridLayout(1, 2, 12, 0));
        btns.setOpaque(false);
        btns.add(cashIn);
        btns.add(cashOut);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 4; g.weightx = 1;
        g.insets = new Insets(16, 0, 0, 0);
        card.add(btns, g);

        return card;
    }

    // ---- Table card ------------------------------------------------------
    private JComponent buildTableCard() {
        RoundedPanel card = new RoundedPanel(16, CARD);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Header row: title + filter buttons + delete
        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);

        JLabel title = new JLabel("Transactions");
        title.setForeground(INK);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));

        btnAll = filterButton("All");
        btnIn  = filterButton("Cash In");
        btnOut = filterButton("Cash Out");
        btnAll.addActionListener(e -> { filter = "ALL";      refresh(); });
        btnIn.addActionListener(e ->  { filter = "CASH IN";  refresh(); });
        btnOut.addActionListener(e -> { filter = "CASH OUT"; refresh(); });

        JButton delete = pillButton("\uD83D\uDDD1  Delete Selected", SLATE, INK);
        delete.addActionListener(e -> handleDelete());

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filters.setOpaque(false);
        filters.add(btnAll);
        filters.add(btnIn);
        filters.add(btnOut);
        filters.add(Box.createHorizontalStrut(8));
        filters.add(delete);

        head.add(title, BorderLayout.WEST);
        head.add(filters, BorderLayout.EAST);

        // Table styling
        table.setRowHeight(34);
        table.setShowVerticalLines(false);
        table.setGridColor(LINE);
        table.setSelectionBackground(SEL);
        table.setSelectionForeground(INK);
        table.setFont(table.getFont().deriveFont(13f));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setForeground(INK);

        JTableHeader th = table.getTableHeader();
        th.setReorderingAllowed(false);
        th.setResizingAllowed(false);
        th.setBackground(new Color(0xEE, 0xF1, 0xF6));
        th.setForeground(MUTED);
        th.setFont(th.getFont().deriveFont(Font.BOLD, 12f));
        th.setPreferredSize(new Dimension(0, 34));

        // Cell renderer: zebra striping + colored type/amount + alignment
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                c.setBorder(new EmptyBorder(0, 12, 0, 12));
                String type = String.valueOf(tableModel.getValueAt(row, 1)).trim();
                boolean in = type.equals("CASH IN");

                if (!sel) {
                    c.setBackground(row % 2 == 0 ? CARD : ROW_ALT);
                    if (col == 1 || col == 3) c.setForeground(in ? GREEN_DK : RED_DK);
                    else c.setForeground(INK);
                }
                c.setHorizontalAlignment(col == 0 ? SwingConstants.CENTER
                        : col == 3 ? SwingConstants.RIGHT : SwingConstants.LEFT);
                if (col == 3 && !sel) c.setFont(c.getFont().deriveFont(Font.BOLD));
                else c.setFont(t.getFont());
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++)
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(1).setMaxWidth(120);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(LINE));
        scroll.getViewport().setBackground(CARD);

        card.add(head, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);

        highlightFilter();
        return card;
    }

    // ---- Small UI helpers ------------------------------------------------
    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(MUTED);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 12f));
        return l;
    }

    private void styleField(JTextField f) {
        f.setFont(f.getFont().deriveFont(14f));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LINE, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        f.setPreferredSize(new Dimension(140, 36));
    }

    // A flat, rounded, colored button with hover feedback.
    private JButton pillButton(String text, Color base, Color hover) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = getModel().isPressed() ? hover.darker()
                        : getModel().isRollover() ? hover : base;
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorder(new EmptyBorder(10, 16, 10, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // A small toggle-style filter chip.
    private JButton filterButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 12f));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(7, 14, 7, 14));
        return b;
    }

    private void highlightFilter() {
        styleChip(btnAll, filter.equals("ALL"));
        styleChip(btnIn,  filter.equals("CASH IN"));
        styleChip(btnOut, filter.equals("CASH OUT"));
    }

    private void styleChip(JButton b, boolean active) {
        b.setContentAreaFilled(true);
        b.setOpaque(true);
        if (active) {
            b.setBackground(BLUE);
            b.setForeground(Color.WHITE);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BLUE, 1, true), new EmptyBorder(7, 14, 7, 14)));
        } else {
            b.setBackground(CARD);
            b.setForeground(SLATE);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(LINE, 1, true), new EmptyBorder(7, 14, 7, 14)));
        }
    }

    // ---- Logic (unchanged behavior) --------------------------------------
    private void handleAdd(boolean isCashIn) {
        String name = nameField.getText().trim();
        String amtText = amountField.getText().trim();

        if (name.isEmpty()) { error("Please enter a name."); nameField.requestFocus(); return; }
        double amount;
        try {
            amount = Double.parseDouble(amtText);
        } catch (NumberFormatException ex) {
            error("Amount must be a number."); amountField.requestFocus(); return;
        }
        if (amount <= 0) { error("Amount must be greater than zero."); amountField.requestFocus(); return; }

        if (isCashIn) {
            manager.addCashIn(name, amount);
        } else {
            manager.addCashOut(name, amount);
        }

        nameField.setText("");
        amountField.setText("");
        nameField.requestFocus();
        refresh();
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { error("Select a transaction to delete."); return; }
        int serial = Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 0)));

        manager.deleteTransaction(serial);
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        double totalIn = 0, totalOut = 0;

        for (Transaction t : view) {
            String type = t.getType().trim();
            if (type.equals("CASH IN")) totalIn += t.getAmount();
            else totalOut += t.getAmount();

            if (filter.equals("ALL") || type.equals(filter)) {
                tableModel.addRow(new Object[]{
                        t.getSerialNumber(), type, t.getName(),
                        String.format("%.2f", t.getAmount())
                });
            }
        }

        cashInValue.setText(String.format("%,.2f", totalIn));
        cashOutValue.setText(String.format("%,.2f", totalOut));
        double net = totalIn - totalOut;
        balanceValue.setText(String.format("%,.2f", net));
        balanceValue.setForeground(net < 0 ? RED : net > 0 ? GREEN : BLUE);

        highlightFilter();
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ---- A panel that paints a rounded, soft-shadowed card ---------------
    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color fill;
        RoundedPanel(int radius, Color fill) {
            this.radius = radius; this.fill = fill;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // subtle shadow
            g2.setColor(new Color(0, 0, 0, 12));
            g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, radius, radius);
            // card
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 4, radius, radius);
            g2.setColor(LINE);
            g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 5, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> new FinanceGUI().setVisible(true));
    }
}
