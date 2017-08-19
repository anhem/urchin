package urchin.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import urchin.controller.api.*;
import urchin.domain.model.GroupId;
import urchin.domain.model.UserId;
import urchin.testutil.TestApplication;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static urchin.controller.UserControllerIT.PASSWORD;
import static urchin.testutil.UnixUserAndGroupCleanup.GROUP_PREFIX;
import static urchin.testutil.UnixUserAndGroupCleanup.USERNAME_PREFIX;

public class GroupControllerIT extends TestApplication {

    private UserId userId;

    @Before
    public void setup() {
        userId = new UserId(addUserRequest(new AddUserDto(USERNAME_PREFIX + System.currentTimeMillis(), PASSWORD)).getBody().getId());
    }

    @After
    public void tearDown() {
        removeUserRequest(userId.getId());
    }

    @Test
    public void addGroupAndAddUserToGroupAndRemoveUseFromGroupAndRemoveGroup() {

        //Add group

        AddGroupDto addGroupDto = new AddGroupDto(GROUP_PREFIX + System.currentTimeMillis());

        ResponseEntity<IdDto> addGroupResponse = addGroupRequest(addGroupDto);

        assertEquals(HttpStatus.OK, addGroupResponse.getStatusCode());
        GroupId groupId = new GroupId(addGroupResponse.getBody().getId());
        assertTrue(groupId.getId() > 0);

        //Get groups

        ResponseEntity<GroupDto[]> groupsResponse = getGroupsRequest();

        assertEquals(HttpStatus.OK, groupsResponse.getStatusCode());
        List<GroupDto> groups = asList(groupsResponse.getBody());
        assertFalse(groups.isEmpty());
        List<GroupDto> groupDtos = groups.stream()
                .filter(groupDto -> groupDto.getName().equals(addGroupDto.getName()))
                .collect(Collectors.toList());
        assertEquals(1, groupDtos.size());
        assertEquals(groupId.getId(), groupDtos.get(0).getGroupId());
        assertEquals(addGroupDto.getName(), groupDtos.get(0).getName());

        //Add user to group

        AddUserToGroupDto addUserToGroupDto = new AddUserToGroupDto(groupId.getId(), userId.getId());
        ResponseEntity<MessageDto> addUserToGroupResponse = addUserToGroupRequest(addUserToGroupDto);

        assertEquals(HttpStatus.OK, addUserToGroupResponse.getStatusCode());

        //Remove user from group

        ResponseEntity<MessageDto> removeUserFromGroupResponse = removeUserFromGroupRequest(userId, groupId);

        assertEquals(HttpStatus.OK, removeUserFromGroupResponse.getStatusCode());

        //Remove group

        ResponseEntity<MessageDto> removeGroupResponse = removeGroupRequest(groupDtos.get(0).getGroupId());

        assertEquals(HttpStatus.OK, removeGroupResponse.getStatusCode());
    }

    private ResponseEntity<IdDto> addGroupRequest(AddGroupDto addGroupDto) {
        return testRestTemplate.postForEntity(discoverControllerPath() + "/add", addGroupDto, IdDto.class);
    }

    private ResponseEntity<GroupDto[]> getGroupsRequest() {
        return testRestTemplate.getForEntity(discoverControllerPath(), GroupDto[].class);
    }

    private ResponseEntity<MessageDto> removeGroupRequest(int groupId) {
        return testRestTemplate.exchange(discoverControllerPath() + "/" + groupId, HttpMethod.DELETE, null, new ParameterizedTypeReference<MessageDto>() {
        });
    }

    private ResponseEntity<MessageDto> addUserToGroupRequest(AddUserToGroupDto addUserToGroupDto) {
        return testRestTemplate.postForEntity(discoverControllerPath() + "/user", addUserToGroupDto, MessageDto.class);
    }

    private ResponseEntity<MessageDto> removeUserFromGroupRequest(UserId userId, GroupId groupId) {
        return testRestTemplate.exchange(discoverControllerPath() + "/" + groupId.getId() + "/user/" + userId.getId(), HttpMethod.DELETE, null, new ParameterizedTypeReference<MessageDto>() {
        });
    }

    private ResponseEntity<IdDto> addUserRequest(AddUserDto addUserDto) {
        return testRestTemplate.postForEntity(discoverControllerPath(UserController.class) + "/add", addUserDto, IdDto.class);
    }

    private ResponseEntity<MessageDto> removeUserRequest(int userId) {
        return testRestTemplate.exchange(discoverControllerPath(UserController.class) + "/" + userId, HttpMethod.DELETE, null, new ParameterizedTypeReference<MessageDto>() {
        });
    }
}