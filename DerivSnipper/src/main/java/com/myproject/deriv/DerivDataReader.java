package com.myproject.deriv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java-websocket.client.WebSocketClient;
import org.java-websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Map;

public class DerivDataReader implements Runnable {
    private final StrategyEngine strategyEngine;
    private final String appId;
    private final String symbol;

    public DerivDataReader(StrategyEngine strategyEngine, String appId, String symbol) {
        this.strategyEngine = strategyEngine;
        this.appId = appId;
        this.symbol = symbol;
    }

    @Override
    public void run() {
        try {
            String url = "wss://ws.derivws.com/websockets/v3?app_id=" + appId;
            WebSocketClient client = new WebSocketClient(new URI(url)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to Deriv WebSocket API");
                    String subscribeMessage = String.format("{\"ticks\": [\"%s\"]}", symbol);
                    send(subscribeMessage);
                }

                @Override
                public void onMessage(String message) {
                    processMessage(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from WebSocket API");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            client.connectBlocking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(message);
            JsonNode tickNode = rootNode.path("tick");

            if (!tickNode.isMissingNode()) {
                String symbol = tickNode.path("symbol").asText();
                long epoch = tickNode.path("epoch").asLong();
                double price = tickNode.path("quote").asDouble();

                TickData tickData = new TickData(symbol, epoch, price);
                strategyEngine.processTick(tickData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
