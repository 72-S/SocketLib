package dev.socketlib.config;

public interface ConfigInterface {
    void loadConfig();
    void saveConfig();
    void getConfig(String key);
}
