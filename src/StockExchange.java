import java.io.IOException;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;

public class StockExchange implements Runnable{
    private String name;
    private int ID;
    private ArrayList<Stock> availableStocks;
    private DatagramSocket datagramSocket;

    public StockExchange(String name, int ID) {
        this.name = name;
        this.ID = ID;
        try {
            this.datagramSocket = new DatagramSocket(9000, InetAddress.getByName("localhost"));
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        addDefaultStocks();
    }
    private void addDefaultStocks(){
        availableStocks.add(new Stock("HSA", 114, 234));
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public void run() {
        while(true){
            for (Stock stock : availableStocks) {
                stock.setValue(stock.getValue() + Math.random() * 10 - 5);
                try {
                    datagramSocket.send(new DatagramPacket(stock.toString().getBytes(), stock.toString().getBytes().length));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
