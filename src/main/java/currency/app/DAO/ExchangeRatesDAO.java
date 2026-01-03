package currency.app.DAO;

import currency.app.Configs.DatabaseAdapter;
import currency.app.entities.Currency;
import currency.app.entities.ExchangeRate;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ExchangeRatesDAO implements DAO<ExchangeRate> {
    private final DatabaseAdapter dbAdapter = new DatabaseAdapter();
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    public Optional<ExchangeRate> findById(int id) {
        try {
            String[] columns = {"id", "base_currency_id", "target_currency_id", "rate"};
            Object[] queryParams = {id};
            String query = "SELECT * FROM exchange_rates WHERE id = ?";

            Map<String, Object> result = dbAdapter.executeRaw(query, columns,QUERY_MODES.SELECT, queryParams);

            int idd = (int) result.get(columns[0]);
            int base_currency_id = (int) result.get(columns[1]);
            int target_currency_id = (int) result.get(columns[2]);
            BigDecimal rate = (BigDecimal) result.get(columns[3]);


            Optional<Currency> baseCurrency = currencyDAO.findById(base_currency_id);
            Optional<Currency> targetCurrency = currencyDAO.findById(target_currency_id);

            if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
                ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(), targetCurrency.get(), rate, idd);
                return Optional.of(exchangeRate);
            } else return Optional.empty();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public Optional<ExchangeRate> findByCodes(String fromCode, String toCode) {
        try {

            Optional<Currency> baseCurrency = currencyDAO.findByCode(fromCode);
            Optional<Currency> targetCurrency = currencyDAO.findByCode(toCode);

            if (baseCurrency.isPresent() && targetCurrency.isPresent()) {

                String[] columns = {"id", "base_currency_id", "target_currency_id", "rate"};
                Object[] queryParams = {baseCurrency.get().getId(), targetCurrency.get().getId()};
                String query = "SELECT * FROM exchange_rates WHERE base_currency_id = ? AND target_currency_id = ?";

                Map<String, Object> result = dbAdapter.executeRaw(query, columns, QUERY_MODES.SELECT, queryParams);

                if (result.isEmpty()) return Optional.empty();

                int idd = (int) result.get(columns[0]);
                BigDecimal rate = (BigDecimal) result.get(columns[3]);
                ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(), targetCurrency.get(), rate,idd);
                return Optional.of(exchangeRate);

            } else {
                return Optional.empty();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<ExchangeRate> create(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        try {

            Optional<Currency> baseCurrency = currencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = currencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isPresent() && targetCurrency.isPresent()) {
                ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(), targetCurrency.get(), rate);
                ExchangeRate res = save(exchangeRate);
                return Optional.of(res);
            } else {
                return Optional.empty();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate entity) {
        try {
            String query = "UPDATE public.exchange_rates\n" +
                    "\tSET  rate=?\n" +
                    "\tWHERE id = ?";


            String[] columns = {"id", "base_currency_id", "target_currency_id", "rate"};
            Object[] queryParams = {entity.getRate(),entity.getId()};
           Map<String,Object> res = dbAdapter.executeRaw(query, columns, QUERY_MODES.UPDATE, queryParams);
           if (res.isEmpty()) return Optional.empty();
           return Optional.of(entity);
        } catch (Exception e) {
            return  Optional.empty();
        }


    }

    @Override
    public List<ExchangeRate> findAll() {
        return List.of();
    }

    @Override
    public Optional<ExchangeRate> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public ExchangeRate save(ExchangeRate entity) throws SQLException {
        Object[] queryParams = {entity.getBaseCurrency().getId(), entity.getTargetCurrency().getId(), entity.getRate()};
        String query = "INSERT INTO public.exchange_rates(\n" +
                "\t base_currency_id, target_currency_id, rate)\n" +
                "\tVALUES (?, ? ,? ) RETURNING id;";
        int id = dbAdapter.executeWithReturningId(query, queryParams);
        entity.setId(id);
        return entity;
    }
}
