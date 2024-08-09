package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.group.GroupDTO;
import com.sharp.vn.its.management.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(value = "/groups")
public class GroupController {
    @Autowired
    private GroupService service;

    @GetMapping()
    public List<GroupDTO> loadAllGroupsData() {
        return service.getAllGroupsData();
    }
}