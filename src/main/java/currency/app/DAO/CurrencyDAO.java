package currency.app.DAO;

import currency.app.entities.Currency;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CurrencyDAO implements DAO<Currency> {
    private final DatabaseAdapter dbAdapter = new DatabaseAdapter();

    public Optional<Currency> findByCode(String code) {
        try {
            return dbAdapter.getEntityByParam(Currency.class, "code",code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        try {
            return dbAdapter.getListEntities(Currency.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Currency save(Currency entity) throws SQLException {
            Object[] queryParams = {entity.getCode(), entity.getFullName(), entity.getSign()};
            int id = dbAdapter.saveAndReturnId(Currency.class, queryParams);
            entity.setId(id);
            return entity;
    }

    @Override
    public Optional<Currency> update(Currency entity) {
        return null;
    }

}
