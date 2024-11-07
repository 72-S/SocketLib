package client;

import org.json.JSONObject;

public interface ClientInterface {
    void connect(String address, int port);
    void disconnect();
}
