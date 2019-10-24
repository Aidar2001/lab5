package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;

import java.io.File;
import java.util.List;

public interface Client {
    RequestResult<Void> connect(String username, String host, int port);

    RequestResult<Boolean> login(String password);

    RequestResult<Boolean> register(String email);

    RequestResult<Void> closeConnection();

    RequestResult<Boolean> importFile(File file);

    RequestResult<Boolean> addHuman(Human human);

    RequestResult<Boolean> addIfMax(Human human);

    RequestResult<CollectionInfo> getInfo();

    RequestResult<List<Human>> getAllHumans();

    RequestResult<Boolean> remove(Integer id);

    RequestResult<Boolean> removeGreater(Human human);

    RequestResult<Boolean> removeFirst();

    //    RequestResult<Boolean> load(String collectionToLoad);
    RequestResult<Boolean> load();

    //    RequestResult<Boolean> save(String collectionToLoad);
    RequestResult<Void> save();
}
