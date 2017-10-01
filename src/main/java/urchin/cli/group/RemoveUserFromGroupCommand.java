package urchin.cli.group;


import org.springframework.stereotype.Component;
import urchin.cli.common.BasicCommand;
import urchin.model.group.Group;
import urchin.model.user.User;

import static java.util.Arrays.copyOf;

@Component
public class RemoveUserFromGroupCommand extends BasicCommand {

    private static final String USERNAME = "%username%";
    private static final String GROUP = "%group%";


    private static final String[] COMMAND = new String[]{
            "sudo",
            "deluser",
            USERNAME,
            GROUP
    };

    public RemoveUserFromGroupCommand(Runtime runtime) {
        super(runtime);
    }

    public void execute(User user, Group group) {
        LOG.info("Removing user {} to group {}", user, group);
        executeCommand(setupCommand(user, group));
    }

    private String[] setupCommand(User user, Group group) {
        String[] command = copyOf(COMMAND, COMMAND.length);
        command[2] = user.getUsername().getValue();
        command[3] = group.getName().getValue();
        return command;
    }
}