package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;

import java.io.File;
import java.util.List;

public interface Client {
    RequestResult<Void> connect(String host, int port);

    RequestResult<Void> importFile(File file);

    RequestResult<Void> addHuman(Human human);

    RequestResult<CollectionInfo> getInfo();

    RequestResult<List<Human>> getAllHumans();
}
