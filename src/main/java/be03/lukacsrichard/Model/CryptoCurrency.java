package be03.lukacsrichard.Model;

public class CryptoCurrency extends FinancialAsset{
    private String longName;

    public CryptoCurrency(String symbol, String[] dates, double[] closingPrices, double fiftyTwoWeekMin,
            double fiftyTwoWeekMax, double rSI, double currentPrice, String longName) {
        super(symbol, dates, closingPrices, fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, currentPrice);
        this.longName = longName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
}
