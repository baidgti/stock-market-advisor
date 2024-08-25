package be03.lukacsrichard.Logic;

public class rSICalculator {

    public static double calculateRSI(double[] closingPrices) {
        int period = 14;  // Default RSI calculation period is 14 days

        double[] dailyChanges = new double[closingPrices.length - 1];
        for (int i = 1; i < closingPrices.length; i++) {
            dailyChanges[i - 1] = closingPrices[i] - closingPrices[i - 1];
        }

        double gainSum = 0;
        double lossSum = 0;
        for (int i = 0; i < period; i++) {
            if (dailyChanges[i] > 0) {
                gainSum += dailyChanges[i];
            } else {
                lossSum -= dailyChanges[i];
            }
        }

        double averageGain = gainSum / period;
        double averageLoss = lossSum / period;

        double rs = averageGain / averageLoss;
        double rSI = 100 - (100 / (1 + rs));

        return rSI;
    }
}
