# DerivSniper


Project Title: Real-time Deriv Volatility Trading System
Overview
This project provides a framework for students to connect to Deriv's real-time volatility indices via their WebSocket API, process the data, and implement a custom trading strategy. The system is designed with a hybrid architecture, leveraging Python for efficient real-time data streaming and Java for robust strategy execution and application logic.

Disclaimer: This project is for educational purposes only. It fetches real-time market data but does not execute actual trades on a live Deriv account. Any trading strategies implemented are purely for simulation and learning. Always exercise caution and understand the risks associated with financial markets.

Features
Real-time Data: Connects to Deriv's WebSocket API to receive live tick data for selected volatility indices.

Modular Design: Separate components for data fetching (Python) and strategy execution (Java).

Extensible Strategy: Easily implement and test your own trading strategies within the StrategyEngine.

Logging: Basic logging of data and strategy signals.

Prerequisites
Before you begin, ensure you have the following installed on your machine:

Java Development Kit (JDK) 11 or higher:

Download from Oracle JDK or OpenJDK.

Apache Maven:

Download and install from Maven Official Site.

Verify installation by running mvn -v in your terminal.

Python 3.8 or higher:

Download from Python Official Site.

Verify installation by running python --version or python3 --version in your terminal.

Python websocket-client library:

Install via pip:

pip install websocket-client

Deriv App ID:

Register a free developer account and create an application on the Deriv Developers website. You will get an app_id which is crucial for connecting to their API.

Setup Instructions
Clone or Download the Project:

If using Git: git clone <repository_url>

Otherwise, create the directory structure manually and place the files.

Configure Deriv App ID:

Open python_scripts/deriv_data_streamer.py.

Replace "YOUR_DERIV_APP_ID" with your actual app_id obtained from Deriv Developers.

Modify SYMBOL in deriv_data_streamer.py to the volatility index you want to track (e.g., 'R_100' for Volatility 100 Index, 'R_75' for Volatility 75 Index). You can find available symbols in the Deriv API documentation.

# python_scripts/deriv_data_streamer.py
APP_ID = "YOUR_DERIV_APP_ID" # <--- REPLACE THIS WITH YOUR APP ID
SYMBOL = "R_100" # <--- Change this to the desired Volatility Index (e.g., 'R_75', 'R_50')

Build the Java Project:

Navigate to the DerivTradingSystem/ directory (where pom.xml is located) in your terminal.

Run the Maven build command:

mvn clean install

This will compile the Java code and package it.

How to Run
Ensure all prerequisites are met and configurations are updated.

Open your terminal and navigate to the root directory of the project (DerivTradingSystem/).

Run the Java application:

mvn exec:java -Dexec.mainClass="com.myproject.deriv.Main"

This command will:

Start the Java Main class.

The Java application will then launch the deriv_data_streamer.py script as a subprocess.

The Python script will connect to Deriv and start streaming tick data.

The Java application will read this data, process it in StrategyEngine, and print signals to the console.

Understanding the Output
You will see continuous output in your terminal, showing:

Real-time tick data (symbol, epoch time, price).

Messages from the StrategyEngine as it processes data and identifies potential trading signals (e.g., "BUY Signal!" or "SELL Signal!").

Customizing Your Strategy
Your trading strategy logic will primarily reside in src/main/java/com/myproject/deriv/StrategyEngine.java.

Open StrategyEngine.java.

Implement your strategy logic within the processTick(TickData tick) method.

This method is called every time a new tick of data arrives.

You'll need to store historical data if your strategy relies on it (e.g., for moving averages, RSI, etc.). You can use a List<TickData> or a custom data structure within this class.

Example: You could calculate a simple moving average of the last 'N' ticks and generate a signal if the current price crosses it.

// Example (inside StrategyEngine.java):
private List<Double> priceHistory = new ArrayList<>();
private final int WINDOW_SIZE = 10; // For a simple moving average

public void processTick(TickData tick) {
    System.out.println("Strategy Engine received tick: " + tick);

    priceHistory.add(tick.getPrice());
    if (priceHistory.size() > WINDOW_SIZE) {
        priceHistory.remove(0); // Keep history size
    }

    if (priceHistory.size() == WINDOW_SIZE) {
        double sum = 0;
        for (double price : priceHistory) {
            sum += price;
        }
        double movingAverage = sum / WINDOW_SIZE;

        System.out.println("Current Price: " + tick.getPrice() + ", " + WINDOW_SIZE + "-tick MA: " + movingAverage);

        if (tick.getPrice() > movingAverage * 1.001) { // 0.1% above MA
            System.out.println(">>> BUY Signal! <<<");
        } else if (tick.getPrice() < movingAverage * 0.999) { // 0.1% below MA
            System.out.println(">>> SELL Signal! <<<");
        }
    }
}

Troubleshooting
ConnectionRefusedError or WebSocket connection failed (Python script):

Double-check your APP_ID in deriv_data_streamer.py. Ensure it's correct and registered on Deriv.

Check your internet connection.

python: command not found or python3: command not found:

Ensure Python is installed and added to your system's PATH. You might need to use python3 instead of python depending on your system's configuration.

Java compilation errors:

Ensure JDK 11 or higher is installed and configured correctly.

Run mvn clean install again to rebuild the project.

No data received in Java, but Python script runs:

Verify the Python script is printing JSON data to stdout correctly.

Check the DerivDataReader.java for any errors in reading the process's input stream.

Ensure the Python script's output format matches what DerivDataReader expects.

Future Enhancements
Persistence: Store historical tick data and trading signals in a database (e.g., H2 (in-memory) or SQLite for simplicity, or PostgreSQL/MySQL for more robust storage) for backtesting and analysis.

Backtesting Framework: Integrate a proper backtesting module to test your strategies on historical data.

User Interface (UI): Develop a simple Swing/JavaFX or web-based UI to visualize real-time data, strategy performance, and signals.

Advanced Strategy: Implement more sophisticated technical indicators, machine learning models, or risk management rules.

Multiple Symbols: Extend the system to subscribe and process data for multiple volatility indices concurrently.

Order Execution: For a production-ready system (beyond a school project), integrate with Deriv's trading APIs to place actual orders (requires an authenticated trading account and careful handling of funds).