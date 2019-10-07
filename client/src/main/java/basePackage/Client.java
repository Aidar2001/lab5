package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;

import java.io.File;
import java.util.List;

public interface Client {
    RequestResult<Void> connection(String host, int port);

    RequestResult<Void> closeConnection();

    RequestResult<Void> importFile(File file);

    RequestResult<Void> addHuman(Human human);

    RequestResult<Void> addIfMax(Human human);

    RequestResult<CollectionInfo> getInfo();

    RequestResult<List<Human>> getAllHumans();

    RequestResult<Boolean> remove(Integer id);

    RequestResult<Boolean> removeGreater(Human human);

    RequestResult<Boolean> removeFirst();

    RequestResult<Boolean> load(String collectionToLoad);

    RequestResult<Boolean> save(String collectionToLoad);
}
