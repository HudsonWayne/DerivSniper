package com.myproject.deriv;

public class TickData {
    private final String symbol;
    private final long epoch;
    private final double price;

    public TickData(String symbol, long epoch, double price) {
        this.symbol = symbol;
        this.epoch = epoch;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public long getEpoch() { return epoch; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return symbol + "," + epoch + "," + price;
    }
}
