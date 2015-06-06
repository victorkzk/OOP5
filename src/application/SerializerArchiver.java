package application;

import com.victorkzk.furniture.Serializer;
import plugin_adaptation.Archiver;
import plugin_adaptation.ZipArchiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public class SerializerArchiver implements Serializer{

    private Serializer serializer;
    private Archiver archiver = new ZipArchiver();
    private boolean enableArchiver = false;

    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void serialize(String filePath, List<Object> objectList) throws FileNotFoundException {
        serializer.serialize(filePath, objectList);
        if (enableArchiver) {
            archiver.archiveAndDelete(filePath);
        }
    }

    @Override
    public String getFileExtension() {
        return serializer.getFileExtension();
    }

    public void setArchiverEnable(boolean value) {
        enableArchiver = value;
    }
}
