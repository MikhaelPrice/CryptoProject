package com.ID.Finance.Restservis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class GettingValute {

    public static HashMap<String, Double> forBuying = new HashMap<>();
    public static HashMap<String, Double> forSale = new HashMap<>();

    public static void getValute() {
        try {
            URL url = new URL("https://belarusbank.by/api/kursExchange");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openConnection().getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder information = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                inputLine = inputLine.replaceAll("\"", "");
                information.append(inputLine);
            }
            inputLine = information.substring(information.indexOf("{"), information.indexOf("}"));
            String[] values = inputLine.split(",");
            for (String value : values) {
                int index = value.indexOf(":");
                double price = Double.parseDouble(value.substring(index + 1));
                if (value.substring(0, index).contains("USD_in")) {
                    forBuying.put("USD_in", price);
                } else if (value.substring(0, index).equalsIgnoreCase("USD_out")) {
                    forSale.put("USD_out", price);
                }
                if (value.substring(0, index).equalsIgnoreCase("EUR_in")) {
                    forBuying.put("EUR_in", price);
                } else if (value.substring(0, index).equalsIgnoreCase("EUR_out")) {
                    forSale.put("EUR_out", price);
                }
                if (value.substring(0, index).equalsIgnoreCase("RUB_in")) {
                    forBuying.put("RUB_in", price);
                } else if (value.substring(0, index).equalsIgnoreCase("RUB_out")) {
                    forSale.put("RUB_out", price);
                    break;
                }
            }
            System.out.println(forBuying);
            System.out.println(forSale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
