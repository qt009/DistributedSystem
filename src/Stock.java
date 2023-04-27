import java.io.Serializable;

public class Stock implements Serializable {
    private String abbreviation;
    private double price;

    public Stock(String abbreviation, double price) {
        this.abbreviation = abbreviation;
        this.price = price;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
