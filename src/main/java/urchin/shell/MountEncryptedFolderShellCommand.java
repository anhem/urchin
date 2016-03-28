package urchin.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import urchin.domain.EncryptedFolder;
import urchin.domain.Passphrase;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.Arrays;

@Repository
public class MountEncryptedFolderShellCommand {

    private static final Logger LOG = LoggerFactory.getLogger(MountEncryptedFolderShellCommand.class);
    private static final String ENCRYPTED_FOLDER_PATH = "%encryptedFolderPath%";
    private static final String FOLDER_PATH = "%folderPath%";
    private static final String PASSPHRASE = "%passphrase%";

    private static final String[] COMMAND = new String[]{"sudo", "mount", "-t", "ecryptfs", ENCRYPTED_FOLDER_PATH, FOLDER_PATH, "-o", "key=passphrase:passphrase_passwd=" + PASSPHRASE + ",ecryptfs_cipher=aes,ecryptfs_key_bytes=16,ecryptfs_passthrough=n,no_sig_cache=n,ecryptfs_enable_filename_crypto=y"};

    private final Runtime runtime;

    @Autowired
    public MountEncryptedFolderShellCommand(Runtime runtime) {
        this.runtime = runtime;
    }

    public void execute(Path folder, EncryptedFolder encryptedFolder, Passphrase passphrase) {
        LOG.info("Setting up encrypted folder {} and mounting it to {}", encryptedFolder.getPath(), folder);
        String[] command = setupCommand(folder, encryptedFolder, passphrase);
        try {
            Process process = runtime.exec(command);
            OutputStream outputStream = process.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.newLine();
            bufferedWriter.flush();
            process.waitFor();
            if (process.exitValue() != 0) {
                throw new ShellCommandException("Process returned code: " + process.exitValue());
            }
        } catch (Exception e) {
            LOG.error("Failed to execute command");
            throw new ShellCommandException(e);
        }
    }

    private String[] setupCommand(Path folder, EncryptedFolder encryptedFolder, Passphrase passphrase) {
        String[] command = Arrays.copyOf(COMMAND, COMMAND.length);
        command[4] = encryptedFolder.getPath().toAbsolutePath().toString();
        command[5] = folder.toAbsolutePath().toString();
        command[7] = command[7].replace(PASSPHRASE, passphrase.getPassphrase());
        return command;
    }
}
