package urchin.domain;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class FolderSettings {

    private int id;
    private final Path folder;
    private final EncryptedFolder encryptedFolder;
    private LocalDateTime created;
    private boolean automount;

    public FolderSettings(Path folder, EncryptedFolder encryptedFolder) {
        this.folder = folder;
        this.encryptedFolder = encryptedFolder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Path getFolder() {
        return folder;
    }

    public EncryptedFolder getEncryptedFolder() {
        return encryptedFolder;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isAutomount() {
        return automount;
    }

    public void setAutomount(boolean automount) {
        this.automount = automount;
    }
}
