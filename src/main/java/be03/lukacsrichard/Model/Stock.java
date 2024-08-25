package be03.lukacsrichard.Model;

public class Stock extends FinancialAsset {
    private long marketCap;

    public Stock(String symbol, String[] dates, double[] closingPrices, double fiftyTwoWeekMin, double fiftyTwoWeekMax,
                 double rSI, long marketCap, double currentPrice) {
        super(symbol, dates, closingPrices, fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, currentPrice);
        this.marketCap = marketCap;
    }

    public long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }
}
