import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;


public class Bank implements Serializable, Runnable {
    private final String name;
    private final Map<Stock, Integer> securities; // Map of securities held by the bank
    private DatagramSocket socket;

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
        while (true){
            try (DatagramSocket socket = new DatagramSocket(9000, InetAddress.getLocalHost())){
                System.out.println("Bank listening on address " + socket.getLocalAddress() + ":" + socket.getLocalPort() + "\n");

                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                System.out.println("Waiting for update...");
                socket.receive(packet);
                System.out.println("Received update from " + packet.getAddress() + ":" + packet.getPort());
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Received update: " + message);
                String[] parts = message.split(",");
                String abbreviation = parts[0];

                double newPrice = Double.parseDouble(parts[1]);
                updateSecurityPrice(abbreviation, newPrice);
                printPortfolio();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
}

