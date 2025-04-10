package interfaces;

import java.util.List;

public interface GenericDAO<T, ID> {
    boolean create(T entity);
    boolean update(T entity);
    boolean delete(ID id);
    T findById(ID id);
    List<T> findAll();
}
