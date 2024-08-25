package be03.lukacsrichard.Logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import be03.lukacsrichard.Model.Stock;

public class StockPriceFetcher {

    public static Stock getLast365DaysClosingPrices(String stockSymbol) {
        String urlString = "https://query1.finance.yahoo.com/v7/finance/chart/" + stockSymbol + "?range=1y&interval=1d";
        
        String[] dates = new String[365];
        double[] closingPrices = new double[365];
        int actualSize = 0;
        
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
    
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
    
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
    
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject result = jsonResponse.getJSONObject("chart").getJSONArray("result").getJSONObject(0);
    
            // Extract timestamps and closing prices
            JSONArray timestamps = result.getJSONArray("timestamp");
            JSONArray closingPricesArray = result.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close");
    
            actualSize = timestamps.length(); // Update actual size based on data received
            for (int i = 0; i < actualSize; i++) {
                long timestamp = timestamps.getLong(i);
                double closePrice = closingPricesArray.getDouble(i);
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .format(new java.util.Date(timestamp * 1000L));
                dates[i] = date;
                closingPrices[i] = closePrice;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        // Trim the arrays to actual size
        dates = java.util.Arrays.copyOf(dates, actualSize);
        closingPrices = java.util.Arrays.copyOf(closingPrices, actualSize);
    
        // Calculate 52-week min and max
        double fiftyTwoWeekMin = Double.MAX_VALUE;
        double fiftyTwoWeekMax = Double.MIN_VALUE;
        for (double price : closingPrices) {
            if (price < fiftyTwoWeekMin) {
                fiftyTwoWeekMin = price;
            }
            if (price > fiftyTwoWeekMax) {
                fiftyTwoWeekMax = price;
            }
        }
    
        double rSI = rSICalculator.calculateRSI(closingPrices);
        double currentPrice = closingPrices[closingPrices.length - 1];
    
        return new Stock(stockSymbol, dates, closingPrices, fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, 0, currentPrice);
    }
}
