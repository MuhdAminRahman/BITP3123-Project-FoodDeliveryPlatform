import javax.swing.*;
import java.awt.*;

public class OrderPage extends JFrame {
    public OrderPage() {
        setTitle("Skybite - Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(26, 115, 232));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        JLabel orderLabel = new JLabel("Place Your Order", SwingConstants.CENTER);
        orderLabel.setForeground(Color.WHITE);
        orderLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        headerPanel.add(orderLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(Color.WHITE);

        JLabel menuLabel = new JLabel("Menu Item:");
        menuLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField menuField = new JTextField();
        menuField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(menuLabel);
        formPanel.add(menuField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField qtyField = new JTextField();
        qtyField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(qtyLabel);
        formPanel.add(qtyField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel noteLabel = new JLabel("Notes (optional):");
        noteLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextArea noteArea = new JTextArea(3, 20);
        noteArea.setFont(new Font("Poppins", Font.PLAIN, 14));
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScroll = new JScrollPane(noteArea);
        formPanel.add(noteLabel);
        formPanel.add(noteScroll);
        formPanel.add(Box.createVerticalStrut(20));

        JButton submitBtn = new JButton("Submit Order");
        submitBtn.setBackground(new Color(26, 115, 232));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        submitBtn.setFocusPainted(false);
        formPanel.add(submitBtn);

        add(formPanel, BorderLayout.CENTER);

        // Button action
        submitBtn.addActionListener(e -> {
            String menu = menuField.getText();
            String qty = qtyField.getText();
            String notes = noteArea.getText();
            if (menu.isEmpty() || qty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in menu item and quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Order submitted!\nMenu: " + menu + "\nQuantity: " + qty + (notes.isEmpty() ? "" : "\nNotes: " + notes));
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderPage().setVisible(true));
    }
}
