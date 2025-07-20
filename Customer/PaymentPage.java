import javax.swing.*;
import java.awt.*;

public class PaymentPage extends JFrame {
    public PaymentPage() {
        setTitle("Skybite - Payment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(26, 115, 232));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        JLabel paymentLabel = new JLabel("Payment", SwingConstants.CENTER);
        paymentLabel.setForeground(Color.WHITE);
        paymentLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        headerPanel.add(paymentLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(Color.WHITE);

        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField cardField = new JTextField();
        cardField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(cardLabel);
        formPanel.add(cardField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel expiryLabel = new JLabel("Expiry Date (MM/YY):");
        expiryLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField expiryField = new JTextField();
        expiryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(expiryLabel);
        formPanel.add(expiryField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel cvvLabel = new JLabel("CVV:");
        cvvLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField cvvField = new JTextField();
        cvvField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(cvvLabel);
        formPanel.add(cvvField);
        formPanel.add(Box.createVerticalStrut(20));

        JButton payBtn = new JButton("Pay Now");
        payBtn.setBackground(new Color(26, 115, 232));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        payBtn.setFocusPainted(false);
        formPanel.add(payBtn);

        add(formPanel, BorderLayout.CENTER);

        // Button action
        payBtn.addActionListener(e -> {
            String card = cardField.getText();
            String expiry = expiryField.getText();
            String cvv = cvvField.getText();
            if (card.isEmpty() || expiry.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all payment fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Payment successful!\nCard: " + card + "\nExpiry: " + expiry);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentPage().setVisible(true));
    }
}
