package be03.lukacsrichard.UI;
import be03.lukacsrichard.Exceptions.WrongInputException;
import be03.lukacsrichard.Logic.CryptoCurrencyLogic;
import be03.lukacsrichard.Logic.StockLogic;
import be03.lukacsrichard.Model.CryptoCurrency;
import be03.lukacsrichard.Model.Stock;
import be03.lukacsrichard.Repository.CryptoCurrencyRepository;
import be03.lukacsrichard.Repository.StockRepository;

import java.util.Map;
import java.util.Scanner;

public class App 
{
    public static void main(String[] args) throws WrongInputException {

        userInput();
    }

    public static void userInput() throws WrongInputException {
        StockRepository stockRepository = new StockRepository();
        CryptoCurrencyRepository cryptoCurrencyRepository = new CryptoCurrencyRepository();

        System.out.println("Kérlek válassz az alábbi menüpontok közül");
        System.out.println("1: Részvény lekérdezés");
        System.out.println("2: Kriptovaluta lekérdezés");
        Scanner consoleScanner = new Scanner(System.in);
        int userChoice = 0;
        try {
            userChoice = consoleScanner.nextInt();
            consoleScanner.nextLine(); // Consume the leftover newline
        } catch (NumberFormatException e) {
            consoleScanner.close();
            throw new WrongInputException("Érvénytelen bemenet. Kérlek számot adj meg.");
        }

        if (userChoice == 1) {
            System.out.println("Kérlek válassz az alábbi menüpontok közül");
            System.out.println("1: Részvény befektetés tanácsadás");
            System.out.println("2: Részvény adatainak lekérdezése");
            System.out.println("3: Részvény adatainak fájlba iratása");

            try {
                userChoice = consoleScanner.nextInt();
                consoleScanner.nextLine(); // Consume the leftover newline
            } catch (NumberFormatException e) {
                consoleScanner.close();
                throw new WrongInputException("Érvénytelen bemenet. Kérlek számot adj meg.");
            }

            if (userChoice == 1) {
                Map.Entry<String, Double> lowestRSIStock = stockRepository.getStockWithLowestRSI();
                if (lowestRSIStock != null) {
                    System.out.println("Vásárlásra ajánlott részvény:");
                    System.out.println(lowestRSIStock.getKey());
                    System.out.println("RSI: " + lowestRSIStock.getValue());
                }
                Map.Entry<String, Double> highestRSIStock = stockRepository.getStockWithHighestRSI();
                if (highestRSIStock != null) {
                    System.out.println("Eladásra ajánlott részvény:");
                    System.out.println(highestRSIStock.getKey());
                    System.out.println("RSI: " + highestRSIStock.getValue());
                }
                consoleScanner.close();
            } else if (userChoice == 2) {
                System.out.println("Kérlek add meg a részvény tőzsdei azonosítóját");
                System.out.println("Például: AAPL (Apple); TSLA (Tesla); MSFT (Microsoft)");
                String receivedSymbol = consoleScanner.nextLine();
                consoleScanner.close();
                Stock stock = StockLogic.getData(receivedSymbol);
                System.out.println("Jel: " + stock.getSymbol());
                System.out.println(String.format("Jelenlegi ár: %.2f$", stock.getCurrentPrice()));
                System.out.println(String.format("52 hetes minimum: %.2f$", stock.getFiftyTwoWeekMin()));
                System.out.println(String.format("52 hetes maximum: %.2f$", stock.getFiftyTwoWeekMax()));
                System.out.println(String.format("Relatív erősség index: %.2f", stock.getRSI()));
                System.out.println("Piaci kapitalizáció: " + stock.getMarketCap() + "$");
            } else if (userChoice == 3) {
                System.out.println("Kérlek add meg a részvény tőzsdei azonosítóját");
                String receivedSymbol = consoleScanner.nextLine();
                consoleScanner.close();
                Stock stock = stockRepository.GetBySymbol(receivedSymbol);
                StockLogic.writeStockToFile(stock);
            }

        } else if (userChoice == 2) {
            System.out.println("Kérlek válassz az alábbi menüpontok közül");
            System.out.println("1: Kriptovaluta befektetés tanácsadás");
            System.out.println("2: Kriptovaluta adatainak lekérdezése");
            System.out.println("3: Kriptovaluta adatainak fájlba iratása");

            try {
                userChoice = consoleScanner.nextInt();
                consoleScanner.nextLine(); // Consume the leftover newline
            } catch (NumberFormatException e) {
                consoleScanner.close();
                throw new WrongInputException("Érvénytelen bemenet. Kérlek számot adj meg.");
            }

            if (userChoice == 1) {
                Map.Entry<String, Double> lowestRSICrypto = cryptoCurrencyRepository.getCryptoWithLowestRSI();
                if (lowestRSICrypto != null) {
                    System.out.println("Vásárlásra ajánlott kriptovaluta:");
                    System.out.println(lowestRSICrypto.getKey());
                    System.out.println("RSI: " + lowestRSICrypto.getValue());
                }
                Map.Entry<String, Double> highestRSICrypto = cryptoCurrencyRepository.getCryptoWithHighestRSI();
                if (highestRSICrypto != null) {
                    System.out.println("Eladásra ajánlott kriptovaluta:");
                    System.out.println(highestRSICrypto.getKey());
                    System.out.println("RSI: " + highestRSICrypto.getValue());
                }
                consoleScanner.close();
            } else if (userChoice == 2) {
                System.out.println("Kérlek add meg a kriptovaluta tőzsdei azonosítóját");
                System.out.println("Például: BTC-USD (Bitcoin); ETH-USD (Ethereum); XRP-USD (Ripple)");
                String receivedSymbol = consoleScanner.nextLine();
                consoleScanner.close();
                CryptoCurrency cryptoCurrency = CryptoCurrencyLogic.getData(receivedSymbol);
                System.out.println("Név: " + cryptoCurrency.getLongName());
                System.out.println("Jel: " + cryptoCurrency.getSymbol());
                System.out.println(String.format("Jelenlegi ár: %.2f$", cryptoCurrency.getCurrentPrice()));
                System.out.println(String.format("52 hetes minimum: %.2f$", cryptoCurrency.getFiftyTwoWeekMin()));
                System.out.println(String.format("52 hetes maximum: %.2f$", cryptoCurrency.getFiftyTwoWeekMax()));
                System.out.println(String.format("Relatív erősség index: %.2f", cryptoCurrency.getRSI()));
            } else if (userChoice == 3) {
                System.out.println("Kérlek add meg a kriptovaluta tőzsdei azonosítóját");
                String receivedSymbol = consoleScanner.nextLine();
                consoleScanner.close();
                CryptoCurrency cryptoCurrency = cryptoCurrencyRepository.GetBySymbol(receivedSymbol);
                CryptoCurrencyLogic.writeCryptoCurrencyToFile(cryptoCurrency);
            }
        }
    }
}
