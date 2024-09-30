package dev.socketlib;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;
import server.WebSocketServerImpl;


import javax.inject.Inject;


@Plugin(id = "socketlib", name = "SocketLib", version = "1.0.0", authors = "72-S")
public class VelocityMain {
    private final ProxyServer server;
    WebSocketServerImpl webSocketServer;

    private final Logger logger;

    @Inject
    public VelocityMain(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        logger.info("SocketLib injected");
    }



    @Subscribe
    public void onProxyInitialization(final ProxyInitializeEvent event) {
        logger.info("SocketLib enabled");
        webSocketServer = new WebSocketServerImpl(logger);
        webSocketServer.startServer(8080, "localhost");


    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        logger.info("SocketLib disabled");
        webSocketServer.stopServer(0);
    }


}
