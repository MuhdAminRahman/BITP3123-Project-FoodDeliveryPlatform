package com.fooddeliveryplatform;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class OrdersPanel extends JPanel {
    private String authToken;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public OrdersPanel(String authToken) {
        this.authToken = authToken;
        setLayout(new BorderLayout());

        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Date", "Restaurant", "Status", "Amount"}, 0);
        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewDetailsButton = new JButton("View Details");
        JButton refreshButton = new JButton("Refresh");
        
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ordersTable.getSelectedRow();
                if (selectedRow >= 0) {
                    long orderId = (long) tableModel.getValueAt(selectedRow, 0);
                    new OrderDetailsFrame(authToken, orderId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(OrdersPanel.this, 
                            "Please select an order", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadOrders();
            }
        });
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load orders
        loadOrders();
    }

    private void loadOrders() {
        tableModel.setRowCount(0); // Clear existing data
        
        try {
            URL url = new URL("http://localhost:8080/api/orders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonReader reader = Json.createReader(conn.getInputStream());
                JsonArray orders = reader.readArray();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                
                for (int i = 0; i < orders.size(); i++) {
                    JsonObject order = orders.getJsonObject(i);
                    
                    // Safely handle JSON values that might be numbers or strings
                    long orderId = getJsonLong(order, "orderId");
                    long createdAt = getJsonLong(order, "createdAt");
                    long restaurantId = getJsonLong(order, "restaurantId");
                    double totalAmount = getJsonDouble(order, "totalAmount");
                    
                    tableModel.addRow(new Object[]{
                        orderId,
                        dateFormat.format(new Date(createdAt)),
                        "Restaurant " + restaurantId, // Should fetch restaurant name
                        order.getString("status", "UNKNOWN"),
                        String.format("$%.2f", totalAmount)
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error loading orders: " + conn.getResponseMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error connecting to server: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to safely get long values from JSON
    private long getJsonLong(JsonObject obj, String key) {
        JsonValue value = obj.get(key);
        if (value.getValueType() == JsonValue.ValueType.NUMBER) {
            return obj.getJsonNumber(key).longValue();
        } else if (value.getValueType() == JsonValue.ValueType.STRING) {
            try {
                return Long.parseLong(obj.getString(key));
            } catch (NumberFormatException e) {
                return 0L;
            }
        }
        return 0L;
    }
    
    // Helper method to safely get double values from JSON
    private double getJsonDouble(JsonObject obj, String key) {
        JsonValue value = obj.get(key);
        if (value.getValueType() == JsonValue.ValueType.NUMBER) {
            return obj.getJsonNumber(key).doubleValue();
        } else if (value.getValueType() == JsonValue.ValueType.STRING) {
            try {
                return Double.parseDouble(obj.getString(key));
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }
}