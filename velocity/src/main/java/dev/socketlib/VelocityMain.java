package dev.socketlib;

import javax.inject.Inject;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;



@Plugin(id = "socketlib", name = "SocketLib", version = "1.0.0", authors = "72-S")
public class VelocityMain {
    private final ProxyServer server;

    private final Logger logger;

    @Inject
    public VelocityMain(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        logger.info("SocketLib injected");
    }



    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("SocketLib enabled");
        new Startup(server);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("SocketLib disabled");
    }


}
