package com.fooddeliveryplatform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class RestaurantMenuFrame extends JFrame {
    private String authToken;
    private long restaurantId;
    private DefaultTableModel menuTableModel;
    private JTable menuTable;
    private JSpinner quantitySpinner;

    public RestaurantMenuFrame(String authToken, long restaurantId) {
        this.authToken = authToken;
        this.restaurantId = restaurantId;
        
        setTitle("Restaurant Menu");
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Menu table
        menuTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Price", "Available"}, 0);
        menuTable = new JTable(menuTableModel);
        JScrollPane scrollPane = new JScrollPane(menuTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Order panel
        JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        orderPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        orderPanel.add(quantitySpinner);
        
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
            }
        });
        orderPanel.add(addToCartButton);
        
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PlaceOrderFrame(authToken, restaurantId).setVisible(true);
            }
        });
        orderPanel.add(placeOrderButton);
        
        mainPanel.add(orderPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        loadMenu();
    }
    
    private void loadMenu() {
        menuTableModel.setRowCount(0);
        
        try {
            URL url = new URL("http://localhost:8080/api/fooditems" );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonReader reader = Json.createReader(conn.getInputStream());
                JsonArray menuItems = reader.readArray();
                
                for (int i = 0; i < menuItems.size(); i++) {
                    JsonObject item = menuItems.getJsonObject(i);
                    menuTableModel.addRow(new Object[]{
                        item.getJsonNumber("food_Id").longValue(),
                        item.getString("name"),
                        item.getString("description"),
                        item.getJsonNumber("price").doubleValue(),
                        item.getBoolean("is_available") ? "Yes" : "No"
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error loading menu: " + conn.getResponseMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error connecting to server: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addToCart() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow >= 0) {
            long foodId = (long) menuTableModel.getValueAt(selectedRow, 0);
            String foodName = (String) menuTableModel.getValueAt(selectedRow, 1);
            double price = (double) menuTableModel.getValueAt(selectedRow, 3);
            int quantity = (int) quantitySpinner.getValue();
            
            // In a real app, you would add this to a shopping cart model
            JOptionPane.showMessageDialog(this, 
                    "Added " + quantity + " x " + foodName + " to cart", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Please select a menu item", 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}