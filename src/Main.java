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

        DeutscheBank.printAccountBalance();
        CommerzBank.printAccountBalance();
        Sparkasse.printAccountBalance();

    }
}