package StockExchange.src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import Stock.Stock;
public class StockExchange implements Runnable{
    private final Stock[] stocks;

    private int totalPackageSent = 0;
    Map<String, String> bankMap ;

    public StockExchange(Stock[] stocks) {
        this.stocks = stocks;
        this.bankMap = setBankMapByEnvVariable();
    }

    public void sendUpdates() {
        Random rand = new Random();
        byte[] buffer = new byte[1024];
        try (DatagramSocket socket = new DatagramSocket()){

            while (true) {
                int index = rand.nextInt(stocks.length);
                Stock stock = stocks[index];
                double newPrice = generateNewPrice(stock.getPrice());
                stock.setPrice(newPrice);

                for (String bankIp : bankMap.keySet()) {
                    InetAddress address = InetAddress.getByName(bankIp);
                    int port = Integer.parseInt(bankMap.get(bankIp));
                    String message = stock.getAbbreviation() + "," + newPrice;
                    DatagramPacket update = new DatagramPacket(message.getBytes(), message.length(), address, port);
                    socket.send(update);
                    System.out.println("Sent update for " + stock.getAbbreviation() + " to " + address + ":" + update.getPort() + "\n");
                    incrementTotalPackageSent();
                }
                Thread.sleep(15000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double generateNewPrice(double currentPrice) {
        Random rand = new Random();
        double volatility = 0.2; // assuming 20% volatility
        double changePercent = volatility * rand.nextDouble();
        boolean positive = rand.nextBoolean();

        if (!positive) {
            changePercent *= -1;
        }

        double changeAmount = currentPrice * changePercent;
        return currentPrice + changeAmount;
    }

    private void incrementTotalPackageSent() {
        totalPackageSent++;
        System.out.println("Total package sent: " + totalPackageSent + '\n');
    }

    private Map<String, String> setBankMapByEnvVariable(){
        Map<String, String> result = new HashMap<>();
        Map<String, String> envMap = System.getenv();
        for (String key : envMap.keySet()) {
            if (key.startsWith("BANK_IP_")) {
                String bankNum = key.substring(8);
                String bankIp = envMap.get(key);
                String bankPort = envMap.get("BANK_PORT_" + bankNum);
                result.put(bankIp, bankPort);
            }
        }
        return result;
    }
    public static void main(String[] args) {
        Stock[] stocks = new Stock[5];
        stocks[0] = new Stock("AAPL", 100);
        stocks[1] = new Stock("GOOG", 200);
        stocks[2] = new Stock("MSFT", 300);
        stocks[3] = new Stock("AMZN", 400);
        stocks[4] = new Stock("FB", 500);

        StockExchange exchange = new StockExchange(stocks);
        exchange.run();
    }

    @Override
    public void run() {
        sendUpdates();
    }
}
