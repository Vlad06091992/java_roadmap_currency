package currency.app.DAO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> findById(int id);

    List<T> findAll();

    Optional<T> findByCode(String code);

    //    Optional<T> save(T entity);
    T save(T entity) throws SQLException;

    Optional<T> update(T entity);
}

