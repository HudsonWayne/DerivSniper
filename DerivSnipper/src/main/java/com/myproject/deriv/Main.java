package com.myproject.deriv;

public class Main {
    public static void main(String[] args) {
        String appId = "YOUR_DERIV_APP_ID"; // Replace with your Deriv App ID
        String symbol = "R_100"; // Replace with your desired volatility index symbol

        StrategyEngine engine = new StrategyEngine();
        DerivDataReader dataReader = new DerivDataReader(engine, appId, symbol);

        new Thread(dataReader).start();
    }
}
