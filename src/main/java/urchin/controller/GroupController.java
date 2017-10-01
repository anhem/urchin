package urchin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import urchin.controller.api.IdDto;
import urchin.controller.api.ImmutableIdDto;
import urchin.controller.api.ImmutableMessageDto;
import urchin.controller.api.MessageDto;
import urchin.controller.api.group.AddGroupDto;
import urchin.controller.api.group.AddUserToGroupDto;
import urchin.controller.api.group.GroupDto;
import urchin.model.Group;
import urchin.model.GroupId;
import urchin.model.GroupName;
import urchin.model.UserId;
import urchin.service.GroupService;

import javax.validation.Valid;
import java.util.List;

import static urchin.controller.api.mapper.GroupMapper.mapToGroupDto;
import static urchin.controller.api.mapper.GroupMapper.mapToGroupsDto;

@RestController
@RequestMapping("api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<GroupDto> getGroups() {
        List<Group> groups = groupService.getGroups();
        return mapToGroupsDto(groups);
    }

    @RequestMapping(value = "{groupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GroupDto getGroup(@PathVariable int groupId) {
        return mapToGroupDto(groupService.getGroup(GroupId.of(groupId)));
    }

    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public IdDto addGroup(@Valid @RequestBody AddGroupDto addGroupDto) {
        GroupId groupId = groupService.addGroup(GroupName.of(addGroupDto.getGroupName()));
        return ImmutableIdDto.of(groupId.getValue());
    }

    @RequestMapping(value = "{groupId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MessageDto removeGroup(@PathVariable int groupId) {
        groupService.removeGroup(GroupId.of(groupId));
        return ImmutableMessageDto.of("Group removed");
    }

    @RequestMapping(value = "user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MessageDto addUserToGroup(@Valid @RequestBody AddUserToGroupDto addUserToGroupDto) {
        groupService.addUserToGroup(
                UserId.of(addUserToGroupDto.getUserId()),
                GroupId.of(addUserToGroupDto.getGroupId())
        );
        return ImmutableMessageDto.of("User added to group");
    }

    @RequestMapping(value = "{groupId}/user/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MessageDto removeUserFromGroup(@PathVariable int groupId, @PathVariable int userId) {
        groupService.removeUserFromGroup(UserId.of(userId), GroupId.of(groupId));
        return ImmutableMessageDto.of("User removed from group");
    }

}
