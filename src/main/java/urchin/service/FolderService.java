package urchin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import urchin.domain.EncryptedFolder;
import urchin.domain.Passphrase;
import urchin.shell.MountEncryptedFolderShellCommand;
import urchin.shell.MountVirtualFolderShellCommand;
import urchin.shell.UmountFolderShellCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.createDirectories;
import static urchin.util.EncryptedFolderUtil.getEncryptedFolder;
import static urchin.util.EncryptedFolderUtil.getFolder;
import static urchin.util.PassphraseGenerator.generateEcryptfsPassphrase;

public class FolderService {

    private static final Logger LOG = LoggerFactory.getLogger(FolderService.class);

    private final MountEncryptedFolderShellCommand mountEncryptedFolderShellCommand;
    private final MountVirtualFolderShellCommand mountVirtualFolderShellCommand;
    private final UmountFolderShellCommand umountFolderShellCommand;

    @Autowired
    public FolderService(
            MountEncryptedFolderShellCommand mountEncryptedFolderShellCommand,
            MountVirtualFolderShellCommand mountVirtualFolderShellCommand,
            UmountFolderShellCommand umountFolderShellCommand
    ) {
        this.mountEncryptedFolderShellCommand = mountEncryptedFolderShellCommand;
        this.mountVirtualFolderShellCommand = mountVirtualFolderShellCommand;
        this.umountFolderShellCommand = umountFolderShellCommand;
    }

    public Passphrase createAndMountEncryptedFolder(Path folder) throws IOException {
        EncryptedFolder encryptedFolder = getEncryptedFolder(folder);
        createEncryptionFolderPair(folder, encryptedFolder);
        Passphrase passphrase = generateEcryptfsPassphrase();
        mountEncryptedFolderShellCommand.execute(folder, encryptedFolder, passphrase);
        return passphrase;
    }

    public void mountEncryptedFolder(EncryptedFolder encryptedFolder, Passphrase passphrase) throws IOException {
        Path folder = getFolder(encryptedFolder);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
            mountEncryptedFolderShellCommand.execute(folder, encryptedFolder, passphrase);
        } else {
            throw new IllegalStateException(String.format("Folder %s should not exist", folder));
        }
    }

    public void umountEncryptedFolder(Path folder) throws IOException {
        umountFolderShellCommand.execute(folder);
        if (folder.toFile().list().length == 0) {
            LOG.info("Deleting empty folder {}", folder.toAbsolutePath());
            Files.delete(folder);
        } else {
            throw new RuntimeException("Something went wrong during umount");
        }
    }

    public void setupVirtualFolder(List<Path> folders, Path virtualFolder) throws IOException {
        createVirtualFolder(virtualFolder);
        mountVirtualFolderShellCommand.execute(folders, virtualFolder);
    }


    private void createVirtualFolder(Path virtualFolder) throws IOException {
        if (!Files.exists(virtualFolder)) {
            createDirectories(virtualFolder);
        }
    }

    private void createEncryptionFolderPair(Path folder, EncryptedFolder encryptedFolder) throws IOException {
        if (!Files.exists(folder) && !Files.exists(encryptedFolder.getPath())) {
            LOG.info("Creating folder pair {} - {}", folder, encryptedFolder);
            createDirectories(folder);
            createDirectories(encryptedFolder.getPath());
        } else {
            LOG.warn("Folder pair already exist");
        }
    }
}
