package TCP.src;

public interface TCPInterface {
    void connect(String host, int port);
    void send(String data);
    String receive();
    void close();
}
