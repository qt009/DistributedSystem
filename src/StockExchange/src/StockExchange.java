import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class StockExchange implements Runnable{
    private final Stock[] stocks;

    public StockExchange(Stock[] stocks) {
        this.stocks = stocks;
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

                String message = stock.getAbbreviation() + "," + newPrice;
                System.out.println("Sending update: " + message);
                InetAddress address = InetAddress.getByAddress(new byte[]{(byte) 172, (byte) 20, (byte) 0, (byte) 3});
                DatagramPacket update = new DatagramPacket(message.getBytes(), message.length(), address, 9000);
                System.out.println("message of packet: " + new String(update.getData(), 0, update.getLength()));
                socket.send(update);
                System.out.println("Sent update for " + stock.getAbbreviation() + " to " + address + ":" + update.getPort() + "\n");

                Thread.sleep(3000);
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
