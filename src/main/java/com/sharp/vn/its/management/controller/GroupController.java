package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.group.GroupDTO;
import com.sharp.vn.its.management.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Group controller.
 */
@RestController()
@RequestMapping(value = "/groups")
public class GroupController {
    /**
     * The Service.
     */
    @Autowired
    private GroupService service;

    /**
     * Load all groups data list.
     *
     * @return the list
     */
    @GetMapping()
    public List<GroupDTO> loadAllGroupsData() {
        return service.getAllGroupsData();
    }

    /**
     * Load all groups data page.
     *
     * @param request the request
     * @return the page
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/all")
    public Page<GroupDTO> loadAllGroupsData(@RequestBody GroupDTO request) {
        return service.loadAllGroupData(request);
    }

    /**
     * Save group dto.
     *
     * @param request the request
     * @return the group dto
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public GroupDTO saveGroup(@Valid @RequestBody GroupDTO request) {
        return service.saveGroup(request);
    }

    /**
     * Delete system response entity.
     *
     * @param groupId the group id
     * @return the response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable(required = true) Long groupId) {
        service.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

    /**
     * Update group group dto.
     *
     * @param groupId the group id
     * @param request the request
     * @return the group dto
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{groupId}")
    public GroupDTO updateGroup(@PathVariable(required = true) Long groupId,
                                  @Valid @RequestBody GroupDTO request) {
        return service.saveGroup(request);
    }

    /**
     * Gets group detail.
     *
     * @param groupId the group id
     * @return the group detail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{groupId}")
    public GroupDTO getGroupDetail(@PathVariable(required = true) Long groupId) {
        return service.getGroupDetail(groupId);
    }
}
