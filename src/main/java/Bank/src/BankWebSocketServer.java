/*

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/")
public class BankWebSocketServer {

    private static Set<Session> sessions = new HashSet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("WebSocket connection opened.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message from WebSocket: " + message);

        // Handle the message and interact with the Java ServerSocket here

        // Example: Sending a response back to the WebSocket client
        String response = "Response from Java ServerSocket";
        session.getAsyncRemote().sendText(response);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("WebSocket connection closed.");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("WebSocket error: " + error.getMessage());
    }

    // Run the WebSocket server
    public static void main(String[] args) throws Exception {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            WebSocketWorker worker = new WebSocketWorker(socket);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }

    private static class WebSocketWorker implements Runnable {
        private Socket socket;

        public WebSocketWorker(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            // Handle the TCP connection with the Java ServerSocket
            // Example: Read input from the socket, process data, etc.
        }
    }
}
*/
