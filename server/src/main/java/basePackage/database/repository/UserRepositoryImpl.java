package basePackage.database.repository;

import basePackage.database.model.User;

import java.sql.*;
import java.util.Objects;

public class UserRepositoryImpl implements UserRepository {

    private static final String TABLE_NAME = "ServiceUsers";
    private static final String[] FIELDS = new String[]{
            "id",
            "username",
            "email",
            "password_hash"
    };
    private static final String ID_SEQUENCE_NAME = "serviceusers_id_seq";

    private Connection connection;

    public UserRepositoryImpl(Connection connection) throws SQLException {
        this.connection = Objects.requireNonNull(connection);

        if (connection.isClosed()) {
            throw new RuntimeException();
        }
    }

    @Override
    public void createTable() throws SQLException {
        Statement statement = connection.createStatement();

        int index = 0;
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s(" +
                        "%s SERIAL PRIMARY KEY," +
                        "%s VARCHAR(255) UNIQUE NOT NULL, " +
                        "%s VARCHAR(255) NOT NULL, " +
                        "%s VARCHAR(255) NOT NULL" +
                        ");",
                TABLE_NAME,
                FIELDS[index++], FIELDS[index++], FIELDS[index++], FIELDS[index++]
        );
        statement.execute(sql);
    }

    @Override
    public int createUser(User user) throws SQLException {
        Objects.requireNonNull(user);
        Objects.requireNonNull(user.getUsername());
        Objects.requireNonNull(user.getEmail());
        Objects.requireNonNull(user.getPasswordHash());

        int index = 1;
        String sql = String.format(
                "INSERT INTO %s(%s,%s,%s)" +
                        "VALUES (?,?,?)",
                TABLE_NAME, FIELDS[index++], FIELDS[index++], FIELDS[index++]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        index = 1;
        statement.setString(index++, user.getUsername());
        statement.setString(index++, user.getEmail());
        statement.setString(index++, user.getPasswordHash());

        if(statement.executeUpdate() == 0) {
            return -1;
        }

        return getLastId();
    }

    @Override
    public User getUserById(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        String sql = String.format("SELECT * FROM %s WHERE \'%s\'=?", TABLE_NAME, FIELDS[0]);
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
    public User getUserByUsername(String username) throws SQLException {
        Objects.requireNonNull(username);

        String sql = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, FIELDS[1]);
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, username);

        ResultSet set = statement.executeQuery();
        if (set.next()) {
            return mapFrom(set);
        } else {
            return null;
        }
    }

    private static User mapFrom(ResultSet set) throws SQLException {
        int index = 0;
        int id = set.getInt(FIELDS[index++]);
        String username = set.getString(FIELDS[index++]);
        String email = set.getString(FIELDS[index++]);
        String passwordHash = set.getString(FIELDS[index++]);
        return new User()
                .withId(id)
                .withUsername(username)
                .withEmail(email)
                .withPasswordHash(passwordHash);
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
