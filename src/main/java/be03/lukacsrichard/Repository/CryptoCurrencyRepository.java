package be03.lukacsrichard.Repository;

import be03.lukacsrichard.Model.CryptoCurrency;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class CryptoCurrencyRepository extends MainRepository<CryptoCurrency> {

    @Override
    public void Insert(CryptoCurrency object) {
        System.out.println("Inserting cryptocurrency: " + object.getSymbol());
        String sql = "INSERT INTO dbo.CryptoCurrency (Symbol, CurrentPrice, FiftyTwoWeekMin, FiftyTwoWeekMax, RSI, LongName) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, object.getSymbol());
            preparedStatement.setDouble(2, object.getCurrentPrice());
            preparedStatement.setDouble(3, object.getFiftyTwoWeekMin());
            preparedStatement.setDouble(4, object.getFiftyTwoWeekMax());
            preparedStatement.setDouble(5, object.getRSI());
            preparedStatement.setString(6, object.getLongName());

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Update(String symbol, CryptoCurrency object) {
        String sql = "UPDATE dbo.CryptoCurrency SET CurrentPrice = ?, FiftyTwoWeekMin = ?, FiftyTwoWeekMax = ?, RSI = ?, LongName = ? WHERE Symbol = ?";
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, object.getCurrentPrice());
            preparedStatement.setDouble(2, object.getFiftyTwoWeekMin());
            preparedStatement.setDouble(3, object.getFiftyTwoWeekMax());
            preparedStatement.setDouble(4, object.getRSI());
            preparedStatement.setString(5, object.getLongName());
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
    public void Delete(String symbol) {
        String sql = "DELETE FROM dbo.CryptoCurrency WHERE Symbol = ?";
        try (Connection connection = this.databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Delete successful. Rows affected: " + rowsAffected);
            } else {
                System.out.println("No rows were deleted.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CryptoCurrency> GetAll() {
        String sql = "SELECT * FROM dbo.CryptoCurrency";
        List<CryptoCurrency> cryptocurrencies = new ArrayList<>();
        try (Connection connection = this.databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String symbol = resultSet.getString("Symbol");
                double fiftyTwoWeekMin = resultSet.getDouble("FiftyTwoWeekMin");
                double fiftyTwoWeekMax = resultSet.getDouble("FiftyTwoWeekMax");
                double rSI = resultSet.getDouble("RSI");
                String longName = resultSet.getString("LongName");
                double currentPrice = resultSet.getDouble("CurrentPrice");

                CryptoCurrency cryptoCurrency = new CryptoCurrency(symbol, new String[0], new double[0], fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, currentPrice, longName);
                cryptocurrencies.add(cryptoCurrency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cryptocurrencies;
    }

    @Override
    public CryptoCurrency GetBySymbol(String symbol) {
        CryptoCurrency cryptoCurrency = null;
        String sql = "SELECT * FROM dbo.CryptoCurrency WHERE Symbol = ?";
        try (Connection connection = this.databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, symbol);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedSymbol = resultSet.getString("Symbol");
                    double fiftyTwoWeekMin = resultSet.getDouble("FiftyTwoWeekMin");
                    double fiftyTwoWeekMax = resultSet.getDouble("FiftyTwoWeekMax");
                    double rSI = resultSet.getDouble("RSI");
                    String longName = resultSet.getString("LongName");
                    double currentPrice = resultSet.getDouble("CurrentPrice");
                    cryptoCurrency = new CryptoCurrency(retrievedSymbol, new String[0], new double[0], fiftyTwoWeekMin, fiftyTwoWeekMax, rSI, currentPrice, longName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cryptoCurrency;
    }

    public void InsertCryptoPrices(String symbol, String[] dates, double[] closingPrices) {
        String selectSql = "SELECT COUNT(*) FROM dbo.CryptoCurrencyPrices WHERE Symbol = ? AND Date = ?";
        String insertSql = "INSERT INTO dbo.CryptoCurrencyPrices (Symbol, Date, Price) VALUES (?, ?, ?)";
        String updateSql = "UPDATE dbo.CryptoCurrencyPrices SET Price = ? WHERE Symbol = ? AND Date = ?";
    
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

    public Map.Entry<String, Double> getCryptoWithHighestRSI() {
        String sql = "SELECT TOP 1 Symbol, RSI FROM dbo.CryptoCurrency ORDER BY RSI DESC;";
        return executeRSIQuery(sql);
    }

    public Map.Entry<String, Double> getCryptoWithLowestRSI() {
        String sql = "SELECT TOP 1 Symbol, RSI FROM dbo.CryptoCurrency ORDER BY RSI ASC;";
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
