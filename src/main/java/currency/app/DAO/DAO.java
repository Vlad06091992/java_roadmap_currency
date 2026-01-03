package currency.app.DAO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> findById(int id) ;
    List<T> findAll() ;
    Optional<T> findByCode(String code);
    T save(T entity);
    Optional<T> create(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate);

    Optional<T> update(T entity);
    void delete(String id);
}

