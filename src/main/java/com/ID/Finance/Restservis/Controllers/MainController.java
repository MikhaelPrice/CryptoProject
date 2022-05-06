package com.ID.Finance.Restservis.Controllers;

import com.ID.Finance.Restservis.GettingCrypto;
import com.ID.Finance.Restservis.models.Prices;
import com.ID.Finance.Restservis.repo.PricesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private PricesRepository pricesRepository;

    @PostMapping("/")
    public String postInformation(@RequestParam(value = "username") String username, @RequestParam(value = "symbol") String symbol, Model model) {
        double price = GettingCrypto.getCrypto(symbol);
        Prices newPrices = new Prices(username, symbol, price);
        pricesRepository.save(newPrices);
        if (pricesRepository.findById(newPrices.getId()).isPresent()) {
            if (price == 0) {
                model.addAttribute("price", "Криптовалюта недоступна к просмотру или проверьте корректность ввода данных");
            } else {
                model.addAttribute("price", pricesRepository.findById(newPrices.getId()).get().getPrice());
            }
        }
        return "home";
    }

    @GetMapping("/")
    public String getInformation(@RequestParam(value = "username") String username, @RequestParam(value = "symbol") String symbol, Model model) {
        try {
            double price = GettingCrypto.getCrypto(symbol);
            Prices prices = new Prices(username, symbol, price);
            pricesRepository.save(prices);
            if (pricesRepository.findById(prices.getId()).isPresent()) {
                model.addAttribute("price", pricesRepository.findById(prices.getId()).get().getPrice());
            }
        } catch (Exception e) {
            model.addAttribute("price", "Криптовалюта недоступна к просмотру или проверьте корректность ввода данных");
        }
        return "home";
    }

}
