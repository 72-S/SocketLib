package dev.socketlib.config;

public interface ConfigInterface {
    void loadConfig(String path);
    String getKey(String key);
}
