import java.lang.reflect.Array;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class StockExchange {
    private String name;
    private int ID;
    private ArrayList<Stock> availableStocks;
    private DatagramSocket datagramSocket;

    public StockExchange(String name, int ID) {
        this.name = name;
        this.ID = ID;
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
}
