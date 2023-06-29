package Bank.src;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private Bank bank;
    private final Socket clientSocket;
    // private String clientUsername;

    public ClientHandler(Socket clientSocket, Bank bank) {
        this.clientSocket = clientSocket;
        this.bank = bank;
    }

    @Override
    public void run() {
        try {
            // Read data from the client and send responses back
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Message from Web: " + inputLine);
                String[] tokens = inputLine.split(" ", 2);
                String command = tokens[0];
                String content = tokens[1];

                if (tokens.length < 2) {
                    System.out.println("ERROR: Invalid command");
                    break;
                }

                /*OutputStream outputStream = clientSocket.getOutputStream();

                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                dataOutputStream.writeUTF("Sending to the other side this message: ");
                dataOutputStream.flush();
                dataOutputStream.close();*/
                System.out.println("Command is " + command + " and content is " + content);
                switch (command) {
                    case "CONNECT":
                        out.println("Hello from the bank! with IP: " + clientSocket.getInetAddress() + " and port: " + clientSocket.getPort());
                        System.out.println("CONNECT: OK");
                        out.flush(); // flush the buffer to send the message immediately*/
                        break;

                    case "POST":
                        String[] token = content.split(":", 2);
                        double change = Double.parseDouble(token[1]);

                        if (Objects.equals(token[0], "Add")) {
                            System.out.println("ADD " + change);
                            bank.addTotalValue(change);
                        } else if (Objects.equals(token[0], "Sub")) {
                            System.out.println("SUB " + change);
                            bank.subTotalValue(change);
                        }

                        out.println("POST: OK");
                        System.out.println("POST: OK");
                        System.out.println("Update Total Value: " + bank.getTotalValue());
                        bank.setBankValueUpdated(true);
                        handlePossibleBankruptcy();
                        out.flush();
                        break;

                    case "GET":
                        if (Objects.equals(content, "TotalBalance")) {
                            System.out.println(content);
                            out.println("TotalBalance:"+bank.getTotalValue());
                        }
                        System.out.println("GET: OK");
                        out.flush();
                        break;

                    case "DELETE":

                        break;

                    case "DISCONNECT":
                        in.close();
                        out.close();
                        clientSocket.close();
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
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

    }
    public void handlePossibleBankruptcy() throws UnknownHostException, MqttException {
        if(bank.getTotalValue() <= 0){
            bank.askForHelp();
        }

        if(bank.isBankrupt()){
            System.out.println("Bank is bankrupt");
            Thread.currentThread().interrupt();
        }
    }
}