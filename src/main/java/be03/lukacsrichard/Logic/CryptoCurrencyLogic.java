package be03.lukacsrichard.Logic;

import be03.lukacsrichard.Model.CryptoCurrency;
import be03.lukacsrichard.Repository.CryptoCurrencyRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CryptoCurrencyLogic {

    public static CryptoCurrency getData(String cryptoSymbol) {
        CryptoCurrency cryptoCurrency = CryptoCurrencyPriceFetcher.getLast365DaysClosingPrices(cryptoSymbol);
        
        CryptoCurrencyRepository cryptoCurrencyRepository = new CryptoCurrencyRepository();
        
        CryptoCurrency existingCryptoCurrency = cryptoCurrencyRepository.GetBySymbol(cryptoSymbol);
        
        if (existingCryptoCurrency == null) {
            cryptoCurrencyRepository.Insert(cryptoCurrency);
            System.out.println("Cryptocurrency inserted successfully.");
        } else {
            cryptoCurrencyRepository.Update(cryptoSymbol, cryptoCurrency);
            System.out.println("Cryptocurrency updated successfully.");
        }
        cryptoCurrencyRepository.InsertCryptoPrices(cryptoSymbol, cryptoCurrency.getDates(), cryptoCurrency.getClosingPrices());
        return cryptoCurrency;
    }

    public static void writeCryptoCurrencyToFile(CryptoCurrency cryptoCurrency) {
        String symbol = cryptoCurrency.getSymbol();
        File file = new File(symbol + ".txt");
        
        if (file.exists()) {
            if (!file.delete()) {
                System.err.println("Failed to delete the existing file: " + file.getAbsolutePath());
                return;
            }
        }
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Symbol: " + cryptoCurrency.getSymbol() + "\n");
            writer.write("Current Price: " + cryptoCurrency.getCurrentPrice() + "\n");
            writer.write("52 Week Min: " + cryptoCurrency.getFiftyTwoWeekMin() + "\n");
            writer.write("52 Week Max: " + cryptoCurrency.getFiftyTwoWeekMax() + "\n");
            writer.write("RSI: " + cryptoCurrency.getRSI() + "\n");
            writer.write("Long Name: " + cryptoCurrency.getLongName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to write cryptocurrency data to file: " + e.getMessage());
        }
    }
}
