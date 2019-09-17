package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.connect.RequestResult;
import basePackage.objectModel.Human;

import java.io.File;
import java.util.List;

public interface Client {
    public RequestResult<Void> connect(String host, int port);

    public RequestResult<Void> importFile(File file);

    public RequestResult<Void> addHuman(Human human);

    public RequestResult<CollectionInfo> getInfo();

    public RequestResult<List<Human>> getAllHumans();
}
