package server;

import org.java_websocket.WebSocket;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import utils.SSLUtils;

import javax.net.ssl.SSLContext;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WebSocketServerImpl implements ServerInterface {
    private final Logger logger;
    private WebSocketServer server;
    private final Set<WebSocket> connections = Collections.synchronizedSet(new HashSet<>());

    public WebSocketServerImpl(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void startServer(int port, String address) {
        server = new WebSocketServer(new InetSocketAddress(address, port)) {

            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                connections.add(conn);
                logger.info("New connection from {}", conn.getRemoteSocketAddress());
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                connections.remove(conn);
                logger.info("Connection closed: {}", conn.getRemoteSocketAddress());
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                JSONObject jsonMessage = new JSONObject(message);
            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                logger.error("An error occurred: {}", ex.getMessage());
            }

            @Override
            public void onStart() {
                logger.info("Server started on {}:{}", getAddress().getHostString(), getAddress().getPort());
            }
        };

        try {
            SSLContext sslContext = SSLUtils.createServerSSLContext();
            server.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(sslContext));
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage());
        }

        server.start();
    }

    @Override
    public void stopServer(int timeout) {
        if (server != null) {
            try {
                server.stop(timeout);
                logger.info("Server stopped");
            } catch (InterruptedException e) {
                logger.error("An error occurred: {}", e.getMessage());
            }
        }
    }

}
