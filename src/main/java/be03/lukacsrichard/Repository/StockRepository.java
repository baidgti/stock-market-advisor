package be03.lukacsrichard.Repository;

import be03.lukacsrichard.Model.Stock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.List;

public class StockRepository extends MainRepository<Stock> {

    @Override
    public void Insert(Stock object) {
        System.out.println("Inserting stock: " + object.getSymbol());
        String sql = "INSERT INTO dbo.Stock (Symbol, CurrentPrice, FiftyTwoWeekMin, FiftyTwoWeekMax, RSI, MarketCap) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, object.getSymbol());
            preparedStatement.setDouble(2, object.getCurrentPrice());
            preparedStatement.setDouble(3, object.getFiftyTwoWeekMin());
            preparedStatement.setDouble(4, object.getFiftyTwoWeekMax());
            preparedStatement.setDouble(5, object.getRSI());
            preparedStatement.setLong(6, object.getMarketCap());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Update(String symbol, Stock object) {
        String sql = "UPDATE dbo.Stock SET CurrentPrice = ?, FiftyTwoWeekMin = ?, FiftyTwoWeekMax = ?, RSI = ?, MarketCap = ? WHERE Symbol = ?";
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, object.getCurrentPrice());
            preparedStatement.setDouble(2, object.getFiftyTwoWeekMin());
            preparedStatement.setDouble(3, object.getFiftyTwoWeekMax());
            preparedStatement.setDouble(4, object.getRSI());
            preparedStatement.setLong(5, object.getMarketCap());
            preparedStatement.setString(6, symbol);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Update successful.");
            } else {
                System.out.println("No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Delete(String date) {
        throw new UnsupportedOperationException("Unimplemented method 'Delete'");
    }

    @Override
    public List<Stock> GetAll() {
        throw new UnsupportedOperationException("Unimplemented method 'GetAll'");
    }

    @Override
    public Stock GetBySymbol(String symbol) {
        Stock stock = null;
        String sql = "SELECT * FROM dbo.Stock WHERE Symbol = ?";
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedSymbol = resultSet.getString("Symbol");
                    double fiftyTwoWeekMin = resultSet.getDouble("FiftyTwoWeekMin");
                    double fiftyTwoWeekMax = resultSet.getDouble("FiftyTwoWeekMax");
                    double rSI = resultSet.getDouble("RSI");
                    long marketCap = resultSet.getLong("MarketCap");
                    double currentPrice = resultSet.getDouble("CurrentPrice");
                    stock = new Stock(retrievedSymbol, new String[0], new double[0], fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, marketCap, currentPrice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void InsertStockPrices(String symbol, String[] dates, double[] closingPrices) {
        String selectSql = "SELECT COUNT(*) FROM dbo.StockPrices WHERE Symbol = ? AND Date = ?";
        String insertSql = "INSERT INTO dbo.StockPrices (Symbol, Date, Price) VALUES (?, ?, ?)";
        String updateSql = "UPDATE dbo.StockPrices SET Price = ? WHERE Symbol = ? AND Date = ?";
    
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement insertStmt = connection.prepareStatement(insertSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
    
            for (int i = 0; i < dates.length; i++) {
                // Check if the record exists
                selectStmt.setString(1, symbol);
                selectStmt.setString(2, dates[i]);
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            // Record exists, update it
                            updateStmt.setDouble(1, closingPrices[i]);
                            updateStmt.setString(2, symbol);
                            updateStmt.setString(3, dates[i]);
                            updateStmt.executeUpdate();
                        } else {
                            // Record does not exist, insert it
                            insertStmt.setString(1, symbol);
                            insertStmt.setString(2, dates[i]);
                            insertStmt.setDouble(3, closingPrices[i]);
                            insertStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map.Entry<String, Double> getStockWithHighestRSI() {
        String sql = "SELECT TOP 1 Symbol, RSI FROM dbo.Stock ORDER BY RSI DESC;";
        return executeRSIQuery(sql);
    }

    public Map.Entry<String, Double> getStockWithLowestRSI() {
        String sql = "SELECT TOP 1 Symbol, RSI FROM dbo.Stock ORDER BY RSI ASC;";
        return executeRSIQuery(sql);
    }

    private Map.Entry<String, Double> executeRSIQuery(String sql) {
        Map.Entry<String, Double> result = null;
        try {
            this.databaseConnection.reopenConnection(); // Ensure the connection is fresh
            try (Connection connection = this.databaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    String symbol = resultSet.getString("Symbol");
                    double rsi = resultSet.getDouble("RSI");
                    result = new AbstractMap.SimpleEntry<>(symbol, rsi);
                }

            } finally {
                this.databaseConnection.closeConnection(); // Close the connection after use
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
