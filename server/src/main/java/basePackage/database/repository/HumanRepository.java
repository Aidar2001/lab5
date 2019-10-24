package basePackage.database.repository;

import basePackage.database.model.DatabaseHuman;

import java.sql.SQLException;
import java.util.List;

public interface HumanRepository {

    void createTable() throws SQLException;

    int createHuman(DatabaseHuman human) throws SQLException;

    DatabaseHuman getHumanById(int id) throws SQLException;

    List<DatabaseHuman> getAllHumans() throws SQLException;

    boolean updateHuman(DatabaseHuman human) throws SQLException;

    boolean removeHuman(int id) throws SQLException;
}
