package urchin.util;

import org.junit.Test;
import urchin.model.folder.EncryptedFolder;
import urchin.model.folder.Folder;
import urchin.model.folder.ImmutableFolder;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EncryptedFolderUtilTest {

    @Test
    public void gettingEncryptedFolderFromFolderAndFolderFromEncryptedFolder() {
        Folder folder = ImmutableFolder.of(Paths.get(new File("").getAbsolutePath()));
        EncryptedFolder encryptedFolder = EncryptedFolderUtil.getEncryptedFolder(folder);
        Folder returnedFolder = EncryptedFolderUtil.getFolder(encryptedFolder);

        assertEquals(folder.toAbsolutePath(), returnedFolder.toAbsolutePath());
        assertNotEquals(folder.toAbsolutePath(), encryptedFolder.getPath().toAbsolutePath());

    }

}