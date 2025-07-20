package com.fooddeliveryplatform.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.fooddeliveryplatform.httphandler.*;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyServer {

    private HttpServer server;
    private Map<String, HttpHandler> handlerMap;

    public MyServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(null); // creates a default executor
        handlerMap = new HashMap<>();
        
        initializeHandlers();
        registerContexts();
    }

    private void initializeHandlers() {
        // Create handler instances
        AuthHandler authHandler = new AuthHandler();
        UserApiHandler userHandler = new UserApiHandler();
        RestaurantApiHandler restaurantHandler = new RestaurantApiHandler();
        FoodItemApiHandler foodItemHandler = new FoodItemApiHandler();
        OrderApiHandler orderHandler = new OrderApiHandler();
        OrderItemApiHandler orderItemHandler = new OrderItemApiHandler();
        PaymentApiHandler paymentHandler = new PaymentApiHandler();
        DeliveryTrackingApiHandler deliveryTrackingHandler = new DeliveryTrackingApiHandler();

        // Store handlers in map with their base paths
        handlerMap.put("/api/auth", authHandler);
        
        // User/Customer related endpoints
        handlerMap.put("/api/users", new AuthMiddleware(userHandler, "CUSTOMER","RESTAURANT","DELIVERY_PARTNER"));
        handlerMap.put("/api/users/{id}", new AuthMiddleware(userHandler, "CUSTOMER","RESTAURANT","DELIVERY_PARTNER"));
        
        // Restaurant endpoints
        handlerMap.put("/api/restaurants", new AuthMiddleware(restaurantHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/restaurants/{id}", new AuthMiddleware(restaurantHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/restaurants/{id}/menu", new AuthMiddleware(foodItemHandler, "CUSTOMER","RESTAURANT"));
        
        // Food item endpoints
        handlerMap.put("/api/fooditems", new AuthMiddleware(foodItemHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/fooditems/{id}", new AuthMiddleware(foodItemHandler, "CUSTOMER","RESTAURANT"));
        
        // Order endpoints
        handlerMap.put("/api/orders", new AuthMiddleware(orderHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/orders/{id}", new AuthMiddleware(orderHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/orders/{id}/items", new AuthMiddleware(orderItemHandler, "CUSTOMER","RESTAURANT"));
        
        // Order item endpoints
        handlerMap.put("/api/orderitems", new AuthMiddleware(orderItemHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/orderitems/{id}", new AuthMiddleware(orderItemHandler, "CUSTOMER","RESTAURANT"));
        
        // Payment endpoints
        handlerMap.put("/api/payments", new AuthMiddleware(paymentHandler, "CUSTOMER","RESTAURANT"));
        handlerMap.put("/api/orders/{id}/payment", new AuthMiddleware(paymentHandler, "CUSTOMER","RESTAURANT"));
        
        // Delivery tracking endpoints
        handlerMap.put("/api/deliveries", new AuthMiddleware(deliveryTrackingHandler, "DELIVERY_PARTNER"));
        handlerMap.put("/api/orders/{id}/tracking", new AuthMiddleware(deliveryTrackingHandler, "CUSTOMER","DELIVERY_PARTNER","RESTAURANT"));
    }

    private void registerContexts() {
        // Register all handlers from the map
        for (Map.Entry<String, HttpHandler> entry : handlerMap.entrySet()) {
            server.createContext(entry.getKey(), entry.getValue());
        }
    }

    public void start() {
        server.start();
        System.out.println("Server started on port: " + server.getAddress().getPort());
        System.out.println("Available endpoints:");
        handlerMap.keySet().forEach(endpoint -> System.out.println("  " + endpoint));
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped.");
    }

    public void addHandler(String context, HttpHandler handler) {
        server.createContext(context, handler);
        handlerMap.put(context, handler);
        System.out.println("Added new endpoint: " + context);
    }
}