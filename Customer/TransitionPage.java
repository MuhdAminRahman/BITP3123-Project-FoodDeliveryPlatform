import javax.swing.*;
import java.awt.*;

public class TransitionPage extends JFrame {
    public TransitionPage() {
        setTitle("Skybite - Transition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(26, 115, 232));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        JLabel titleLabel = new JLabel("Skybite", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 22));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center message
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setLayout(new GridBagLayout());
        JLabel transitionLabel = new JLabel("Transitioning...", SwingConstants.CENTER);
        transitionLabel.setFont(new Font("Poppins", Font.BOLD, 20));
        centerPanel.add(transitionLabel);
        add(centerPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransitionPage().setVisible(true));
    }
}
