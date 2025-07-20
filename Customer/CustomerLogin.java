import javax.swing.*;
import java.awt.*;

public class CustomerLogin extends JFrame {
    public CustomerLogin() {
        setTitle("Skybite - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(26, 115, 232));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(new ImageIcon(new ImageIcon("https://storage.googleapis.com/workspace-0f70711f-8b4e-4d94-86f1-2a93ccde5887/image/ee335f03-466c-4cb3-8672-eb0d0c8b566d.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Sign in to your Skybite account");
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(Color.WHITE);

        JLabel emailLabel = new JLabel("Email Address");
        JTextField emailField = new JTextField();
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(15));

        JLabel passwordLabel = new JLabel("Password");
        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(15));

        JPanel rememberPanel = new JPanel(new BorderLayout());
        JCheckBox rememberCheck = new JCheckBox("Remember me");
        JLabel forgotLabel = new JLabel("<html><a href='#'>Forgot password?</a></html>");
        rememberPanel.add(rememberCheck, BorderLayout.WEST);
        rememberPanel.add(forgotLabel, BorderLayout.EAST);
        rememberPanel.setOpaque(false);
        formPanel.add(rememberPanel);
        formPanel.add(Box.createVerticalStrut(20));

        JButton loginButton = new JButton("Sign In");
        loginButton.setBackground(new Color(26, 115, 232));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Poppins", Font.BOLD, 16));
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel divider = new JLabel("or", SwingConstants.CENTER);
        divider.setForeground(new Color(95, 99, 104));
        formPanel.add(divider);
        formPanel.add(Box.createVerticalStrut(20));

        JPanel socialPanel = new JPanel();
        socialPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JButton googleBtn = new JButton("G");
        googleBtn.setBackground(new Color(219, 68, 55));
        googleBtn.setForeground(Color.WHITE);
        JButton githubBtn = new JButton("G");
        githubBtn.setBackground(new Color(51, 51, 51));
        githubBtn.setForeground(Color.WHITE);
        JButton linkedinBtn = new JButton("L");
        linkedinBtn.setBackground(new Color(0, 119, 181));
        linkedinBtn.setForeground(Color.WHITE);
        socialPanel.add(googleBtn);
        socialPanel.add(githubBtn);
        socialPanel.add(linkedinBtn);
        socialPanel.setOpaque(false);
        formPanel.add(socialPanel);
        formPanel.add(Box.createVerticalStrut(20));

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JLabel signupLabel = new JLabel("<html><a href='signup.html'>Create account</a></html>");
        JLabel termsLabel = new JLabel("<html><a href='#'>Terms of Service</a></html>");
        JLabel privacyLabel = new JLabel("<html><a href='#'>Privacy Policy</a></html>");
        footerPanel.add(signupLabel);
        footerPanel.add(termsLabel);
        footerPanel.add(privacyLabel);
        footerPanel.setOpaque(false);
        formPanel.add(footerPanel);

        add(formPanel, BorderLayout.CENTER);

        // Login button action
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Simulate successful login
            JOptionPane.showMessageDialog(this, "Login successful! Redirecting...");
            // Redirect logic here
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerLogin().setVisible(true);
        });
    }
}
