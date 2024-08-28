package com.sharp.vn.its.management.service;


import com.sharp.vn.its.management.constants.MessageCode;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.dto.group.GroupDTO;
import com.sharp.vn.its.management.entity.GroupEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.SortCriteria;
import com.sharp.vn.its.management.repositories.GroupRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * The type Group service.
 */
@Service
@Slf4j
public class GroupService {
    /**
     * The Group repository.
     */
    @Autowired
    private GroupRepository groupRepository;

    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The Service.
     */
    @Autowired
    private AuthenticationService service;

    /**
     * Gets all groups data.
     *
     * @return the all groups data
     */
    public List<GroupDTO> getAllGroupsData() {
        log.info("Fetching all groups...");
        final List<GroupDTO> groups = groupRepository.findAll().stream()
                .map(GroupDTO::new)
                .toList();
        log.info("All group fetched successfully.");
        return groups;
    }

    public Page<GroupDTO> loadAllGroupData(GroupDTO request) {
        Map<String, SortCriteria> sort = request.getFilter().getSort();
        buildSortCondition(sort);
        Page<GroupEntity> groupEntities = groupRepository.findByGroupNameContains(request.getFilter().getSearchKeyword(), request.getFilter().getPageable());
        return groupEntities.map(GroupDTO::new);
    }

    /**
     * Gets group detail.
     *
     * @param id the id
     * @return the group detail
     */
    public GroupDTO getGroupDetail(Long id) {
        if (id == null) {
            log.error("Group id not found");
            throw new DataValidationException(MessageCode.ERROR_GROUP_ID_NOT_FOUND);
        }
        log.info("Fetching group detail for id: {}", id);
        final GroupDTO groupDTO = new GroupDTO(groupRepository.findById(id).orElseThrow(() -> {
            log.error("Group not found");
            return new ObjectNotFoundException(MessageCode.ERROR_GROUP_NOT_FOUND);
        }));
        log.info("Group detail fetched successfully for id: {}", id);
        return groupDTO;
    }

    public void deleteGroup(Long id) {
        if (id == null) {
            log.error("Group id not found");
            throw new DataValidationException(MessageCode.ERROR_GROUP_ID_NOT_FOUND);
        }
//        if(taskRepository.existsBySystemId(id)){
//            log.error("Group with {} cannot delete because there are still users associated", id);
//            throw new DataValidationException(MessageCode.ERROR_SYSTEM_WITH_FOREIGN_KEY_TO_TASK);
//        }
        groupRepository.deleteById(id);
        log.info("Group with id {} deleted successfully.", id);
    }

    /**
     * Save group dto.
     *
     * @param groupDTO the group dto
     * @return the group dto
     */
    public GroupDTO saveGroup(GroupDTO groupDTO) {
        log.info("Saving group...");
        final Long groupId = groupDTO.getGroupId();
        GroupEntity groupEntity = new GroupEntity();

        // update when group id is not null
        if (groupId != null) {
            groupEntity = groupRepository.findById(groupId).orElseThrow(() -> {
                log.error("Group with id {} not found.", groupId);
                return new DataValidationException(MessageCode.ERROR_GROUP_ID_NOT_FOUND);
            });
        }

        final String userName = service.getUser().getUsername();
        if (userName == null) {
            throw new ObjectNotFoundException("User not found");
        }

        final UserEntity userEntity = userRepository.findById(service.getUser().getId()).get();
        // set properties
        BeanUtils.copyProperties(groupDTO, groupEntity);
        groupEntity.setGroupName(groupEntity.getGroupName());
        groupEntity.setCreatedBy(userEntity);
        groupEntity.setUpdatedBy(userEntity);
        groupEntity = groupRepository.save(groupEntity);
        log.info("Group saved successfully.");
        return new GroupDTO(groupEntity);
    }


    /**
     * Build sort condition.
     *
     * @param sort the sort
     */
    private void buildSortCondition(Map<String, SortCriteria> sort) {
        if (sort.isEmpty()) {
            sort.put("updatedDate", new SortCriteria("updatedDate", SortType.DESC.getText()));
            return;
        }
        sort.forEach((key, criteria) -> {
            switch (key) {
                case "groupId":
                    criteria.setFieldName("id");
                    break;
                case "groupName":
                    criteria.setFieldName("groupName");
                    break;
                case "createdDate":
                    criteria.setFieldName("createdDate");
                    break;
                case "updatedDate":
                    criteria.setFieldName("updatedDate");
                    break;
                case "updateBy":
                    criteria.setFieldName("updatedBy");
                    break;
                default:
                    break;
            }
        });
    }
}
