package client;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import org.json.JSONObject;
import utils.SSLUtils;


import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.net.URI;
import java.util.logging.Logger;

public class WebSocketClientImpl implements ClientInterface {

    private WebSocketClient client;
    private final Logger logger;


    public WebSocketClientImpl(Logger logger) {
        this.logger = logger;
    }


    @Override
    public void connect(String address, int port) {
        try {
            client = new WebSocketClient(new URI("wss://" + address + ":" + port)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    logger.info("Connected to server: " + getURI());
                }

                @Override
                public void onMessage(String message) {
                    JSONObject jsonMessage = new JSONObject(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    logger.info("Disconnected from server");
                }

                @Override
                public void onError(Exception ex) {
                    logger.severe("An error occurred: " + ex.getMessage());
                }
            };

            SSLContext sslContext = SSLUtils.createClientSSLContext();
            SSLSocketFactory factory = sslContext.getSocketFactory();
            client.setSocketFactory(factory);

            client.connect();
        } catch (Exception e) {
            logger.severe("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (client != null) {
            client.close();
        }
    }

    
}

