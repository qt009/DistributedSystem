package Bank.src;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
                //dataOutputStream.close(); // close the output stream when we're done.

                switch (command) {
                    case "CONNECT":
                        out.println("CONNECT: OK");
                        System.out.println("CONNECT: OK");
                        out.flush();

                        out.flush(); // flush the buffer to send the message immediately*/
                        break;

                    case "POST":
                        String inputLine2;
                        inputLine2 = in.readLine();
                        System.out.println(inputLine2);
                        out.println("POST: OK");
                        System.out.println("POST: OK");
                        out.flush();


                    case "GET":


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