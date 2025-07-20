package com.fooddeliveryplatform.customerswing.ui;

import com.fooddeliveryplatform.customerswing.api.ApiClient;
import com.fooddeliveryplatform.customerswing.util.ThemeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Application Frame - Customer Dashboard
 * Features sidebar navigation and content panels
 */
public class MainFrame extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
    
    private final ApiClient apiClient;
    private final ApiClient.LoginResponse userInfo;
    
    // UI Components
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout contentCardLayout;
    private JLabel userNameLabel;
    private JLabel pageTitle;
    
    // Navigation buttons
    private JButton menuButton;
    private JButton ordersButton;
    private JButton profileButton;
    private JButton logoutButton;
    
    // Content panels
    private MenuPanel menuPanel;
    private OrderHistoryPanel orderHistoryPanel;
    private ProfilePanel profilePanel;
    
    // Shopping cart
    private List<CartItem> shoppingCart;
    private JButton cartButton;
    private JLabel cartCountLabel;
    
    public MainFrame(ApiClient apiClient, ApiClient.LoginResponse userInfo) {
        this.apiClient = apiClient;
        this.userInfo = userInfo;
        this.shoppingCart = new ArrayList<>();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        
        // Load initial content
        showMenuPanel();
    }
    
    private void initializeComponents() {
        // Content layout
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(ThemeManager.CONTENT_BG);
        
        // Page title
        pageTitle = new JLabel("Menu");
        ThemeManager.styleTitleLabel(pageTitle);
        
        // User name label
        userNameLabel = new JLabel("Welcome, " + userInfo.getFullName());
        ThemeManager.styleRegularLabel(userNameLabel);
        userNameLabel.setForeground(ThemeManager.TEXT_WHITE);
        
        // Navigation buttons
        menuButton = createSidebarButton("🍽️ Menu", true);
        ordersButton = createSidebarButton("📋 My Orders", false);
        profileButton = createSidebarButton("👤 Profile", false);
        logoutButton = createSidebarButton("🚪 Logout", false);
        ThemeManager.styleDangerButton(logoutButton);
        
        // Cart button
        cartButton = new JButton("🛒 Cart");
        ThemeManager.styleSuccessButton(cartButton);
        cartCountLabel = new JLabel("0");
        cartCountLabel.setFont(ThemeManager.FONT_SMALL);
        cartCountLabel.setForeground(ThemeManager.TEXT_WHITE);
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBackground(ThemeManager.ERROR_RED);
        cartCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cartCountLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        cartCountLabel.setVisible(false);
        
        // Content panels
        menuPanel = new MenuPanel(apiClient, this);
        orderHistoryPanel = new OrderHistoryPanel(apiClient);
        profilePanel = new ProfilePanel(apiClient, userInfo);
    }
    
    private JButton createSidebarButton(String text, boolean selected) {
        JButton button = new JButton(text);
        button.setFont(ThemeManager.FONT_MEDIUM);
        button.setForeground(selected ? ThemeManager.TEXT_WHITE : new Color(200, 200, 200));
        button.setBackground(selected ? new Color(59, 130, 246, 50) : ThemeManager.SIDEBAR_DARK);
        button.setBorder(ThemeManager.createSidebarItemBorder());
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!button.getText().equals(getSelectedButtonText())) {
                    button.setBackground(new Color(59, 130, 246, 30));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!button.getText().equals(getSelectedButtonText())) {
                    button.setBackground(ThemeManager.SIDEBAR_DARK);
                }
            }
        });
        
        return button;
    }
    
    private String getSelectedButtonText() {
        if (menuButton.getBackground().equals(new Color(59, 130, 246, 50))) return menuButton.getText();
        if (ordersButton.getBackground().equals(new Color(59, 130, 246, 50))) return ordersButton.getText();
        if (profileButton.getBackground().equals(new Color(59, 130, 246, 50))) return profileButton.getText();
        return "";
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Sidebar
        setupSidebar();
        
        // Main content area
        setupMainContent();
        
        add(sidebarPanel, BorderLayout.WEST);
        add(setupMainContent(), BorderLayout.CENTER);
    }
    
    private void setupSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(ThemeManager.SIDEBAR_DARK);
        sidebarPanel.setPreferredSize(new Dimension(ThemeManager.SIDEBAR_WIDTH, 0));
        
        // Header section
        JPanel headerSection = new JPanel();
        headerSection.setLayout(new BoxLayout(headerSection, BoxLayout.Y_AXIS));
        headerSection.setBackground(ThemeManager.SIDEBAR_DARK);
        headerSection.setBorder(ThemeManager.createPaddedBorder(ThemeManager.PADDING_LARGE));
        
        JLabel brandLabel = new JLabel("FoodyMoody");
        brandLabel.setFont(new Font("Inter", Font.BOLD, 24));
        brandLabel.setForeground(ThemeManager.TEXT_WHITE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel customerLabel = new JLabel("Customer Portal");
        customerLabel.setFont(ThemeManager.FONT_REGULAR);
        customerLabel.setForeground(new Color(200, 200, 200));
        customerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerSection.add(brandLabel);
        headerSection.add(Box.createVerticalStrut(5));
        headerSection.add(customerLabel);
        headerSection.add(Box.createVerticalStrut(10));
        headerSection.add(userNameLabel);
        
        // Navigation section
        JPanel navSection = new JPanel();
        navSection.setLayout(new BoxLayout(navSection, BoxLayout.Y_AXIS));
        navSection.setBackground(ThemeManager.SIDEBAR_DARK);
        navSection.setBorder(ThemeManager.createPaddedBorder(ThemeManager.PADDING_MEDIUM));
        
        // Set button sizes
        Dimension buttonSize = new Dimension(ThemeManager.SIDEBAR_WIDTH - 32, 45);
        menuButton.setMaximumSize(buttonSize);
        menuButton.setPreferredSize(buttonSize);
        ordersButton.setMaximumSize(buttonSize);
        ordersButton.setPreferredSize(buttonSize);
        profileButton.setMaximumSize(buttonSize);
        profileButton.setPreferredSize(buttonSize);
        
        navSection.add(menuButton);
        navSection.add(Box.createVerticalStrut(5));
        navSection.add(ordersButton);
        navSection.add(Box.createVerticalStrut(5));
        navSection.add(profileButton);
        
        // Cart section
        JPanel cartSection = new JPanel(new BorderLayout());
        cartSection.setBackground(ThemeManager.SIDEBAR_DARK);
        cartSection.setBorder(ThemeManager.createPaddedBorder(ThemeManager.PADDING_MEDIUM));
        
        JPanel cartButtonPanel = new JPanel(new BorderLayout());
        cartButtonPanel.setBackground(ThemeManager.SIDEBAR_DARK);
        cartButton.setPreferredSize(new Dimension(ThemeManager.SIDEBAR_WIDTH - 64, 40));
        cartButtonPanel.add(cartButton, BorderLayout.CENTER);
        cartButtonPanel.add(cartCountLabel, BorderLayout.EAST);
        
        cartSection.add(cartButtonPanel, BorderLayout.CENTER);
        
        // Footer section
        JPanel footerSection = new JPanel();
        footerSection.setLayout(new BoxLayout(footerSection, BoxLayout.Y_AXIS));
        footerSection.setBackground(ThemeManager.SIDEBAR_DARK);
        footerSection.setBorder(ThemeManager.createPaddedBorder(ThemeManager.PADDING_MEDIUM));
        
        logoutButton.setMaximumSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);
        footerSection.add(logoutButton);
        
        // Assemble sidebar
        sidebarPanel.add(headerSection);
        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(navSection);
        sidebarPanel.add(Box.createVerticalGlue());
        sidebarPanel.add(cartSection);
        sidebarPanel.add(Box.createVerticalStrut(20));
        sidebarPanel.add(footerSection);
    }
    
    private JPanel setupMainContent() {
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(ThemeManager.CONTENT_BG);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ThemeManager.CARD_WHITE);
        headerPanel.setBorder(ThemeManager.createPaddedBorder(ThemeManager.PADDING_LARGE));
        headerPanel.setPreferredSize(new Dimension(0, ThemeManager.HEADER_HEIGHT));
        
        headerPanel.add(pageTitle, BorderLayout.WEST);
        
        // Add content panels to card layout
        contentPanel.add(menuPanel, "MENU");
        contentPanel.add(orderHistoryPanel, "ORDERS");
        contentPanel.add(profilePanel, "PROFILE");
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);
        
        return mainContentPanel;
    }
    
    private void setupEventHandlers() {
        // Navigation buttons
        menuButton.addActionListener(e -> showMenuPanel());
        ordersButton.addActionListener(e -> showOrdersPanel());
        profileButton.addActionListener(e -> showProfilePanel());
        
        // Cart button
        cartButton.addActionListener(e -> showCartDialog());
        
        // Logout button
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });
    }
    
    private void setupFrame() {
        setTitle("FoodyMoody - Customer Portal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
    }
    
    private void showMenuPanel() {
        updateNavigation(menuButton, "Menu");
        contentCardLayout.show(contentPanel, "MENU");
        menuPanel.refreshMenu();
    }
    
    private void showOrdersPanel() {
        updateNavigation(ordersButton, "My Orders");
        contentCardLayout.show(contentPanel, "ORDERS");
        orderHistoryPanel.refreshOrders();
    }
    
    private void showProfilePanel() {
        updateNavigation(profileButton, "Profile");
        contentCardLayout.show(contentPanel, "PROFILE");
    }
    
    private void updateNavigation(JButton selectedButton, String title) {
        // Reset all buttons
        resetButton(menuButton);
        resetButton(ordersButton);
        resetButton(profileButton);
        
        // Highlight selected button
        selectedButton.setBackground(new Color(59, 130, 246, 50));
        selectedButton.setForeground(ThemeManager.TEXT_WHITE);
        
        // Update page title
        pageTitle.setText(title);
    }
    
    private void resetButton(JButton button) {
        button.setBackground(ThemeManager.SIDEBAR_DARK);
        button.setForeground(new Color(200, 200, 200));
    }
    
    private void showCartDialog() {
        if (shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Your cart is empty. Add some items from the menu first!", 
                "Empty Cart", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        OrderFrame orderFrame = new OrderFrame(this, apiClient, shoppingCart);
        orderFrame.setVisible(true);
    }
    
    private void performLogout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            // Clear authentication token
            apiClient.setAuthToken(null);
            
            // Show login frame
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
                this.dispose();
            });
        }
    }
    
    // Cart management methods
    public void addToCart(ApiClient.FoodItem foodItem, int quantity, String specialInstructions) {
        // Check if item already exists in cart
        for (CartItem cartItem : shoppingCart) {
            if (cartItem.getFoodItem().getFoodId() == foodItem.getFoodId()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                updateCartDisplay();
                return;
            }
        }
        
        // Add new item to cart
        CartItem cartItem = new CartItem(foodItem, quantity, specialInstructions);
        shoppingCart.add(cartItem);
        updateCartDisplay();
        
        // Show confirmation
        JOptionPane.showMessageDialog(this,
            foodItem.getName() + " added to cart!",
            "Added to Cart",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void removeFromCart(CartItem cartItem) {
        shoppingCart.remove(cartItem);
        updateCartDisplay();
    }
    
    public void clearCart() {
        shoppingCart.clear();
        updateCartDisplay();
    }
    
    public List<CartItem> getShoppingCart() {
        return new ArrayList<>(shoppingCart);
    }
    
    private void updateCartDisplay() {
        int itemCount = shoppingCart.stream().mapToInt(CartItem::getQuantity).sum();
        
        if (itemCount > 0) {
            cartCountLabel.setText(String.valueOf(itemCount));
            cartCountLabel.setVisible(true);
        } else {
            cartCountLabel.setVisible(false);
        }
        
        cartButton.setText("🛒 Cart (" + itemCount + ")");
    }
    
    // Cart item class
    public static class CartItem {
        private ApiClient.FoodItem foodItem;
        private int quantity;
        private String specialInstructions;
        
        public CartItem(ApiClient.FoodItem foodItem, int quantity, String specialInstructions) {
            this.foodItem = foodItem;
            this.quantity = quantity;
            this.specialInstructions = specialInstructions;
        }
        
        // Getters and setters
        public ApiClient.FoodItem getFoodItem() { return foodItem; }
        public void setFoodItem(ApiClient.FoodItem foodItem) { this.foodItem = foodItem; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public String getSpecialInstructions() { return specialInstructions; }
        public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
        
        public double getTotalPrice() {
            return foodItem.getPrice() * quantity;
        }
    }
}
