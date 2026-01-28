package currency.app.services;

import currency.app.DAO.ExchangeRatesDAO;
import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotFoundException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Utilites.JSONUtils;
import currency.app.entities.Currency;
import currency.app.entities.Exchange;
import currency.app.entities.ExchangeRate;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
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

    public String createExchangeRate(String baseCurrencyCode, String targetCurrencyCode, String rate) throws NotFoundException {

        Optional<ExchangeRate> exchangeRate = exchangeRatesDAO.findByCodes(baseCurrencyCode, targetCurrencyCode);
        if (exchangeRate.isPresent()) {
            throw new IsExistException("exchange rate already exists");

        } else {
            BigDecimal rateValue = BigDecimal.valueOf(Double.parseDouble(rate));

            if (rateValue.compareTo(BigDecimal.ZERO) < 0) {
                throw new NotValidDataException("Exchange rate cannot be negative. " + rateValue);
            }

            Optional<ExchangeRate> newExchangeRate = exchangeRatesDAO.create(baseCurrencyCode, targetCurrencyCode, rateValue);

            if (newExchangeRate.isPresent()) {
                return JSONUtils.getSimpleJson(newExchangeRate);
            } else {
                throw new RuntimeException("server error");
            }
        }

    }


    public String getAllExchangeRates() throws NotFoundException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<ExchangeRate> exchangeRates = exchangeRatesDAO.findAll();
        return JSONUtils.getSimpleJson(exchangeRates);
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

    public String exchange (String from, String to, String amount) throws NotFoundException, NotValidDataException {
         BigDecimal hundred = BigDecimal.valueOf(100);

        BigDecimal amountValue = BigDecimal.valueOf(Long.parseLong(amount));

        if (amountValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotValidDataException("Amount cannot be negative. " + amountValue);
        }

        Optional<ExchangeRate> exchangeRate = Optional.empty();

        Optional<ExchangeRate> excRate = exchangeRatesDAO.findByCodes(from, to);
        if (excRate.isPresent()) {
            exchangeRate = excRate;
        } else {
            Optional<ExchangeRate> reversedExchangeRate = exchangeRatesDAO.findByCodes(to, from);
            if (reversedExchangeRate.isPresent()) {
                Currency targetCurrency = reversedExchangeRate.get().getTargetCurrency();
                Currency baseCurrency = reversedExchangeRate.get().getBaseCurrency();
                BigDecimal rate = hundred.divide(reversedExchangeRate.get().getRate().multiply(hundred), 7, RoundingMode.HALF_UP);
                reversedExchangeRate.get().setBaseCurrency(targetCurrency);
                reversedExchangeRate.get().setTargetCurrency(baseCurrency);
                reversedExchangeRate.get().setRate(rate);
                exchangeRate = reversedExchangeRate;

            }
        }

        if (exchangeRate.isPresent()) {
            Exchange exchange = new Exchange(exchangeRate.get().getBaseCurrency(), exchangeRate.get().getTargetCurrency(), exchangeRate.get().getRate(), amountValue);
            return JSONUtils.getSimpleJsonWithoutId(exchange);
        } else {
            Optional<ExchangeRate> usdFrom = exchangeRatesDAO.findByCodes("USD", from);
            Optional<ExchangeRate> usdTo = exchangeRatesDAO.findByCodes("USD", to);


            if (usdFrom.isEmpty() || usdTo.isEmpty()) {
               throw new NotFoundException("valute not found");
            }

            BigDecimal usdFromRate = usdFrom.get().getRate();
            BigDecimal usdToRate = usdTo.get().getRate();

            BigDecimal resultRate = usdToRate.divide(usdFromRate, 7, RoundingMode.HALF_UP);
            Exchange exchange = new Exchange(usdFrom.get().getTargetCurrency(), usdTo.get().getTargetCurrency(), resultRate, amountValue);
            return currency.app.Utilites.JSONUtils.getSimpleJsonWithoutId(exchange);
        }

    }

}