package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.service.SystemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Load all systems data by filter from the request.
     *
     * @return the list
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/all")
    public Page<SystemDTO> loadAllSystems(@RequestBody SystemDTO request) {
        return systemService.loadAllSystemData(request);
    }

    /**
     * Save system dto.
     *
     * @param request the request
     * @return the system dto
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public SystemDTO saveSystem(@Valid @RequestBody SystemDTO request) {
        return systemService.saveSystem(request);
    }

    /**
     * Delete system response entity.
     *
     * @param systemId the system id
     * @return the response entity
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{systemId}")
    public ResponseEntity<?> deleteSystem(@PathVariable(required = true) Long systemId) {
        systemService.deleteSystem(systemId);
        return ResponseEntity.ok().build();
    }

    /**
     * Update system dto.
     *
     * @param systemId the system id
     * @param request the request
     * @return the system dto
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{systemId}")
    public SystemDTO updateSystem(@PathVariable(required = true) Long systemId,
                              @Valid @RequestBody SystemDTO request) {
        return systemService.saveSystem(request);
    }

    /**
     * Gets system detail.
     *
     * @param systemId the system id
     * @return the system detail
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{systemId}")
    public SystemDTO getSystemDetail(@PathVariable(required = true) Long systemId) {
        return systemService.getSystemDetail(systemId);
    }
}
