package be03.lukacsrichard;

import be03.lukacsrichard.Exceptions.WrongInputException;
import be03.lukacsrichard.Repository.CryptoCurrencyRepository;
import be03.lukacsrichard.Repository.StockRepository;
import be03.lukacsrichard.UI.App;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AppTest {

    private ByteArrayOutputStream outContent;
    private ByteArrayInputStream inContent;

    @Before
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testStockAdviceLowestRSI() throws WrongInputException {
        String input = "1\n1\n"; // Simulates user selecting stock advice (1) and stock RSI options (1)
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        StockRepository stockRepository = mock(StockRepository.class);
        Map.Entry<String, Double> lowestRSIStock = new AbstractMap.SimpleEntry<>("AAPL", 30.0);
        when(stockRepository.getStockWithLowestRSI()).thenReturn(lowestRSIStock);

        App.userInput();

        String output = outContent.toString();
        assertTrue(output.contains("Vásárlásra ajánlott részvény:"));
        assertTrue(output.contains("AAPL"));
        assertTrue(output.contains("RSI: 30.0"));
    }

    @Test
    public void testCryptoAdviceHighestRSI() throws WrongInputException {
        String input = "2\n1\n"; // Simulates user selecting cryptocurrency advice (2) and crypto RSI options (1)
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        CryptoCurrencyRepository cryptoCurrencyRepository = mock(CryptoCurrencyRepository.class);
        Map.Entry<String, Double> highestRSICrypto = new AbstractMap.SimpleEntry<>("BTC-USD", 70.0);
        when(cryptoCurrencyRepository.getCryptoWithHighestRSI()).thenReturn(highestRSICrypto);

        App.userInput();

        String output = outContent.toString();
        assertTrue(output.contains("Eladásra ajánlott kriptovaluta:"));
        assertTrue(output.contains("BTC-USD"));
        assertTrue(output.contains("RSI: 70.0"));
    }

    @Test(expected = WrongInputException.class)
    public void testWrongInput() throws WrongInputException {
        String input = "abc\n";
        inContent = new ByteArrayInputStream(input.getBytes());
        System.setIn(inContent);

        App.userInput();
    }
}
