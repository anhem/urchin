package urchin.domain;

import org.junit.Before;
import org.junit.Test;
import urchin.testutil.H2Application;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FolderSettingsRepositoryTest extends H2Application {

    private FolderSettingsRepository folderSettingsRepository;

    @Before
    public void setup() {
        folderSettingsRepository = new FolderSettingsRepository(jdbcTemplate);
    }

    @Test
    public void crd() {
        LocalDateTime now = LocalDateTime.now();
        FolderSettings folderSettings = new FolderSettings(Paths.get("/some/path"), new EncryptedFolder(Paths.get("/some/.path")));
        folderSettingsRepository.saveFolderSettings(folderSettings);
        List<FolderSettings> allFolderSettings = folderSettingsRepository.getAllFolderSettings();

        assertEquals(1, allFolderSettings.size());
        FolderSettings readFolderSettings = allFolderSettings.get(0);
        assertTrue(readFolderSettings.getId() > 0);
        assertEquals(folderSettings.getFolder(), readFolderSettings.getFolder());
        assertEquals(folderSettings.getEncryptedFolder(), readFolderSettings.getEncryptedFolder());
        assertTrue(now.isBefore(readFolderSettings.getCreated()));

        folderSettingsRepository.removeFolderSettings(readFolderSettings.getId());

        assertEquals(0, folderSettingsRepository.getAllFolderSettings().size());
    }

}