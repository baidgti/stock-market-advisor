package be03.lukacsrichard.Model;

public abstract class FinancialAsset {
    private String symbol;
    private String[] dates;
    private double[] closingPrices;
    private double fiftyTwoWeekMin;
    private double fiftyTwoWeekMax;
    private double rSI;
    private double currentPrice;

    public FinancialAsset(String symbol, String[] dates, double[] closingPrices, double fiftyTwoWeekMin,
                          double fiftyTwoWeekMax, double rSI, double currentPrice) {
        this.symbol = symbol;
        this.dates = dates;
        this.closingPrices = closingPrices;
        this.fiftyTwoWeekMin = fiftyTwoWeekMin;
        this.fiftyTwoWeekMax = fiftyTwoWeekMax;
        this.rSI = rSI;
        this.currentPrice = currentPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String[] getDates() {
        return dates;
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }

    public double[] getClosingPrices() {
        return closingPrices;
    }

    public void setClosingPrices(double[] closingPrices) {
        this.closingPrices = closingPrices;
    }

    public double getFiftyTwoWeekMin() {
        return fiftyTwoWeekMin;
    }

    public void setFiftyTwoWeekMin(double fiftyTwoWeekMin) {
        this.fiftyTwoWeekMin = fiftyTwoWeekMin;
    }

    public double getFiftyTwoWeekMax() {
        return fiftyTwoWeekMax;
    }

    public void setFiftyTwoWeekMax(double fiftyTwoWeekMax) {
        this.fiftyTwoWeekMax = fiftyTwoWeekMax;
    }

    public double getRSI() {
        return rSI;
    }

    public void setRSI(double rSI) {
        this.rSI = rSI;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
