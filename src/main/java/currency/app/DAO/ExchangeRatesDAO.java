package currency.app.DAO;

import currency.app.Configs.DatabaseAdapter;
import currency.app.entities.Currency;
import currency.app.entities.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ExchangeRatesDAO implements DAO<ExchangeRate> {
    private final DatabaseAdapter db = new DatabaseAdapter();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    public Optional<ExchangeRate> findById(int id) {
        try {
            String[] columns = {"id", "base_currency_id", "target_currency_id", "rate"};
            Object[] queryParams = {id};
            String query = "SELECT * FROM exchange_rates WHERE id = ?";

            Map<String, Object> result = db.executeRaw(query, columns, queryParams);

            int idd = (int) result.get(columns[0]);
            int base_currency_id = (int) result.get(columns[1]);
            int target_currency_id = (int) result.get(columns[2]);
            BigDecimal rate = (BigDecimal) result.get(columns[3]);


            Optional<Currency> baseCurrency = currencyDAO.findById(base_currency_id);
            Optional<Currency> targetCurrency = currencyDAO.findById(target_currency_id);

            if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
                ExchangeRate exchangeRate = new ExchangeRate(idd, baseCurrency.get(), targetCurrency.get(), rate);
                return Optional.of(exchangeRate);
            } else return Optional.empty();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) {
        return null;
    }

    @Override
    public ExchangeRate update(ExchangeRate entity) {
        return null;
    }

//    @Override
//    public List<Currency> findAll() {
//        try {
//            String[] columns = {"code", "fullName", "sign", "id"};
//            String query = "SELECT * FROM currencies";
//            return db.executeAll(query, columns, Currency.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
//    }

//    @Override
//    public ExchangeRate save(ExchangeRate entity) {
//        try {
//            Object[] queryParams = {entity.getCode(), entity.getFullName(), entity.sign};
//            String query = "INSERT INTO public.currencies(\n" +
//                    "\tcode, fullname, sign)\n" +
//                    "\tVALUES (?, ?, ?) RETURNING id;";
//            int id = db.executeForSave(query, queryParams);
//            entity.setId(id);
//            return entity;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    @Override
//    public Currency update(Currency entity) {
//        return null;
//    }

    @Override
    public void delete(String id) {

    }


    @Override
    public List<ExchangeRate> findAll() {
        return List.of();
    }

    @Override
    public Optional<ExchangeRate> findByCode(String code) {
        return Optional.empty();
    }
}
