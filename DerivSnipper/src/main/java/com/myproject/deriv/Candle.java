package com.myproject.deriv;

public class Candle {
    public final long startEpoch;
    public double open, high, low, close;

    public Candle(long startEpoch, double open) {
        this.startEpoch = startEpoch;
        this.open = this.high = this.low = this.close = open;
    }

    public void update(double price) {
        if (price > high) high = price;
        if (price < low) low = price;
        close = price;
    }
}
