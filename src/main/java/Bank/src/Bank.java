package Bank.src;

import Stock.Stock;
import Thrift.src.BankService;
import Thrift.src.LoanRequest;
import Thrift.src.LoanResponse;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Bank implements Runnable {
    private final String name;

    private double reserves;
    private double totalValue;

    private final Map<Stock, Integer> securities; // Map of securities held by the bank

    private BankTCP bankTCP;

    private int totalPackageReceived = 0;


    private boolean isBankrupt;
    private boolean isBankValueUpdated = false;

    private BankThriftHandler bankThriftHandler;
    private BankMQTTHandler bankMQTTHandler;

    public Bank(String name) {
        this.name = name;
        securities = new HashMap<>();
        this.reserves = 1000000;
        this.isBankrupt = false;
        setTotalValue();
    }

    public String getName() {
        return name;
    }

    public void addSecurity(String abbreviation, double price, int quantity) {
        Stock stock = new Stock(abbreviation, price);
        securities.put(stock, quantity);
    }

    public synchronized void updateSecurityPrice(String abbreviation, double newPrice) {
        for (Stock stock : securities.keySet()) {
            if (stock.getAbbreviation().equals(abbreviation)) {
                stock.setPrice(newPrice);
            }
        }
    }




    public synchronized double calculatePortfolioValue() {
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
        System.out.println("Total stock value: $" + calculatePortfolioValue());
    }

    @Override
    public void run() {
        runMQTTHandler();
        runThrift();
        runUDPSocket();
        runTCPSocket();
    }

    private void runMQTTHandler() {
        try {
            System.out.println("Constructing Bank MQTT Handler with IP: " + getThisBankIP() + " and Port: " + getThisBankPortThrift());
            bankMQTTHandler = new BankMQTTHandler("tcp://172.20.3.0:1883","bank_" + getThisBankIP() + ":" + getThisBankPortThrift(),this);
            bankMQTTHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runThrift() {
        try {
            System.out.println("Constructing Bank Thrift Handler");
            bankThriftHandler = new BankThriftHandler(this);
            bankThriftHandler.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runTCPSocket() {
        try {
            bankTCP = new BankTCP(Integer.parseInt(getThisBankPortTCP()));
            ServerSocket tcpServerSocket = bankTCP.getServerSocket();

            while (true) {
                Socket tcpSocket = tcpServerSocket.accept();
                System.out.println("TCP connection accepted from " + tcpSocket.getInetAddress() + ":" + tcpSocket.getPort());

                ClientHandler clientHandler = new ClientHandler(tcpSocket, this);
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

    private void receiveFromUDP(DatagramSocket socket) throws IOException {
        System.out.println("Bank listening on address " + socket.getLocalAddress() + ":" + socket.getLocalPort() + "\n");
        String message = receiveMessageFromSocket(socket);
        incrementTotalPackageReceived();
        String[] parts = message.split(",");

        String abbreviation = parts[0];
        double newPrice = Double.parseDouble(parts[1]);
        updateSecurityPrice(abbreviation, newPrice);
        setBankValueUpdated(true);
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
    public String getThisBankPortThrift(){
        return System.getenv("THIS_BANK_PORT_THRIFT");
    }
    public String getThisBankIP() throws UnknownHostException { return getInetAddress().getHostAddress();}

    public static void main(String[] args) {
        Bank bank = new Bank("Bank of America");
        bank.addSecurity("AAPL", 100, 100);
        bank.addSecurity("GOOG", 200, 200);
        bank.addSecurity("MSFT", 300, 300);
        bank.addSecurity("AMZN", 400, 400);
        bank.addSecurity("FB", 500, 500);
        System.out.printf("Bank %s has been created\n", bank.getName());
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

    private synchronized void incrementTotalPackageReceived() {
        totalPackageReceived++;
        System.out.println("Total package received: " + totalPackageReceived + '\n');
    }

    private synchronized void setTotalValue() {
        totalValue = reserves + calculatePortfolioValue();
    }

    public synchronized void addTotalValue(double change) {
        reserves += change;
        System.out.println("Reserves: " + reserves);
        setTotalValue();
    }

    public synchronized void subTotalValue(double change) {
        reserves -= change;
        setTotalValue();
    }

    public double getTotalValue() {
        return totalValue + calculatePortfolioValue();
    }

    public double getReserves() {
        return reserves;
    }
    public void setReserves(double reserves) {
        this.reserves = reserves;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }
    public boolean isBankrupt() {
        return isBankrupt;
    }

    public boolean isBankValueUpdated() {
        return isBankValueUpdated;
    }
    public void setBankValueUpdated(boolean bankValueUpdated) {
        isBankValueUpdated = bankValueUpdated;
    }
    public BankThriftHandler getBankThriftHandler() {
        return bankThriftHandler;
    }

    public void askForHelp() {
        System.out.println("SENDING HELPING REQUEST");
        for (Map.Entry<String, Integer> entry : bankThriftHandler.getFriendlyBanks().entrySet()) {
            String hostRpc = entry.getKey();
            int portRpc = entry.getValue();

            try{
                TTransport transport = new TSocket(hostRpc, portRpc);
                TProtocol protocol = new TBinaryProtocol(transport);

                BankService.Client client = new BankService.Client(protocol);
                transport.open();

                double value = 100000;
                LoanRequest request = new LoanRequest(value);
                LoanResponse response = client.processLoanRequest(request);

                System.out.println("Response: "+ response);
                if(response.equals(LoanResponse.APPROVED)){
                    System.out.println(hostRpc+ " success to rescue");
                    this.setReserves(getReserves() + value);
                    break;
                }
                else if(response.equals(LoanResponse.REJECTED)){
                    System.out.println(hostRpc+" fail to rescue");
                    //check if all friendly banks fail to rescue
                    //if so, this bank is bankrupt
                    Map.Entry<String, Integer> lastEntry = bankThriftHandler.getFriendlyBanks().entrySet().stream().reduce((one, two) -> two).get();
                    if(entry.equals(lastEntry)){
                        System.out.println("Bankrupt");
                        setBankrupt(true);
                    }

                }
                else {
                    System.out.println("Else case: "+ response);
                }
                transport.close();
            }
            catch (Exception e){
                System.out.println("Error");
                e.printStackTrace();
            }

        }

    }
}

