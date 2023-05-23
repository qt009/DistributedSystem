package Bank.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import Stock.Stock;


public class Bank implements Serializable, Runnable {
    private final String name;
    private final Map<Stock, Integer> securities; // Map of securities held by the bank

    private BankTCP bankTCP;

    private int totalPackageReceived = 0;


    public Bank(String name) {
        this.name = name;
        securities = new HashMap<>();

    }

    public String getName() {
        return name;
    }

    public void addSecurity(String abbreviation, double price, int quantity) {
        Stock stock = new Stock(abbreviation, price);
        securities.put(stock, quantity);
    }

    public void updateSecurityPrice(String abbreviation, double newPrice) {
        for (Stock stock : securities.keySet()) {
            if (stock.getAbbreviation().equals(abbreviation)) {
                stock.setPrice(newPrice);
            }
        }
    }




    public double calculatePortfolioValue() {
        double total = 0;
        for (Stock stock : securities.keySet()) {
            total += stock.getPrice() * securities.get(stock);
        }
        return total;
    }

    public void printPortfolio() {
        System.out.println("Portfolio for " + name);
        for (Stock stock : securities.keySet()) {
            System.out.println(stock.getAbbreviation() + ": $" + stock.getPrice());
        }
        System.out.println("Total value: $" + calculatePortfolioValue());
    }

    @Override
    public void run() {
        runUDPSocket();
        runTCPSocket();
    }

    private void runTCPSocket() {
        try {
            bankTCP = new BankTCP(Integer.parseInt(getThisBankPortTCP()));
            ServerSocket tcpServerSocket = bankTCP.getServerSocket();

            while (true) {
                Socket tcpSocket = tcpServerSocket.accept();
                System.out.println("TCP connection accepted from " + tcpSocket.getInetAddress() + ":" + tcpSocket.getPort());

                ClientHandler clientHandler = new ClientHandler(tcpSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    private void runUDPSocket() {
        Thread udpThread = new Thread(() -> {
            try (DatagramSocket udpSocket = new DatagramSocket(Integer.parseInt(getThisBankPortUDP()), getInetAddress())) {
                System.out.println("UDP Server listening on " + udpSocket.getLocalAddress() + ":" + udpSocket.getLocalPort());

                while (true) {
                    receiveFromUDP(udpSocket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        udpThread.start();
    }

    private void handleTCPConnection(Socket tcpSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            String message = reader.readLine();
            System.out.println("Received message from WebClient in Thread: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFromUDP(DatagramSocket socket) throws IOException {
        System.out.println("Bank listening on address " + socket.getLocalAddress() + ":" + socket.getLocalPort() + "\n");
        String message = receiveMessageFromSocket(socket);
        incrementTotalPackageReceived();
        String[] parts = message.split(",");

        String abbreviation = parts[0];
        double newPrice = Double.parseDouble(parts[1]);
        updateSecurityPrice(abbreviation, newPrice);
        printPortfolio();
    }

    private static InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    private static String getThisBankPortUDP() {
        return System.getenv("THIS_BANK_PORT_UDP");
    }
    private static String getThisBankPortTCP() {
        return System.getenv("THIS_BANK_PORT_TCP");
    }

    public static void main(String[] args) {
        Bank bank = new Bank("Bank of America");
        bank.addSecurity("AAPL", 100, 100);
        bank.addSecurity("GOOG", 200, 200);
        bank.addSecurity("MSFT", 300, 300);
        bank.addSecurity("AMZN", 400, 400);
        bank.addSecurity("FB", 500, 500);
        bank.run();
    }

    private String receiveMessageFromSocket(DatagramSocket socket) throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        System.out.println("Received update from " + packet.getAddress() + ":" + packet.getPort());
        String message = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Received update: " + message);
        return message;
    }

    private void incrementTotalPackageReceived() {
        totalPackageReceived++;
        System.out.println("Total package received: " + totalPackageReceived + '\n');
    }
}

