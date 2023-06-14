import Bank.src.Bank;
import org.junit.jupiter.api.Test;

public class BankDebug {
    public static void main(String[] args) {
        Bank bank = new Bank("Bank");

        bank.addSecurity("AAPL", 100, 100);
        bank.addSecurity("GOOG", 200, 200);
        bank.addSecurity("MSFT", 300, 300);
        bank.addSecurity("AMZN", 400, 400);
        bank.addSecurity("FB", 500, 500);
        bank.run();
    }
    @Test
    public void testBank(){
        Bank bank = new Bank("Bank");

        bank.addSecurity("AAPL", 100, 100);
        bank.addSecurity("GOOG", 200, 200);
        bank.addSecurity("MSFT", 300, 300);
        bank.addSecurity("AMZN", 400, 400);
        bank.addSecurity("FB", 500, 500);
        bank.run();
    }
}
