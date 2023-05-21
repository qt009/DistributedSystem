import TCP.src.TCPInterface;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class BankTCP implements TCPInterface {
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    private ServerSocket serverSocket;

    public BankTCP(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Bank TCP server started on address " + InetAddress.getLocalHost().getHostAddress() + " on port " + port);
    }

    @Override
    public void connect(String host, int port) {

    }

    @Override
    public void send(String data) {

    }

    @Override
    public String receive() {
        return null;
    }

    @Override
    public void close() {

    }
}
