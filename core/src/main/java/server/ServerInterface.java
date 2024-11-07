package server;

import org.json.JSONObject;

public interface ServerInterface {
    void startServer(int port, String address);
    void stopServer(int timeout);
}

