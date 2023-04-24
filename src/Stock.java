public class Stock {
    private String name;
    private int ID;

    private double value;

    public Stock(String name, int ID, double value) {
        this.name = name;
        this.ID = ID;
        this.value = value;
    }

    public void setValue(double value) {
        this.value = value;
    }
    public Stock(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public double getValue() {
        return value;
    }
}
