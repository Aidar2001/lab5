package basePackage.database.repository;

import basePackage.database.model.Association;

import java.sql.SQLException;
import java.util.List;

public interface AssociationRepository {

    void createTable() throws SQLException;

    boolean createAssociation(Association association) throws SQLException;

    Association getAssociationByKey(String username, int humanId) throws SQLException;

    List<Association> getAssociationsByUsername(String username) throws SQLException;

    Association getAssociationByHumanId(int humanId) throws SQLException;

    boolean removeByHumanId(int humanId) throws SQLException;
}
