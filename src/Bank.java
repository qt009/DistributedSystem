import java.util.HashMap;
import java.util.Map;

/**
 * class Bank
 * Serialization is the process of converting an object into a stream of bytes,
 */
public class Bank{
    private final String name;
    private final int ID;
    private double accountBalance;
    private HashMap<Stock, Double> stocks;
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
}
