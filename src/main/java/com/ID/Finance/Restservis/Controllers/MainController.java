package com.ID.Finance.Restservis.Controllers;

import com.ID.Finance.Restservis.GettingCrypto;
import com.ID.Finance.Restservis.models.Prices;
import com.ID.Finance.Restservis.repo.PricesRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;

@Controller
public class MainController extends TimerTask {

    static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private PricesRepository pricesRepository;
    double oldPrice;
    String symbol = "BTC";
    String username;

    @PostMapping("/")
    public String postInformation(@RequestParam(value = "username") String username, @RequestParam(value = "symbol") String symbol, Model model) {
        try {
            double price = Double.parseDouble(GettingCrypto.getCryptoPrice(symbol));
            Prices prices = new Prices(username, symbol, price);
            this.symbol = symbol;
            this.username = username;
            oldPrice = price;
            pricesRepository.save(prices);
            if (pricesRepository.findById(prices.getId()).isPresent()) {
                model.addAttribute("price", pricesRepository.findById(prices.getId()).get().getPrice());
            }
            Timer timer = new Timer();
            MainController task = new MainController();
            timer.schedule(task, 0, 60000);
        } catch (Exception e) {
            model.addAttribute("price", "Криптовалюта недоступна к просмотру или проверьте корректность ввода данных");
        }
        return "home";
    }

    @Override
    public void run() {
        double newPrice = Double.parseDouble(GettingCrypto.getCryptoPrice(symbol));
        if (GettingCrypto.priceChange(oldPrice, newPrice)) {
            String var = GettingCrypto.getIndex(symbol) + " " + username + " " + (oldPrice / newPrice * 100);
            LOGGER.warn(var);
        }
        oldPrice = newPrice;
    }
}
