package currency.app.DAO;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> findById(int id) ;
    List<T> findAll() ;
    Optional<T> findByCode(String code);
    T save(T entity);
    T update(T entity);
    void delete(String id);
}

