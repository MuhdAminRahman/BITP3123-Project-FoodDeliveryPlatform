import javax.swing.*;
import java.awt.*;

public class SignupPage extends JFrame {
    public SignupPage() {
        setTitle("Skybite - Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(26, 115, 232));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        JLabel signupLabel = new JLabel("Create Your Account", SwingConstants.CENTER);
        signupLabel.setForeground(Color.WHITE);
        signupLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        headerPanel.add(signupLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        formPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel emailLabel = new JLabel("Email Address:");
        emailLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Poppins", Font.PLAIN, 15));
        JPasswordField confirmField = new JPasswordField();
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        formPanel.add(confirmLabel);
        formPanel.add(confirmField);
        formPanel.add(Box.createVerticalStrut(20));

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(26, 115, 232));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Poppins", Font.BOLD, 16));
        signupBtn.setFocusPainted(false);
        formPanel.add(signupBtn);

        add(formPanel, BorderLayout.CENTER);

        // Button action
        signupBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String confirm = new String(confirmField.getPassword());
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(this, "Signup successful!\nWelcome, " + name + "!");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SignupPage().setVisible(true));
    }
}
