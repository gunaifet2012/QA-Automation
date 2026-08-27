package com.orangehrm.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private final Properties properties = new Properties();

    private ConfigManager() {
        String env = System.getProperty("env", "dev");
        String configFile = "config/config-" + env + ".properties";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (is == null) {
                throw new RuntimeException("Config file not found on classpath: " + configFile);
            }
            properties.load(is);
            log.info("Loaded configuration for environment: {}", env);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + configFile, e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new RuntimeException("Required property not found: " + key);
        }
        return value.trim();
    }

    public String get(String key, String defaultValue) {
        return System.getProperty(key, properties.getProperty(key, defaultValue)).trim();
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public String getApiBaseUrl() {
        return get("api.base.url");
    }

    public String getAdminUsername() {
        return get("admin.username");
    }

    public String getAdminPassword() {
        return get("admin.password");
    }

    public int getExplicitWait() {
        return getInt("explicit.wait.seconds");
    }
}
