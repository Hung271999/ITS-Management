package com.sharp.vn.its.management.service;


import com.sharp.vn.its.management.dto.group.GroupDTO;
import com.sharp.vn.its.management.repositories.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
