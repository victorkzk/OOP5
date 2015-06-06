package plugin_adaptation;

import Pack.ArchivationPlugin;

import java.io.File;

public class ZipArchiver implements Archiver{

    ArchivationPlugin oldArchiver = new ArchivationPlugin();

    @Override
    public void archive(File initFile, File zipFile) {
        oldArchiver.packArchive(initFile.getAbsolutePath(), zipFile.getPath(), zipFile.getName());
    }

}
