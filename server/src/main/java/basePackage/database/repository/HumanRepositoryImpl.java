package basePackage.database.repository;

import basePackage.database.model.DatabaseHuman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HumanRepositoryImpl implements HumanRepository {
    private static final String TABLE_NAME = "Humans";
    private static final String[] FIELDS = new String[]{
            "id",
            "name",
            "actionNames",
            "professions",
            "locationName",
            "creationTime"
    };
    private static final String ID_SEQUENCE_NAME = "humans_id_seq";

    private Connection connection;

    public HumanRepositoryImpl(Connection connection) throws SQLException {
        this.connection = Objects.requireNonNull(connection);

        if (connection.isClosed()) {
            throw new RuntimeException();
        }
    }

    @Override
    public void createTable() throws SQLException {
        int index = 0;
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s(" +
                        "%s SERIAL PRIMARY KEY," +
                        "%s VARCHAR(255), " +
                        "%s VARCHAR(1023), " +
                        "%s VARCHAR(1023), " +
                        "%s VARCHAR(255), " +
                        "%s VARCHAR(255)" +
                        ");",
                TABLE_NAME,
                FIELDS[index++], FIELDS[index++], FIELDS[index++],
                FIELDS[index++], FIELDS[index++], FIELDS[index++]
        );
        connection.createStatement().execute(sql);
    }

    @Override
    public int createHuman(DatabaseHuman human) throws SQLException {
        Objects.requireNonNull(human);

        int index = 1;
        String sql = String.format(
                "INSERT INTO %s(%s,%s,%s,%s,%s) " +
                        "VALUES (?,?,?,?,?);",
                TABLE_NAME, FIELDS[index++], FIELDS[index++],
                FIELDS[index++], FIELDS[index++], FIELDS[index++]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        index = 1;
        statement.setString(index++, human.getName());
        statement.setString(index++, String.join(",", human.getActionNames()));
        statement.setString(index++, String.join(",", human.getProfessions()));
        statement.setString(index++, human.getLocationName());
        statement.setString(index++, human.getCreationTime());

        if (statement.executeUpdate() == 0) {
            return -1;
        }

        return getLastId();
    }

    @Override
    public DatabaseHuman getHumanById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        String sql = String.format("SELECT * FROM %s WHERE %s=?;", TABLE_NAME, FIELDS[0]);
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, id);

        ResultSet set = statement.executeQuery();
        if (set.next()) {
            return mapFrom(set);
        } else {
            return null;
        }
    }

    @Override
    public List<DatabaseHuman> getAllHumans() throws SQLException {
        String sql = String.format("SELECT * FROM %s;", TABLE_NAME);
        ResultSet set = connection.createStatement().executeQuery(sql);

        List<DatabaseHuman> list = new ArrayList<>();
        while (set.next()) {
            list.add(mapFrom(set));
        }
        return list;
    }

    @Override
    public boolean updateHuman(DatabaseHuman human) throws SQLException {
        if (human.getId() <= 0) {
            throw new IllegalArgumentException();
        }

        int index = 1;
        String sql = String.format(
                "UPDATE %s" +
                        "SET " +
                        "%s=?" +
                        "%s=?" +
                        "%s=?" +
                        "%s=?" +
                        "%s=?" +
                        " WHERE \'%s\'=?"
                , TABLE_NAME, FIELDS[index++], FIELDS[index++],
                FIELDS[index++], FIELDS[index++], FIELDS[index++], FIELDS[0]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        index = 1;
        statement.setString(index++, human.getName());
        statement.setString(index++, String.join(",", human.getActionNames()));
        statement.setString(index++, String.join(",", human.getProfessions()));
        statement.setString(index++, human.getLocationName());
        statement.setString(index++, human.getCreationTime());
        statement.setInt(index++, human.getId());

        return statement.executeUpdate() > 0;
    }

    @Override
    public boolean removeHuman(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        return connection.
                createStatement()
                .executeUpdate("DELETE FROM " + TABLE_NAME + " WHERE " + FIELDS[0] + "=" + id + ";") > 0;
    }

    private static DatabaseHuman mapFrom(ResultSet set) throws SQLException {
        int index = 0;
        int id = set.getInt(FIELDS[index++]);
        String name = set.getString(FIELDS[index++]);
        List<String> actionNames = Arrays.asList(set.getString(FIELDS[index++]).split(","));
        List<String> professions = Arrays.asList(set.getString(FIELDS[index++]).split(","));
        String locationName = set.getString(FIELDS[index++]);
        String creationTime = set.getString(FIELDS[index++]);
        return new DatabaseHuman()
                .withId(id)
                .withName(name)
                .withActionNames(actionNames)
                .withProfessions(professions)
                .withLocationName(locationName)
                .withCreationTime(creationTime);
    }

    private int getLastId() throws SQLException {
        ResultSet set = connection
                .createStatement()
                .executeQuery(String.format(
                        "SELECT last_value FROM %s;",
                        ID_SEQUENCE_NAME
                ));

        return set.next() ? set.getInt("last_value") : -1;
    }
}
