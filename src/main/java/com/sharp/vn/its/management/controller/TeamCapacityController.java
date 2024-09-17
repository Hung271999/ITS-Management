package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.capacity.TeamCapacityDTO;
import com.sharp.vn.its.management.service.TeamCapacityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("team-capacities")
public class TeamCapacityController {

    @Autowired
    private TeamCapacityService teamCapacityService;

    // Get all team capacities
    @GetMapping
    public ResponseEntity<List<TeamCapacityDTO>> getAllTeamCapacities() {
        List<TeamCapacityDTO> teamCapacities = teamCapacityService.getAllTeamCapacities();
        return new ResponseEntity<>(teamCapacities, HttpStatus.OK);
    }

    // Get team capacity by ID
    @GetMapping("/{id}")
    public ResponseEntity<TeamCapacityDTO> getTeamCapacityById(@PathVariable long id) {
        TeamCapacityDTO teamCapacity = teamCapacityService.getTeamCapacityById(id);
        if (teamCapacity != null) {
            return new ResponseEntity<>(teamCapacity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create or update team capacity
    @PostMapping
    public ResponseEntity<TeamCapacityDTO> saveTeamCapacity(@RequestBody TeamCapacityDTO dto) {
        TeamCapacityDTO savedTeamCapacity = teamCapacityService.saveTeamCapacity(dto);
        return new ResponseEntity<>(savedTeamCapacity, HttpStatus.CREATED);
    }

    // Delete team capacity by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeamCapacity(@PathVariable long id) {
        teamCapacityService.deleteTeamCapacity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Upload Excel file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Please to a file to upload!", HttpStatus.BAD_REQUEST);
        }
        teamCapacityService.uploadFileExcel(file);
        return new ResponseEntity<>("Uploaded and processed excel file successfully!", HttpStatus.OK);
    }
}
