package currency.app.services;

import currency.app.DAO.CurrencyDAO;
import currency.app.Exceptions.IsExistException;
import currency.app.Exceptions.NotValidDataException;
import currency.app.Utilites.JSONUtils;
import currency.app.entities.Currency;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();
    public String getAllCurrenciesStringResponse(){
        List<Currency> currencies = currencyDAO.findAll();
        return JSONUtils.getSimpleJson(currencies);
    }

    public String createCurrency(String name,String code,String sign) throws SQLException,IsExistException{

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
        Currency createdCurrency = currencyDAO.save(newCurrency);
        return JSONUtils.getSimpleJson(createdCurrency);
    }

}
