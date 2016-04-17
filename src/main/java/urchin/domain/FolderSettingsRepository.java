package urchin.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import urchin.domain.mapper.FolderSettingsRowMapper;

import java.util.Date;
import java.util.List;

@Repository
public class FolderSettingsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(FolderSettingsRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FolderSettingsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveFolderSettings(FolderSettings folderSettings) {
        LOG.info("Saving new folder settings for folder {}", folderSettings.getFolder());
        jdbcTemplate.update("INSERT INTO folder_settings(encrypted_folder, folder, created, automount) VALUES(?,?,?, ?)",
                folderSettings.getEncryptedFolder().getPath().toAbsolutePath().toString(), folderSettings.getFolder().toAbsolutePath().toString(), new Date(), folderSettings.isAutomount());
    }

    public List<FolderSettings> getAllFolderSettings() {
        return jdbcTemplate.query("SELECT * FROM folder_settings", new FolderSettingsRowMapper());
    }

    public void removeFolderSettings(int id) {
        LOG.info("Removing folder settings for id {}", id);
        jdbcTemplate.update("DELETE FROM folder_settings WHERE id = ?", id);
    }
}
