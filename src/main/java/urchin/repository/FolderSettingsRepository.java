package urchin.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import urchin.exception.FolderNotFoundException;
import urchin.model.folder.*;

import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Repository
public class FolderSettingsRepository {

    private static final String INSERT_FOLDER_SETTINGS = "INSERT INTO folder_settings(encrypted_folder, folder, created, auto_mount) VALUES(:encryptedFolder,:folder,:created,:autoMount)";
    private static final String SELECT_FOLDER_SETTINGS = "SELECT * FROM folder_settings WHERE id = :folderId";
    private static final String SELECT_FOLDERS_SETTINGS = "SELECT * FROM folder_settings";
    private static final String DELETE_FOLDER_SETTINGS = "DELETE FROM folder_settings WHERE id = :folderId";

    private final Logger log = LoggerFactory.getLogger(FolderSettingsRepository.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public FolderSettingsRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public FolderSettings getFolderSettings(FolderId folderId) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("folderId", folderId.getValue());
            return namedParameterJdbcTemplate.queryForObject(SELECT_FOLDER_SETTINGS, params, (resultSet, i) -> folderSettingsMapper(resultSet));
        } catch (EmptyResultDataAccessException e) {
            throw new FolderNotFoundException("Invalid folderId " + folderId);
        }
    }

    public List<FolderSettings> getFoldersSettings() {
        return namedParameterJdbcTemplate.query(SELECT_FOLDERS_SETTINGS, new MapSqlParameterSource(), (resultSet, i) -> folderSettingsMapper(resultSet));
    }

    public FolderId saveFolderSettings(EncryptedFolder encryptedFolder, Folder folder) {
        log.info("Saving new folder settings for folder {}", folder);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("encryptedFolder", encryptedFolder.getPath().toAbsolutePath().toString())
                .addValue("folder", folder.toAbsolutePath())
                .addValue("created", new Timestamp(new Date().getTime()))
                .addValue("autoMount", false);

        namedParameterJdbcTemplate.update(INSERT_FOLDER_SETTINGS, parameters, keyHolder);

        int folderId = Optional.ofNullable(keyHolder.getKey())
                .map(Number::intValue)
                .orElseThrow(() -> new RuntimeException(format("Failed to save folder settings for folder %s", folder)));

        return FolderId.of(folderId);
    }

    public void removeFolderSettings(FolderId folderId) {
        log.info("Removing folder settings for id {}", folderId);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("folderId", folderId.getValue());

        namedParameterJdbcTemplate.update(DELETE_FOLDER_SETTINGS, params);
    }

    private FolderSettings folderSettingsMapper(ResultSet resultSet) throws SQLException {
        return ImmutableFolderSettings.builder()
                .folderId(FolderId.of(resultSet.getInt("id")))
                .folder(ImmutableFolder.of(Paths.get(resultSet.getString("folder"))))
                .encryptedFolder(ImmutableEncryptedFolder.of(Paths.get(resultSet.getString("encrypted_folder"))))
                .isAutoMount(resultSet.getBoolean("auto_mount"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .build();
    }
}
