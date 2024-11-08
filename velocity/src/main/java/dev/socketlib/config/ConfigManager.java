package dev.socketlib.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;


public class ConfigManager implements ConfigInterface {
    
    private Map<String, Object> configData;
    private final Yaml yaml;
    private static ConfigManager instance;
    
    public ConfigManager() {

        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);

    }
    @Override
    public void loadConfig(String path) {
        File configFile = new File(path);

        if (!configFile.exists()) {
            copyConfig(path);
        }

        // Load the config file
        try (InputStream inputStream = Files.newInputStream(configFile.toPath())) {
            configData = yaml.load(inputStream);
            if (configData == null) {
                configData = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getKey(String key) {
        if (configData != null && configData.containsKey(key)) {
            return configData.get(key).toString();
        } else {
            return null;
        }
    }

    private void copyConfig(String path) {
        File configFile = new File(path);

        // Attempt to copy config.yml from the resources
        try (InputStream in = getClass().getResourceAsStream("/config.yml");
             OutputStream out = Files.newOutputStream(configFile.toPath())) {
            if (in == null) {
                System.out.println("Resource config.yml not found in the plugin JAR.");
                return;
            }

            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            System.out.println("Default config.yml copied to: " + path);
        } catch (IOException e) {
            System.out.println("Failed to copy default config.yml: " + e.getMessage());
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

}

