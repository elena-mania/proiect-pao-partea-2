package dao;

import java.sql.SQLException;

public interface DaoInterface <T>{
    void create(T entity)  throws SQLException;

    T read(int id) throws SQLException;

    void delete(int id) throws SQLException;

    void update(T entity, int id) throws SQLException;
}
