package currency.app.services;

import currency.app.DAO.CurrencyDAO;
import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotFoundException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Exceptions.ServiceException;
import currency.app.Utilites.JSONUtils;
import currency.app.entities.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDAO currencyDAO;

    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public String getAllCurrenciesStringResponse() {
        List<Currency> currencies = currencyDAO.findAll();
        return JSONUtils.getSimpleJson(currencies);
    }

    public String getCurrencyByCode(String code) throws NotFoundException {
        Optional<Currency> currency = currencyDAO.findByCode(code);

        if (currency.isEmpty()) {
            throw new NotFoundException("currency not found");
        }

        return JSONUtils.getSimpleJson(currency.get());
    }

    public String createCurrency(String name, String code, String sign) {
        for (String field : new String[]{code, sign, name}) {
            if (field == null || field.isEmpty()) {
                throw new NotValidDataException("Need required fields: name, code, sign");
            }
        }

        Optional<Currency> currency = currencyDAO.findByCode(code);
        if (currency.isPresent()) {
            throw new IsExistException("Currency already exists");
        }

        Currency newCurrency = new Currency(name, code, sign);
        try {
            Currency createdCurrency = currencyDAO.save(newCurrency);
            return JSONUtils.getSimpleJson(createdCurrency);

        } catch (SQLException ex) {
            throw new ServiceException("Database error");
        }


    }

}
