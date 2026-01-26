package currency.app.services;

import currency.app.DAO.ExchangeRatesDAO;
import currency.app.Exceptions.NotFoundException;
import currency.app.Utilites.JSONUtils;
import currency.app.Utilites.Utils;
import currency.app.entities.ExchangeRate;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeService {
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO();

    public String findByCodes(String from, String to) throws NotFoundException {
        Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findByCodes(from, to);
        if (exchangeRate.isPresent()) {
            return JSONUtils.getSimpleJson(exchangeRate.get());
        } else {
            throw new NotFoundException("currency not found");
        }
    }


    public String updateExchangeRate(String from, String to, String rate) throws NotFoundException, RuntimeException {
        Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findByCodes(from, to);
        if (exchangeRate.isPresent()) {
            //делаем update
            exchangeRate.get().setRate(BigDecimal.valueOf(Double.parseDouble(rate)));
            Optional<ExchangeRate> res = exchangeRatesDAO.update(exchangeRate.get());

            if (res.isPresent()) {
                return JSONUtils.getSimpleJson(exchangeRate.get());
            } else {
                throw new RuntimeException("server error");
            }
        } else {
            throw new NotFoundException("currency not found");
        }
    }

}