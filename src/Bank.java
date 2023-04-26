import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

/**
 * class Bank
 *
 */
public class Bank implements Runnable{
    private final String name;
    private final int ID;
    private double accountBalance;
    private HashMap<Stock, Double> stocks;
    private DatagramSocket datagramSocket;
    private static int port = 8080;
    private void addDefaultStocks(){
        this.stocks.put(new Stock("SGS", 35, 140), 12000.0);
        this.stocks.put(new Stock("SAS", 36, 172), 12000.0);
    }
    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
    public Bank(String name, int ID) {
        this.name = name;
        this.ID = ID;
        this.stocks = new HashMap<>();
        try {
            this.datagramSocket = new DatagramSocket(port++, InetAddress.getByName("localhost"));
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        addDefaultStocks();
        adjustAccountBalance();
    }
    public void printAccountBalance(){
        System.out.println("Name of Bank: " + this.name);
        System.out.println("Bank ID: " + this.ID);
        System.out.println("Bank balance: " + this.accountBalance);
        System.out.println("===========================================");

    }
    public void adjustAccountBalance(){
        this.accountBalance = calculateTotalBalance();
    }
    private double calculateTotalBalance(){
        double accountBalance = 0;
        for (Map.Entry<Stock, Double> entry : stocks.entrySet()){
            Stock stock = entry.getKey();
            double stockValue = stock.getValue();
            double amount = entry.getValue();
            accountBalance += totalStockWorth(stockValue,amount);
        }
        return accountBalance;
    }

    private double totalStockWorth(double stockValue, double amount){
        return stockValue * amount;
    }
    public void addStock(Stock stock, double amount){
        this.stocks.put(stock, amount);
        adjustAccountBalance();
    }

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("Receiving from port " + datagramSocket.getPort());
                datagramSocket.receive(new DatagramPacket(new byte[1024], 1024));
                System.out.println(datagramSocket.getPort());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
