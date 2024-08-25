package be03.lukacsrichard.Logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import be03.lukacsrichard.Model.CryptoCurrency;

public class CryptoCurrencyPriceFetcher {

    public static CryptoCurrency getLast365DaysClosingPrices(String stockSymbol) {
        String urlString = "https://query1.finance.yahoo.com/v7/finance/chart/" + stockSymbol + "?range=1y&interval=1d";
        
        List<String> dates = new ArrayList<>();
        List<Double> closingPrices = new ArrayList<>();
        String longName = null;

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
            
            // Fetch the longName from the metadata
            JSONObject metaData = result.getJSONObject("meta");
            longName = metaData.getString("longName");
    
            // Extract timestamps and closing prices
            JSONArray timestamps = result.getJSONArray("timestamp");
            JSONArray closingPricesArray = result.getJSONObject("indicators").getJSONArray("quote").getJSONObject(0).getJSONArray("close");
    
            for (int i = 0; i < timestamps.length(); i++) {
                long timestamp = timestamps.getLong(i);
                double closePrice = closingPricesArray.isNull(i) ? 0.0 : closingPricesArray.getDouble(i);
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd")
                        .format(new java.util.Date(timestamp * 1000L));
                dates.add(date);
                closingPrices.add(closePrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        // Convert lists to arrays
        String[] datesArray = dates.toArray(new String[0]);
        double[] closingPricesArray = closingPrices.stream().mapToDouble(Double::doubleValue).toArray();
    
        // Calculate 52-week min and max
        double fiftyTwoWeekMin = Double.MAX_VALUE;
        double fiftyTwoWeekMax = Double.MIN_VALUE;
        for (double price : closingPricesArray) {
            if (price < fiftyTwoWeekMin) {
                fiftyTwoWeekMin = price;
            }
            if (price > fiftyTwoWeekMax) {
                fiftyTwoWeekMax = price;
            }
        }
    
        double rSI = rSICalculator.calculateRSI(closingPricesArray);
        double currentPrice = closingPricesArray[closingPricesArray.length - 1];
    
        // Return the CryptoCurrency object with the longName
        return new CryptoCurrency(stockSymbol, datesArray, closingPricesArray, fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, currentPrice, longName);
    }
}
