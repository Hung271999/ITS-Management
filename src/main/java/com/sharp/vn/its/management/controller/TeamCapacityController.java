package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.capacity.TeamCapacityDTO;
import com.sharp.vn.its.management.service.TeamCapacityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "team-capacities")
public class TeamCapacityController {

    @Autowired
    private TeamCapacityService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/all")
    public Page<TeamCapacityDTO> loadAllTeamCapacities(@RequestBody TeamCapacityDTO request) {
        return service.loadAllTeamCapacitiesData(request);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping
//    public TeamCapacityDTO saveTeamCapacity(@Valid @RequestBody TeamCapacityDTO request) {
//        return service.save(request);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{capacityId}")
    public ResponseEntity<?> deleteTeamCapacity(@PathVariable(required = true) Long capacityId) {
        service.deleteTeamCapacity(capacityId);
        return ResponseEntity.ok().build();
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/{capacityId}")
//    public SystemDTO updateTeamCapacity(@PathVariable(required = true) Long capacityId,
//                                  @Valid @RequestBody SystemDTO request) {
//        return service.save(request);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{capacityId}")
    public TeamCapacityDTO getSystemDetail(@PathVariable(required = true) Long capacityId) {
        return service.getTeamCapacityDetail(capacityId);
    }
}
