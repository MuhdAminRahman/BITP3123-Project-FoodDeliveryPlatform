package com.fooddeliveryplatform.httphandler;

import com.fooddeliveryplatform.dao.RestaurantDao;
import com.fooddeliveryplatform.dao.UserDao;
import com.fooddeliveryplatform.model.Restaurant;
import com.fooddeliveryplatform.model.User;
import com.fooddeliveryplatform.model.UserRole;
import com.fooddeliveryplatform.util.JsonUtils;
import com.fooddeliveryplatform.util.JwtUtil;
import com.fooddeliveryplatform.util.PasswordUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.io.OutputStream;

public class AuthHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                handleAuthRequest(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            sendResponse(exchange, 500, "Internal Server Error");
        }
    }

    /* Example request body for authentication:
        To login:
        POST /api/auth
        Content-Type: application/json
        {
            "action": "customerlogin",
            "username": "testuser",
            "password": "securepassword123"
        }

        To register a new user:
     * POST /api/auth
        Content-Type: application/json
        {
            "action": "customerregister",
            "username": "newuser",
            "email": "test1@test.com",
            "password": "newpassword123",
            "role": "CUSTOMER",
            "fullName": "New User",
            "phone": "1234567890",
            "address": "123 New Street"
        }
    */
    private void handleAuthRequest(HttpExchange exchange) throws IOException {
        JsonObject request = JsonUtils.parseRequest(exchange);
        String action = request.getString("action", "");
        if (action.isEmpty()) {
            sendResponse(exchange, 400, "Missing action parameter");
            return;
        }
        switch (action) {
            case "customerlogin":
                handleCustomerLogin(exchange, request);
                break;
            case "restaurantlogin":
                handleRestaurantLogin(exchange, request);
                break;
            case "customerregister":
                handleCustomerRegister(exchange, request);
                break;
            case "restaurantregister":
                handleRestaurantRegister(exchange, request);
                break;
            default:
                sendResponse(exchange, 400, "Invalid action");
        }
    }

    private void handleCustomerLogin(HttpExchange exchange, JsonObject request) throws IOException {
        String username = request.getString("username");
        String password = request.getString("password");

        UserDao userDao = new UserDao();
        User user = userDao.findByUsername(username);
        if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
            sendResponse(exchange, 401, "Invalid credentials");
            return;
        }
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole().toString());
        JsonObject response = Json.createObjectBuilder()
            .add("token", token)
            .add("user", user.toJson())
            .build();
        
        sendJsonResponse(exchange, 200, response.toString());
    }

    private void handleRestaurantLogin(HttpExchange exchange, JsonObject request) throws IOException {
        String username = request.getString("username");
        String password = request.getString("password");

        UserDao userDao = new UserDao();
        User user = userDao.findByUsername(username);
        System.out.println("User found: " + (user != null ? user.getUsername() : "null"));
        System.out.println("userid: " + (user != null ? user.getUserId() : "null"));
        RestaurantDao restaurantDao = new RestaurantDao();
        Restaurant restaurant = restaurantDao.findOwnerById(user.getUserId());
        if (restaurant == null) {
            sendResponse(exchange, 404, "Restaurant not found for user");
            return;
        }
        if (user == null || !PasswordUtil.verifyPassword(password, user.getPassword())) {
            sendResponse(exchange, 401, "Invalid credentials");
            return;
        }
        if (user.getRole() != UserRole.RESTAURANT) {
            sendResponse(exchange, 403, "Access denied for non-restaurant users");
            return;
        }
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole().toString());
        JsonObject response = Json.createObjectBuilder()
            .add("token", token)
            .add("user", user.toJson())
            .build();
        sendJsonResponse(exchange, 200, response.toString());
    }

    private void handleCustomerRegister(HttpExchange exchange, JsonObject request) throws IOException {
        UserDao userDao = new UserDao();
        System.out.println("Registering user: " + request);
        User user = User.fromJson(request);
        user.setPasswordHash(PasswordUtil.hashPassword(request.getString("password")));

        if (user.getUsername() == null || user.getPassword() == null) {
            sendResponse(exchange, 400, "Username and password are required");
            return;
        }
        if (user.getRole() == null) {
            user.setRole(UserRole.CUSTOMER); // Default role
        }
        if (userDao.findByUsername(user.getUsername()) != null) {
            sendResponse(exchange, 400, "Username already exists");
            return;
        }
        userDao.save(user);
        sendResponse(exchange, 201, "User registered successfully");
    }

    /*
     * Handles restaurant registration.
     * This method expects a JSON request body with restaurant and user details.
     * It creates a new restaurant and user, sets the user as the owner of the restaurant,
     * and saves both to the database.
     * It also hashes the user's password before saving.
     * If the username or password is missing, it responds with a 400 Bad Request.
     * If the username already exists, it responds with a 400 Bad Request.
     * If successful, it responds with a 201 Created status.
     * * @param exchange The HTTP exchange containing the request and response.
     * * @param request The JSON request object containing restaurant and user details.
     * * @throws IOException If an I/O error occurs while processing the request.
     * * Example request body for restaurant registration:
     * {
            "action": "restaurantregister",
            "username": "restaurantowner",
            "address": "123 Restaurant St",
            "email": "restaurant@test.com",
            "fullName": "Restaurant Owner",
            "isActive": 1,
            "password": "securepassword123",
            "phone": "1234567890",
            "role" : "RESTAURANT",
            "closingTime": "22:00",
            "deliveryRadius": 5,
            "description": "Best restaurant in town",
            "logoUrl": "http://example.com/logo.png",
            "name": "Best Restaurant",
            "openingTime": "10:00",
            "isActive2": 1
        }
     */
    private void handleRestaurantRegister(HttpExchange exchange, JsonObject request) throws IOException {
        //System.out.println("Handling restaurant registration: " + request);

        RestaurantDao restaurantDao = new RestaurantDao();
        Restaurant restaurant = Restaurant.fromJson(request);
        UserDao userDao = new UserDao();
        User user = restaurant.getOwner();
        user.setPasswordHash(PasswordUtil.hashPassword(request.getString("password")));
        restaurant.setOwner(user);
        restaurant.setActive(true); // Default to active

        if (user.getUsername() == null || user.getPassword() == null) {
            sendResponse(exchange, 400, "Username and password are required");
            return;
        }
        if (user.getRole() == null) {
            user.setRole(UserRole.RESTAURANT); // Default role for restaurant
        }
        if (userDao.findByUsername(user.getUsername()) != null) {
            sendResponse(exchange, 400, "Username already exists");
            return;
        }
        System.out.println("Saving user to database");
        userDao.save(user);
        System.out.println("Saving restaurant to database");
        restaurantDao.save(restaurant);
        System.out.println("Restaurant registered successfully: " + restaurant.getName());
        sendResponse(exchange, 201, "Restaurant registered successfully");
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, statusCode, response);
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}