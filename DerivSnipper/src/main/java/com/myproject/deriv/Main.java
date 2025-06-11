package com.myproject.deriv;

public class Main {
    public static void main(String[] args) {
        StrategyEngine engine = new StrategyEngine();
        new Thread(new DerivDataReader(engine)).start();
    }
}
