package com.fooddeliveryplatform.config;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppConfig {
    private static JsonObject config;
    
    static {
        try {
            // Try environment variables first
            if (System.getenv("DATABASE_URL") != null) {
                config = Json.createObjectBuilder()
                    .add("database", Json.createObjectBuilder()
                        .add("url", System.getenv("DATABASE_URL"))
                        .add("username", System.getenv("DATABASE_USERNAME"))
                        .add("password", System.getenv("DATABASE_PASSWORD")))
                    .add("server", Json.createObjectBuilder()
                        .add("port", Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"))))
                    .add("security", Json.createObjectBuilder()
                        .add("jwtSecret", System.getenv("JWT_SECRET"))
                        .add("jwtExpirationMs", Long.parseLong(System.getenv().getOrDefault("JWT_EXPIRATION_MS", "86400000")))
                        .add("passwordPepper", System.getenv("PASSWORD_PEPPER")))
                    .build();
            } 
            // Fall back to config file
            else {
                try (InputStream is = Files.newInputStream(Paths.get("config.json"));
                     JsonReader reader = Json.createReader(is)) {
                    config = reader.readObject();
                } catch (Exception e) {
                    // Final fallback to classpath resource
                    try (InputStream is = AppConfig.class.getClassLoader()
                            .getResourceAsStream("config.json");
                         JsonReader reader = Json.createReader(is)) {
                        config = reader.readObject();
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed to load configuration", ex);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Configuration initialization failed", e);
        }
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