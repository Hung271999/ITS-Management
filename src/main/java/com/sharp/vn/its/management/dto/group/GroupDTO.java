package com.sharp.vn.its.management.dto.group;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.sharp.vn.its.management.entity.GroupEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.util.DateTimeUtil;
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
     * The Update by.
     */
    private String updateBy;

    /**
     * The Created date.
     */
    private Long createdDate;

    /**
     * The Updated date.
     */
    private Long updatedDate;

    /**
     * The Filter.
     */
    private CriteriaSearchRequest filter = new CriteriaSearchRequest();

    /**
     * Instantiates a new Group dto.
     *
     * @param group the group
     */
    public GroupDTO(GroupEntity group) {
        this.groupName = group.getGroupName();
        this.groupId = group.getId();
        this.createdDate = DateTimeUtil.toEpochMilli(group.getCreatedDate());
        this.updatedDate = DateTimeUtil.toEpochMilli(group.getUpdatedDate());
        this.updateBy = group.getUpdatedBy().getFullName();
    }
}
