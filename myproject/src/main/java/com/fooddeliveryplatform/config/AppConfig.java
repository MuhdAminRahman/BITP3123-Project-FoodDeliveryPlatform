package com.fooddeliveryplatform.config;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppConfig {
    private static JsonObject config;
    
    static {
        try (InputStream is = Files.newInputStream(Paths.get("config.json"));
            JsonReader reader = Json.createReader(is)) {
            JsonObjectBuilder builder = Json.createObjectBuilder(reader.readObject());
            
            // Override with environment variables if they exist
            if (System.getenv("DATABASE_URL") != null) {
                builder.add("database", Json.createObjectBuilder()
                    .add("url", getEnvOrDefault("DATABASE_URL", ""))
                    .add("username", getEnvOrDefault("DATABASE_USERNAME", ""))
                    .add("password", getEnvOrDefault("DATABASE_PASSWORD", "")));
            }
            
            if (System.getenv("PORT") != null) {
                builder.add("server", Json.createObjectBuilder()
                    .add("port", Integer.parseInt(getEnvOrDefault("PORT", "8080"))));
            }
        } catch (Exception e) {
            // Fallback to loading from resources if file not found
            try (InputStream is = AppConfig.class.getClassLoader()
                    .getResourceAsStream("config.json");
                 JsonReader reader = Json.createReader(is)) {
                config = reader.readObject();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to load configuration", ex);
            }
        }
    }
    private static String getEnvOrDefault(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return value != null ? value : defaultValue;
    }

    
    public static String getDatabaseUrl() {return config.getJsonObject("database").getString("url");}
    public static String getDatabaseUsername() {return config.getJsonObject("database").getString("username");}
    public static String getDatabasePassword() {return config.getJsonObject("database").getString("password");}
    
    public static String[] getMigrationLocations() {
        return config.getJsonObject("database")
            .getJsonObject("migration")
            .getJsonArray("locations")
            .stream()
            .map(v -> v.toString().replaceAll("^\"|\"$", "")) // Remove quotes from JSON string values
            .toArray(String[]::new);
    }
    
    public static int getServerPort() {return config.getJsonObject("server").getInt("port");}

    public static String getJwtSecret() {return config.getJsonObject("security").getString("jwtSecret");}
    public static long getJwtExpirationMs() {return config.getJsonObject("security").getJsonNumber("jwtExpirationMs").longValue();}
    public static String getPasswordPepper() {return config.getJsonObject("security").getString("passwordPepper");}

}