package com.sharp.vn.its.management.dto.group;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.GroupEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Group dto.
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO {
    /**
     * The groupId.
     */
    private Long groupId;

    /**
     * The groupName.
     */
    private String groupName;

    /**
     * Instantiates a new Group dto.
     *
     * @param group the group
     */
    public GroupDTO(GroupEntity group) {
        this.groupName = group.getGroupName();
        this.groupId = group.getId();
    }
}