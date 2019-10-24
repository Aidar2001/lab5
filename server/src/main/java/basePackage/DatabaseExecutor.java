package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.database.model.Association;
import basePackage.database.model.DatabaseHuman;
import basePackage.database.repository.AssociationRepository;
import basePackage.database.repository.HumanRepository;
import basePackage.objectModel.*;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseExecutor implements Executor {

    private static final String COLLECTION_NAME = "Default";
    private static final String COLLECTION_TYPE = "Database";

    private Date initializationDate = new Date();

    private String username;

    private HumanRepository humans;
    private AssociationRepository associations;

    public DatabaseExecutor(String username, HumanRepository humans, AssociationRepository associations) {
        this.username = username;
        this.humans = humans;
        this.associations = associations;
    }

    @Override
    public CollectionInfo info() {
        try {
            int size = humans.getAllHumans().size();
            return new CollectionInfo()
                    .withCollectionName(COLLECTION_NAME)
                    .withCollectionType(COLLECTION_TYPE)
                    .withInitializationDate(initializationDate)
                    .withCollectionSize(size);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean remove(Integer id) {
        try {
            Association association = associations.getAssociationByHumanId(id);
            if (association != null && !association.getUsername().equals(username)) {
                return false;
            }

            boolean humanRemove = humans.removeHuman(id);
            boolean associationRemove = true;
            if (humanRemove) {
                associationRemove = associations.removeByHumanId(id);
            }

            return humanRemove;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean add(Human human) {
        try {
            int id = humans.createHuman(mapFrom(human));
            if (id == -1) {
                return false;
            }

            return associations.createAssociation(
                    new Association()
                            .withUsername(username)
                            .withHumanId(id)
            );
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean addIfMax(Human human) {
        try {
            boolean isMax = humans.getAllHumans().stream()
                    .map(DatabaseExecutor::mapFrom)
                    .allMatch(collectionHuman -> collectionHuman.compareTo(human) < 0);
            if (isMax) {
                int id = humans.createHuman(mapFrom(human));
                if (id == -1) {
                    return false;
                }

                return associations.createAssociation(
                        new Association()
                                .withUsername(username)
                                .withHumanId(id)
                );
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean removeFirst() {
        try {
            List<Human> humansList = humans.getAllHumans().stream()
                    .map(DatabaseExecutor::mapFrom)
                    .sorted()
                    .collect(Collectors.toList());
            if (humansList.size() == 0)
                return false;
            else {
                return remove(humansList.get(0).getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Human> show() {
        try {
            return humans.getAllHumans().stream()
                    .map(DatabaseExecutor::mapFrom)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeGreater(Human human) {
        try {
            List<Integer> humanIds = associations.getAssociationsByUsername(username).stream()
                    .map(Association::getHumanId)
                    .collect(Collectors.toList());

            boolean allSuccess = true;
            for (Integer humanId : humanIds) {
                Human ownedHuman = mapFrom(humans.getHumanById(humanId));

                if (ownedHuman.compareTo(human) > 0) {
                    allSuccess &= remove(humanId);
                }
            }
            return allSuccess;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean loadCollection() {
        try {
            return humans.getAllHumans().size() != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveCollection() {

    }

    private static Human mapFrom(DatabaseHuman human) {
        List<IAction> actions = human.getActionNames() != null
                ? human.getActionNames()
                .stream()
                .filter(str -> !str.trim().isEmpty())
                .map(Action::new)
                .collect(Collectors.toList()) : new ArrayList<>();

        List<IProfession> professions = human.getProfessions() != null
                ? human.getProfessions()
                .stream()
                .filter(str -> !str.trim().isEmpty())
                .map(Profession::new).collect(Collectors.toList())
                : new ArrayList<>();

        ZonedDateTime creationDate = human.getCreationTime() != null ? ZonedDateTime.parse(human.getCreationTime()) : null;

        Location location = human.getLocationName() != null
                ? Location.valueOf(human.getLocationName())
                : null;

        return new Human(
                human.getId(),
                human.getName(),
                location,
                actions,
                professions,
                creationDate
        );
    }

    private static DatabaseHuman mapFrom(Human human) {
        return new DatabaseHuman()
                .withId(human.getId())
                .withName(human.getName())
                .withLocationName(human.getLocation() != null ? human.getLocation().toString() : null)
                .withActionNames(human.getActions() != null ? human.getActions().stream().map(Object::toString).collect(Collectors.toList()) : null)
                .withProfessions(human.getProfessions() != null ? human.getProfessions().stream().map(Object::toString).collect(Collectors.toList()) : null)
                .withCreationTime(human.getCreationTime() != null ? human.getCreationTime().toString() : null);
    }
}
