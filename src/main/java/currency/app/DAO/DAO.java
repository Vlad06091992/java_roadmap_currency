package currency.app.DAO;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    List<T> findAll() throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

    T save(T entity) throws SQLException;

    Optional<T> update(T entity);
}

