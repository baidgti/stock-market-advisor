package be03.lukacsrichard.Logic;

import be03.lukacsrichard.Model.Stock;
import be03.lukacsrichard.Repository.StockRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StockLogic {

    public static Stock getData(String stockSymbol) {
        Stock stock = StockPriceFetcher.getLast365DaysClosingPrices(stockSymbol);
        
        StockRepository stockRepository = new StockRepository();
        
        Stock existingStock = stockRepository.GetBySymbol(stockSymbol);
        
        if (existingStock == null) {
            stockRepository.Insert(stock);
            System.out.println("Stock inserted successfully.");
        } else {
            stockRepository.Update(stockSymbol, stock);
            System.out.println("Stock updated successfully.");
        }
        stockRepository.InsertStockPrices(stockSymbol, stock.getDates(), stock.getClosingPrices());
        return stock;
    }

    public static void writeStockToFile(Stock stock) {
        String symbol = stock.getSymbol();
        File file = new File(symbol + ".txt");

        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Failed to delete the existing file: " + file.getAbsolutePath());
                return;
            }
        }
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Symbol: " + stock.getSymbol() + "\n");
            writer.write("Current Price: " + stock.getCurrentPrice() + "\n");
            writer.write("52 Week Min: " + stock.getFiftyTwoWeekMin() + "\n");
            writer.write("52 Week Max: " + stock.getFiftyTwoWeekMax() + "\n");
            writer.write("RSI: " + stock.getRSI() + "\n");
            writer.write("Market Cap: " + stock.getMarketCap() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write stock data to file: " + e.getMessage());
        }
    }
}
