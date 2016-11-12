package urchin.api.mapper;

import urchin.api.AddGroupDto;
import urchin.api.GroupDto;
import urchin.api.GroupsDto;
import urchin.domain.model.Group;

import java.util.List;
import java.util.stream.Collectors;

public class GroupMapper {

    public static Group mapToGroup(AddGroupDto addGroupDto) {
        return new Group(addGroupDto.getName());
    }

    public static GroupsDto mapToGroupsDto(List<Group> groups) {
        return new GroupsDto(
                groups.stream()
                        .map(GroupMapper::mapToGroupDto)
                        .collect(Collectors.toList())
        );
    }

    private static GroupDto mapToGroupDto(Group group) {
        return new GroupDto(
                group.getGroupId().getId(),
                group.getName()
        );
    }
}
