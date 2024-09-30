package dev.socketlib;

import client.WebSocketClientImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitMain extends JavaPlugin  {
    WebSocketClientImpl webSocketClient;

    @Override
    public void onEnable() {
        getLogger().info("SocketLib enabled");
        webSocketClient = new WebSocketClientImpl(getLogger());
        webSocketClient.connect("localhost", 8080);
    }

    @Override
    public void onDisable() {
        getLogger().info("SocketLib disabled");
        webSocketClient.disconnect();
    }

}
