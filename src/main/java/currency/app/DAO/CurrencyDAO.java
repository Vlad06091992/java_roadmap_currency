package currency.app.DAO;

import currency.app.Configs.DatabaseAdapter;
import currency.app.entities.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CurrencyDAO implements DAO<Currency> {
    private final DatabaseAdapter dbAdapter = new DatabaseAdapter();

    @Override
    public Optional<Currency> findByCode(String code) {
        try {
            String[] columns = {"code", "fullName", "sign", "id"};
            Object[] queryParams = {code};
            String query = "SELECT * FROM currencies WHERE code = ?";
            return dbAdapter.getEntity(query, columns, Currency.class, queryParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        try {
            String[] columns = {"code", "fullName", "sign", "id"};
            String query = "SELECT * FROM currencies";
            return dbAdapter.getListEntities(query, columns, Currency.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Currency save(Currency entity) {
        try {
            Object[] queryParams = {entity.getCode(), entity.getFullName(), entity.getSign()};
            String query = "INSERT INTO public.currencies(\n" +
                    "\tcode, fullname, sign)\n" +
                    "\tVALUES (?, ?, ?) RETURNING id;";
            int id = dbAdapter.executeWithReturningId(query, queryParams);
            entity.setId(id);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Currency> create(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        return Optional.empty();
    }

    @Override
    public Optional<Currency> update(Currency entity) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Optional<Currency> findById(int id) {

        try {
            String[] columns = {"code", "fullName", "sign", "id"};
            Object[] queryParams = {id};
            String query = "SELECT * FROM currencies WHERE id = ?";
            return dbAdapter.getEntity(query, columns, Currency.class, queryParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
