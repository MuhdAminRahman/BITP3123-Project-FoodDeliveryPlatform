package com.fooddeliveryplatform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class OrderDetailsFrame extends JFrame {
    private String authToken;
    private long orderId;

    public OrderDetailsFrame(String authToken, long orderId) {
        this.authToken = authToken;
        this.orderId = orderId;
        
        setTitle("Order Details");
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Order details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        try {
            URL url = new URL("http://localhost:8080/api/orders/" + orderId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonReader reader = Json.createReader(conn.getInputStream());
                JsonObject order = reader.readObject();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                
                detailsPanel.add(new JLabel("Order ID:"));
                detailsPanel.add(new JLabel(String.valueOf(order.getJsonNumber("orderId").longValue())));
                
                detailsPanel.add(new JLabel("Date:"));
                detailsPanel.add(new JLabel(dateFormat.format(new Date(order.getJsonNumber("createdAt").longValue()))));
                
                detailsPanel.add(new JLabel("Status:"));
                detailsPanel.add(new JLabel(order.getString("status")));
                
                detailsPanel.add(new JLabel("Delivery Address:"));
                detailsPanel.add(new JLabel(order.getString("deliveryAddress")));
                
                detailsPanel.add(new JLabel("Total Amount:"));
                detailsPanel.add(new JLabel(String.format("$%.2f", order.getJsonNumber("totalAmount").doubleValue())));
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error loading order details: " + conn.getResponseMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error connecting to server: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        mainPanel.add(detailsPanel, BorderLayout.NORTH);
        
        // Order items table
        DefaultTableModel itemsModel = new DefaultTableModel(new Object[]{"Item", "Quantity", "Price", "Total"}, 0);
        JTable itemsTable = new JTable(itemsModel);
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);
        mainPanel.add(itemsScrollPane, BorderLayout.CENTER);
        
        // Load order items
        try {
            URL itemsUrl = new URL("http://localhost:8080/api/orders/" + orderId + "/items");
            HttpURLConnection itemsConn = (HttpURLConnection) itemsUrl.openConnection();
            itemsConn.setRequestMethod("GET");
            itemsConn.setRequestProperty("Authorization", "Bearer " + authToken);
            
            if (itemsConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonReader itemsReader = Json.createReader(itemsConn.getInputStream());
                JsonArray items = itemsReader.readArray();
                
                for (int i = 0; i < items.size(); i++) {
                    JsonObject item = items.getJsonObject(i);
                    double price = item.getJsonNumber("pricePerUnit").doubleValue();
                    int quantity = item.getInt("quantity");
                    
                    itemsModel.addRow(new Object[]{
                        "Item " + item.getJsonNumber("foodItemId").longValue(), // Should fetch item name
                        quantity,
                        String.format("$%.2f", price),
                        String.format("$%.2f", price * quantity)
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        add(mainPanel);
    }
}