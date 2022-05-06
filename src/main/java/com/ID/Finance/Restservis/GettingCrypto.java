package com.ID.Finance.Restservis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GettingCrypto {

    public static final HashMap<String, Double> cryptos = new HashMap<>();

    public static double getCrypto(String nameOrSymbol) {
        double price = 0;
        parseCrypto();
        if (!cryptos.isEmpty()) {
            for (Map.Entry<String, Double> entry : cryptos.entrySet()) {
                if (entry.getKey().contains(nameOrSymbol)) {
                    price = entry.getValue();
                }
            }
        }
        return price;
    }

    public static void parseCrypto() {
        try {
            URL url = new URL("https://api.coinlore.net/api/tickers/");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder information = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                inputLine = inputLine.replaceAll("\"", "");
                information.append(inputLine);
            }
            String[] infValues = information.toString().split(",");
            String name = "", symbol = "";
            for (String infValue : infValues) {
                int index = infValue.indexOf(":");
                if (infValue.substring(0, index).equalsIgnoreCase("symbol")) {
                    symbol = infValue.substring(index + 1);
                }
                if (infValue.substring(0, index).equalsIgnoreCase("name")) {
                    name = infValue.substring(index + 1);
                }
                if (infValue.substring(0, index).equalsIgnoreCase("price_usd")) {
                    double price = Double.parseDouble(infValue.substring(index + 1));
                    cryptos.put(name + symbol + symbol.toLowerCase(Locale.ROOT), price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
