package basePackage;

import basePackage.connect.CollectionInfo;
import basePackage.objectModel.Human;

import java.io.IOException;
import java.util.List;

public interface Executor {

    CollectionInfo info();

    boolean remove(Integer id);

    boolean add(Human human);

    boolean addIfMax(Human human);

    boolean removeFirst();

    List<Human> show();

    boolean removeGreater(Human human);

    boolean loadCollection();

    void saveCollection() throws IOException;
}
