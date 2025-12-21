package currency.app.DAO;

import java.sql.SQLException;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> findById(int id) throws SQLException;

//    Optional<Currency> findById(Integer id) throws SQLException;
//
//    Optional<Currency> findById(Integer id) throws SQLException;
//
//    List<T> getAll(String id);
//    int add(T entity);
}
