package com.myproject.deriv;

import java.util.ArrayList;
import java.util.List;

public class StrategyEngine {
    private final CandleBuilder cb = new CandleBuilder(60); // 1-minute candles
    private final List<Candle> candles = new ArrayList<>();

    public void onTick(TickData tick) {
        List<Candle> newCandles = cb.onTick(tick.getEpoch() * 1000, tick.getPrice());
        newCandles.forEach(c -> {
            candles.add(c);
            evaluateCRT();
        });
    }

    private void evaluateCRT() {
        int s = candles.size();
        if (s < 3) return;

        Candle c1 = candles.get(s - 3);
        Candle c2 = candles.get(s - 2);
        Candle c3 = candles.get(s - 1);

        boolean phase2 = (c2.high > c1.high && c2.close < c1.high)
                      || (c2.low < c1.low && c2.close > c1.low);

        boolean phase3 = (c3.close > c1.high && c2.high > c1.high && c2.close < c1.high)
                       || (c3.close < c1.low && c2.low < c1.low && c2.close > c1.low);

        if (phase2 && phase3) {
            if (c3.close > c1.high) {
                System.out.println("💡 BUY Signal at candle " + c3.startEpoch + " (>" + c1.high + ")");
            } else {
                System.out.println("💡 SELL Signal at candle " + c3.startEpoch + " (<" + c1.low + ")");
            }
        }
    }
}
