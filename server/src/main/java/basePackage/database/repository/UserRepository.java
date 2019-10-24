package basePackage.database.repository;

import basePackage.database.model.User;

import java.sql.SQLException;

public interface UserRepository {

    void createTable() throws SQLException;

    int createUser(User user) throws SQLException;

    User getUserById(int id) throws SQLException;

    User getUserByUsername(String username) throws SQLException;
}
