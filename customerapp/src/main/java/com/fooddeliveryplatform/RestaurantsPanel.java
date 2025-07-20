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

public class RestaurantsPanel extends JPanel {
    private String authToken;
    private JTable restaurantTable;
    private DefaultTableModel tableModel;

    public RestaurantsPanel(String authToken) {
        this.authToken = authToken;
        setLayout(new BorderLayout());

        // Table model
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Status"}, 0);
        restaurantTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(restaurantTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton viewMenuButton = new JButton("View Menu");
        JButton refreshButton = new JButton("Refresh");
        
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = restaurantTable.getSelectedRow();
                if (selectedRow >= 0) {
                    long restaurantId = (long) tableModel.getValueAt(selectedRow, 0);
                    new RestaurantMenuFrame(authToken, restaurantId).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(RestaurantsPanel.this, 
                            "Please select a restaurant", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRestaurants();
            }
        });
        
        buttonPanel.add(viewMenuButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load restaurants
        loadRestaurants();
    }

    private void loadRestaurants() {
        tableModel.setRowCount(0); // Clear existing data
        
        try {
            URL url = new URL("http://localhost:8080/api/restaurants");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                JsonReader reader = Json.createReader(conn.getInputStream());
                JsonArray restaurants = reader.readArray();
                
                for (int i = 0; i < restaurants.size(); i++) {
                    JsonObject restaurant = restaurants.getJsonObject(i);
                    tableModel.addRow(new Object[]{
                        restaurant.getJsonNumber("id").longValue(),
                        restaurant.getString("name"),
                        restaurant.getString("description"),
                        restaurant.getBoolean("isActive") ? "Open" : "Closed"
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error loading restaurants: " + conn.getResponseMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                    "Error connecting to server: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}