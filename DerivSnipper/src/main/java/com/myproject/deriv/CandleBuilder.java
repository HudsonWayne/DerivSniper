package com.myproject.deriv;

import java.util.ArrayList;
import java.util.List;

public class CandleBuilder {
    private final long intervalMs;
    private Candle current = null;
    private final List<Candle> completed = new ArrayList<>();

    public CandleBuilder(long intervalSec) {
        this.intervalMs = intervalSec * 1000;
    }

    public List<Candle> onTick(long epochMs, double price) {
        long bucket = (epochMs / intervalMs) * intervalMs;
        if (current == null || current.startEpoch != bucket) {
            if (current != null) completed.add(current);
            current = new Candle(bucket, price);
        } else {
            current.update(price);
        }
        List<Candle> out = new ArrayList<>(completed);
        completed.clear();
        return out;
    }
}
