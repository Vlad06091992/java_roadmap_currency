package currency.app.container;

import currency.app.DAO.CurrencyDAO;
import currency.app.DAO.DatabaseAdapter;
import currency.app.DAO.ExchangeRatesDAO;
import currency.app.Utilites.Utils;
import currency.app.services.CurrencyService;
import currency.app.services.ExchangeService;

public class AppContext {
    private final Utils utils = new Utils();
    private final DatabaseAdapter dbAdapter = new DatabaseAdapter();
    private final CurrencyDAO currencyDAO = new CurrencyDAO(dbAdapter);
    private final ExchangeRatesDAO exchangeRatesDAO = new ExchangeRatesDAO(dbAdapter, currencyDAO);
    private final CurrencyService currencyService = new CurrencyService(currencyDAO);
    private final ExchangeService exchangeService = new ExchangeService(exchangeRatesDAO);

    private AppContext() {
        System.out.println("AppContext created");
    }

    private static final class InstanceHolder {
        static private final AppContext instance = new AppContext();
    }

    public static AppContext getInstance() {
        return InstanceHolder.instance;
    }

    public DatabaseAdapter getDbAdapter() {
        return dbAdapter;
    }

    public CurrencyDAO getCurrencyDAO() {
        return currencyDAO;
    }

    public ExchangeRatesDAO getExchangeRatesDAO() {
        return exchangeRatesDAO;
    }

    public CurrencyService getCurrencyService() {
        return currencyService;
    }

    public ExchangeService getExchangeService() {
        return exchangeService;
    }

    public Utils getUtils() {
        return utils;
    }

}
