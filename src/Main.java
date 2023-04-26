public class Main {
    public static void main(String[] args) {
        Bank DeutscheBank = new Bank("Deutsche Bank",100);
        Bank CommerzBank = new Bank("CommerzBank",101);
        Bank Sparkasse = new Bank("Sparkasse",102);

        Stock Google =new Stock("GGL",10, 334);
        Stock Microsoft =new Stock("MSF",10, 412);

        Sparkasse.addStock(Google, 144);
        DeutscheBank.addStock(Google, 156);
        DeutscheBank.addStock(Microsoft, 12);

//        StockExchange stockExchange = new StockExchange("Stock Exchange", 1);
//        stockExchange.run();


        new Thread(DeutscheBank).start();
        new Thread(CommerzBank).start();
        new Thread(Sparkasse).start();
        //new Thread(stockExchange).start();





    }
}