package urchin.domain.cli;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import urchin.domain.model.ImmutableUser;
import urchin.domain.model.ImmutableUserId;
import urchin.domain.model.User;
import urchin.testutil.CliTestConfiguration;
import urchin.testutil.UnixUserAndGroupCleanup;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static urchin.testutil.UnixUserAndGroupCleanup.USERNAME_PREFIX;

@RunWith(SpringRunner.class)
@Import(CliTestConfiguration.class)
public class UserCliIT {

    private static final String PASSWORD = "superSecret";

    @Rule
    @Autowired
    public UnixUserAndGroupCleanup unixUserAndGroupCleanup;

    @Autowired
    private UserCli userCli;

    private User user;
    private String username;

    @Before
    public void setUp() {
        username = USERNAME_PREFIX + System.currentTimeMillis();
        user = ImmutableUser.builder()
                .userId(ImmutableUserId.of(1))
                .username(username)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    public void addUserAndSetUserPasswordAndCheckIfUsernameExistAndRemoveUserAreExecutedSuccessfully() {
        userCli.addUser(username);
        userCli.setSetUserPassword(username, PASSWORD);

        assertTrue(userCli.checkIfUsernameExist(username));

        userCli.removeUser(username);

        assertFalse(userCli.checkIfUsernameExist(username));
    }

    @Test
    public void listUsersReturnsListOfUsers() {
        List<String> strings = userCli.listUsers();

        assertFalse(strings.isEmpty());
    }

    @Test
    public void listGroupsForUserReturnsGroups() {
        userCli.addUser(user.getUsername());

        List<String> groups = userCli.listGroupsForUser(user);

        assertEquals(1, groups.size());
    }

}