package basePackage.database.repository;

import basePackage.database.model.Association;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssociationRepositoryImpl implements AssociationRepository { // TODO realize class
    private static final String TABLE_NAME = "Associations";
    private static final String[] FIELDS = new String[]{
            "username",
            "humanId"
    };

    private Connection connection;

    public AssociationRepositoryImpl(Connection connection) throws SQLException {
        this.connection = Objects.requireNonNull(connection);

        if (connection.isClosed()) {
            throw new RuntimeException();
        }
    }

    @Override
    public void createTable() throws SQLException {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s(" +
                        "%s VARCHAR(255)," +
                        "%s INT4," +
                        "PRIMARY KEY (%s, %s)" +
                        ");",
                TABLE_NAME,
                FIELDS[0], FIELDS[1],
                FIELDS[0], FIELDS[1]
        );
        connection.createStatement().execute(sql);
    }

    @Override
    public boolean createAssociation(Association association) throws SQLException {
        String sql = String.format(
                "INSERT INTO %s(%s,%s) " +
                        "VALUES (?,?);",
                TABLE_NAME, FIELDS[0], FIELDS[1]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, association.getUsername());
        statement.setInt(2, association.getHumanId());

        return statement.executeUpdate() > 0;
    }

    @Override
    public Association getAssociationByKey(String username, int humanId) throws SQLException {
        String sql = String.format(
                "SELECT * FROM %s WHERE %s=? AND %s=?;",
                TABLE_NAME, FIELDS[0], FIELDS[1]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, username);
        statement.setInt(2, humanId);

        ResultSet set = statement.executeQuery();
        return set.next() ? mapFrom(set) : null;
    }

    @Override
    public List<Association> getAssociationsByUsername(String username) throws SQLException {
        String sql = String.format(
                "SELECT * FROM %s WHERE %s=?;",
                TABLE_NAME, FIELDS[0]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, username);

        ResultSet set = statement.executeQuery();

        List<Association> associations = new ArrayList<>();
        while(set.next()) {
            associations.add(mapFrom(set));
        }
        return associations;
    }

    @Override
    public Association getAssociationByHumanId(int humanId) throws SQLException {
        String sql = String.format(
                "SELECT * FROM %s WHERE %s=?;",
                TABLE_NAME, FIELDS[1]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, humanId);

        ResultSet set = statement.executeQuery();
        return set.next() ? mapFrom(set) : null;
    }

    @Override
    public boolean removeByHumanId(int humanId) throws SQLException {
        String sql = String.format(
                "DELETE FROM %s WHERE %s=?;",
                TABLE_NAME, FIELDS[1]
        );
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, humanId);

        return statement.executeUpdate() > 0;
    }

    private static Association mapFrom(ResultSet set) throws SQLException {
        String username = set.getString(FIELDS[0]);
        int humanId = set.getInt(FIELDS[1]);
        return new Association(username, humanId);
    }
}
