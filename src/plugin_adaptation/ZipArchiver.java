package plugin_adaptation;

import Pack.ArchivationPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ZipArchiver implements Archiver{

    private static Logger logger = Logger.getLogger(ZipArchiver.class.getName());
    private ArchivationPlugin oldArchiver = new ArchivationPlugin();

    @Override
    public void archiveAndDelete(String filePath) {
        try {
            String pathWithoutExt = filePath.replaceFirst("[.][^.]+$", "");
            new File(pathWithoutExt + ".zip").createNewFile();
            oldArchiver.packArchive(pathWithoutExt + ".zip", new File(filePath).getName(), filePath);
            new File(filePath).delete();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException: " + e);
        }
    }

}
