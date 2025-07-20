package com.fooddeliveryplatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private String authToken;

    public MainFrame(String authToken) {
        this.authToken = authToken;
        setTitle("Food Delivery Platform - Customer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem restaurantsItem = new JMenuItem("Restaurants");
        restaurantsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRestaurantsPanel();
            }
        });
        JMenuItem ordersItem = new JMenuItem("My Orders");
        ordersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOrdersPanel();
            }
        });
        viewMenu.add(restaurantsItem);
        viewMenu.add(ordersItem);
        
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);

        // Tabbed pane for main content
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Restaurants", new RestaurantsPanel(authToken));
        tabbedPane.addTab("My Orders", new OrdersPanel(authToken));
        
        add(tabbedPane);
    }

    private void showRestaurantsPanel() {
        getContentPane().removeAll();
        add(new RestaurantsPanel(authToken));
        revalidate();
        repaint();
    }

    private void showOrdersPanel() {
        getContentPane().removeAll();
        add(new OrdersPanel(authToken));
        revalidate();
        repaint();
    }
}