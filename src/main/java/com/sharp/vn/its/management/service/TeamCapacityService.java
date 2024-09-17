package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.dto.capacity.TeamCapacityDTO;
import com.sharp.vn.its.management.entity.TeamCapacityEntity;
import com.sharp.vn.its.management.repositories.TeamCapacityRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
public class TeamCapacityService {

    @Autowired
    private TeamCapacityRepository repository;

    // Convert entity to DTO
    private TeamCapacityDTO convertToDTO(TeamCapacityEntity entity) {
        TeamCapacityDTO dto = new TeamCapacityDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setHeadCount(entity.getHeadCount());
        dto.setTotalHours(entity.getTotalHours());
        dto.setTimeOff(entity.getTimeOff());
        dto.setReports(entity.getReports());
        dto.setActualCapacity(entity.getActualCapacity());
        dto.setNote(entity.getNote());
        return dto;
    }

    // Convert DTO to entity
    private TeamCapacityEntity convertToEntity(TeamCapacityDTO dto) {
        TeamCapacityEntity entity = new TeamCapacityEntity();
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setHeadCount(dto.getHeadCount());
        entity.setTotalHours(dto.getTotalHours());
        entity.setTimeOff(dto.getTimeOff());
        entity.setReports(dto.getReports());
        entity.setActualCapacity(dto.getActualCapacity());
        entity.setNote(dto.getNote());
        return entity;
    }

    // Get all team capacities
    public List<TeamCapacityDTO> getAllTeamCapacities() {
        List<TeamCapacityEntity> entities = repository.findAll();
        return entities.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get team capacity by ID
    public TeamCapacityDTO getTeamCapacityById(long id) {
        Optional<TeamCapacityEntity> entity = repository.findById(id);
        return entity.map(this::convertToDTO).orElse(null);
    }

    // Create or update team capacity
    public TeamCapacityDTO saveTeamCapacity(TeamCapacityDTO dto) {
        TeamCapacityEntity entity = convertToEntity(dto);
        entity = repository.save(entity);
        return convertToDTO(entity);
    }

    // Delete team capacity by ID
    public void deleteTeamCapacity(long id) {
        repository.deleteById(id);
    }


    public void uploadFileExcel(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

        Iterator<Row> rowIterator = sheet.iterator();
        List<TeamCapacityEntity> teamCapacityList = new ArrayList<>();
        Set<String> otherSystems = new HashSet<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            TeamCapacityEntity teamCapacity = new TeamCapacityEntity();
            try {
                teamCapacity.setId((long) row.getCell(0).getNumericCellValue());
                if (row.getCell(1) != null) {
                    Date startDate = row.getCell(1).getDateCellValue();
                    LocalDateTime startDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    teamCapacity.setStartDate(startDateTime);
                }
                if (row.getCell(2) != null ) {
                    Date endDate = row.getCell(1).getDateCellValue();
                    LocalDateTime endDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    teamCapacity.setEndDate(endDateTime);
                }
                teamCapacity.setHeadCount(row.getCell(3).getRowIndex());
                teamCapacity.setTotalHours(row.getCell(4).getNumericCellValue());
                teamCapacity.setTimeOff(row.getCell(5).getNumericCellValue());
                teamCapacity.setReports(row.getCell(6).getNumericCellValue());
                teamCapacity.setActualCapacity(row.getCell(7).getNumericCellValue());
                teamCapacity.setNote(row.getCell(12).getStringCellValue());
                teamCapacityList.add(teamCapacity);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        repository.saveAll(teamCapacityList);
        log.info(otherSystems.toString());
        workbook.close();
    }
}

