package com.ID.Finance.Restservis;

import com.ID.Finance.Restservis.models.Prices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Pattern;

public class GettingCrypto {

    public static String getCryptoPrice(String name) {
        String price = null;
        int id = 0;
        if (name.equalsIgnoreCase("BTC")) {
            id = 90;
        } else if (name.equalsIgnoreCase("ETH")) {
            id = 80;
        } else if (name.equalsIgnoreCase("SOL")) {
            id = 48543;
        }
        try {
            URL url = new URL("https://api.coinlore.net/api/ticker/?id=" + id);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                Pattern pattern = Pattern.compile("price_usd:");
                inputLine = inputLine.substring(2, inputLine.length() - 2).replaceAll("\"", "");
                String[] values = inputLine.split(",");
                for (String value : values) {
                    if (value.startsWith(String.valueOf(pattern))) {
                        price = value.substring(value.indexOf(":") + 1, value.length() - 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return price;
    }

    public static boolean priceChange(double oldPrice, double newPrice) {
        return newPrice <= oldPrice * 0.99 || newPrice > oldPrice * 1.01;
    }

    public static int getIndex(String symbol){
        int id = 0;
        if (symbol.equalsIgnoreCase("BTC")) {
            id = 90;
        } else if (symbol.equalsIgnoreCase("ETH")) {
            id = 80;
        } else if (symbol.equalsIgnoreCase("SOL")) {
            id = 48543;
        }
        return id;
    }
}
