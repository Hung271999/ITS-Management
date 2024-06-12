package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type System controller.
 */
@RestController
@RequestMapping(value = "/systems")
public class SystemController {

    /**
     * The System service.
     */
    @Autowired
    private SystemService systemService;

    /**
     * Load all systems data list.
     *
     * @return the list
     */
    @GetMapping()
    public List<SystemDTO> loadAllSystemsData() {
        return systemService.getAllSystemsData();
    }
}
