package urchin.domain.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

import static java.util.Arrays.copyOf;
import static org.springframework.util.StringUtils.arrayToDelimitedString;

@Component
public class MountVirtualFolderCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger(MountVirtualFolderCommand.class);
    private static final String FOLDER_LIST = "%folderList%";
    private static final String VIRTUAL_FOLDER_PATH = "%virtualFolderPath%";

    private static final String[] COMMAND = new String[]{
            "mhddfs",
            "-o",
            "allow_other",
            FOLDER_LIST,
            VIRTUAL_FOLDER_PATH
    };

    @Autowired
    public MountVirtualFolderCommand(Runtime runtime) {
        super(runtime);
    }

    public void execute(List<Path> folders, Path virtualFolder) {
        LOG.debug("Mounting virtual folder {} for {} folders", virtualFolder.toAbsolutePath(), folders.size());
        executeCommand(setupCommand(folders, virtualFolder));
    }

    private String[] setupCommand(List<Path> folders, Path virtualFolder) {
        String[] command = copyOf(COMMAND, COMMAND.length);
        command[3] = arrayToDelimitedString(folders.toArray(), ",");
        command[4] = virtualFolder.toAbsolutePath().toString();
        return command;
    }
}