package com.fooddeliveryplatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;

public class PlaceOrderFrame extends JFrame {
    private String authToken;
    private long restaurantId;

    public PlaceOrderFrame(String authToken, long restaurantId) {
        this.authToken = authToken;
        this.restaurantId = restaurantId;
        
        setTitle("Place Order");
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        // Delivery address
        formPanel.add(new JLabel("Delivery Address:"));
        JTextField addressField = new JTextField();
        formPanel.add(addressField);
        
        // Payment method
        formPanel.add(new JLabel("Payment Method:"));
        JComboBox<String> paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Cash on Delivery"});
        formPanel.add(paymentMethodCombo);
        
        // Special instructions
        formPanel.add(new JLabel("Special Instructions:"));
        JTextArea instructionsArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(instructionsArea));
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String address = addressField.getText();
                String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
                String instructions = instructionsArea.getText();
                
                if (address.isEmpty()) {
                    JOptionPane.showMessageDialog(PlaceOrderFrame.this, 
                            "Please enter a delivery address", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    boolean success = placeOrder(address, paymentMethod, instructions);
                    if (success) {
                        JOptionPane.showMessageDialog(PlaceOrderFrame.this, 
                                "Order placed successfully!", 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(PlaceOrderFrame.this, 
                            "Error placing order: " + ex.getMessage(), 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(placeOrderButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private boolean placeOrder(String address, String paymentMethod, String instructions) throws IOException {
        URL url = new URL("http://localhost:8080/api/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + authToken);
        conn.setDoOutput(true);
        java.sql.Date createdAt = new java.sql.Date(new Date().getTime());
        // In a real app, you would include the actual order items from the cart
        JsonObject order = Json.createObjectBuilder()

                .add("createdAt", createdAt.toString())
                .add("deliveryAddress", address)
                .add("status", "PENDING")
                .add("totalAmount", 0.0) // Should calculate from cart
                .add("customerId", 5)// Should get from user profile
                .add("restaurantId", restaurantId)
                .build();
        
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = order.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        return conn.getResponseCode() == HttpURLConnection.HTTP_CREATED;
    }
}