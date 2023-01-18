package at.bif3.swe1.kisin.httpServer.database;

import java.util.Collection;
import java.util.Optional;

public interface DAO<T> {
    //read
    Optional<T> get(int id);

    Optional<T> get(String username);
    Collection<T> getAll();
    //create
    void insert(T t);
    //update
    void update(T t, String[] params);
    //delete
    void delete(T t);
}
//TODO not implemented exception -> or repository pattern