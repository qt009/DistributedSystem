package Testing;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class KeyValueServer {
    private static Map<String, String> keyValueStore;
    private static Map<String, Boolean> clientConnected;

    public KeyValueServer() {
        keyValueStore = new HashMap<>();
        clientConnected = new HashMap<>();
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                // Accept incoming client connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress() + ":"
                        + clientSocket.getPort());

                // Create a new thread for each client once connected
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Error: Port " + port + " is already in use.");
            System.exit(1);
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        // private String clientUsername;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Read data from the client and send responses back
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    String[] tokens = inputLine.split(" ", 2);
                    String command = tokens[0];

                    if (tokens.length < 2) {
                        System.out.println("ERROR: Invalid command");
                        break;
                    }


                    // get the output stream from the socket.
                    OutputStream outputStream = clientSocket.getOutputStream();
                    // create a data output stream from the output stream so we can send data through it
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                    System.out.println("Sending string to the Web");

                    // write the message we want to send
                    dataOutputStream.writeUTF("Hello from the other side!");
                    dataOutputStream.flush(); // send the message
                    //.close(); // close the output stream when we're done.

                    System.out.println("Closing socket and terminating program.");

                    switch (command) {
                        case "CONNECT":
                            out.println("CONNECT: OK");
                            System.out.println("CONNECT: OK");
                            out.flush();
                        /*if (clientConnected.containsKey(tokens[1])) {
                            System.out.println("CONNECT: ERROR");
                            out.println("CONNECT: ERROR");
                            break;
                        }

                        String clientId = tokens[1];
                        clientConnected.put(clientId, true);
                        // keyValueStore.putIfAbsent(clientId, "");
                        out.println("CONNECT: OK");
                        System.out.println("CONNECT: OK");
                        out.flush(); // flush the buffer to send the message immediately*/
                            break;

                        case "POST":
//                        System.out.println(clientConnected.toString());
//
//                        if (clientConnected.containsValue(true)) {
                            String inputLine2;
                            inputLine2 = in.readLine();
                            System.out.println(inputLine2);
                            out.println("POST: OK");
                            System.out.println("POST: OK");
                            out.flush();
//                            keyValueStore.put(tokens[1], inputLine2);
//                            System.out.println(keyValueStore.toString());
//                            System.out.println("PUT: OK");
//                        } else {
//                            System.out.println("PUT: ERROR");
//                        }
//                        break;

                        case "GET":

                        /*if (clientConnected.containsValue(true)) {
                            String value = keyValueStore.getOrDefault(tokens[1], "");
                            if (value.isEmpty()) {
                                System.out.println("GET: ERROR");
                            } else {
                                System.out.println(value);
                            }
                        } else {
                            System.out.println("GET: ERROR");
                        }*/
                            break;

                        case "DELETE":
                        /*if (clientConnected.containsValue(true)) {
                            keyValueStore.remove(tokens[1]);
                            System.out.println("DELETE: OK");
                        } else {
                            System.out.println("DELETE: ERROR");
                        }*/
                            break;

                        case "DISCONNECT":
                        /*if (clientConnected.containsValue(true)) {

                            clientConnected.remove(tokens[0]);
                            keyValueStore.remove(tokens[0]);
                            clientConnected.clear();
                            System.out.println("DISCONNECT: OK");
                            out.println("DISCONNECT: OK");
                            System.exit(0);
                        } else {
                            System.out.println("DISCONNECT: OK");
                            out.println("DISCONNECT: OK");
                        }*/
                            break;

                        default:
                            System.out.println("ERROR: Invalid command");
                            out.println("ERROR: Invalid command");
                            in.close();
                            out.close();
                            clientSocket.close();
                            System.exit(0);
                            break;
                    }

                }

                // Close the client socket when the client disconnects
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        KeyValueServer server = new KeyValueServer();
        server.start(8080);
    }
}
