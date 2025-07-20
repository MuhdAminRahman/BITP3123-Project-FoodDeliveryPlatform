package com.fooddeliveryplatform.model;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class FoodItem {
    private Long food_Id;
    private String category;
    private String description;
    private String imageUrl; // Assuming this field is needed
    private boolean is_available;
    private String name;
    private Double price;
    private Long restaurantId;
    

    public FoodItem() {}


    public FoodItem(Long food_Id, String category, String description, String imageUrl, Boolean is_available, 
                    String name, Double price, Long restaurantId) {
        this.food_Id = food_Id;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.is_available = is_available;
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public Long getFood_Id() {
        return food_Id;
    }
    public void setFood_Id(Long food_Id) {
        this.food_Id = food_Id;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Boolean getIs_available() {
        return is_available;
    }
    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }


    //get request
    public JsonObject toJson() {
        JsonObject json = Json.createObjectBuilder()
            .add("food_Id", food_Id)
            .add("category", category)
            .add("imageUrl", imageUrl == null ? JsonValue.NULL : Json.createValue(imageUrl)) // Handle null case
            .add("description", description)
            .add("is_available", is_available)
            .add("name", name)
            .add("price", price)
            .add("restaurantId", restaurantId) // Assuming restaurantId is used for serialization
            .build();
        return json;
    }
    
    //post request
    public static FoodItem fromJson(JsonObject json) {

        FoodItem foodItem = new FoodItem();

        foodItem.setCategory(json.getString("category"));
        foodItem.setDescription(json.getString("description"));
        foodItem.setImageUrl(json.getString("imageUrl", null)); // Default to null if not present
        foodItem.setIs_available(json.getBoolean("is_available", true)); // Default to true if not present
        foodItem.setName(json.getString("name"));
        foodItem.setPrice(json.getJsonNumber("price").doubleValue());
        foodItem.setRestaurantId(json.getJsonNumber("restaurantId").longValue());
        

        return foodItem;
    }
}
