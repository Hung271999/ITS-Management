package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.constants.MessageCode;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.dto.capacity.TeamCapacityDTO;
import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.TeamCapacityEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.SortCriteria;
import com.sharp.vn.its.management.repositories.TeamCapacityRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Team capacity service.
 */
@Service
@Slf4j
public class TeamCapacityService {

    /**
     * The Repository.
     */
    @Autowired
    private TeamCapacityRepository repository;

    /**
     * The Authentication service.
     */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Gets all team capacities.
     *
     * @return the all team capacities
     */
    public List<TeamCapacityDTO> getAllTeamCapacities() {
        log.info("Fetching all team capacities...");
        List<TeamCapacityDTO> list = repository.findAll().stream().map(TeamCapacityDTO::new).toList();
        log.info("All team capacities fetched successfully.");
        return list;
    }

    /**
     * Load all team capacities data page.
     *
     * @param request the request
     * @return the page
     */
    public Page<TeamCapacityDTO> loadAllTeamCapacitiesData(TeamCapacityDTO request) {
        Map<String, SortCriteria> sort = request.getFilter().getSort();
        buildSortCondition(sort);
        Page<TeamCapacityEntity> capacityEntities = repository.findAll(request.getFilter().getPageable());
        return capacityEntities.map(TeamCapacityDTO::new);
    }

    /**
     * Gets team capacity detail.
     *
     * @param id the id
     * @return the team capacity detail
     */
    public TeamCapacityDTO getTeamCapacityDetail(Long id) {
        if (id == null) {
            log.error("Team capacity id not found");
            throw new DataValidationException(MessageCode.ERROR_CAPACITY_ID_NOT_FOUND);
        }
        log.info("Fetching team capacity detail for id: {}", id);
        final TeamCapacityDTO teamCapacityDTO = new TeamCapacityDTO(repository.findById(id).orElseThrow(() -> {
            log.error("Team capacity not found");
            return new ObjectNotFoundException(MessageCode.ERROR_CAPACITY_NOT_FOUND);
        }));
        log.info("Team capacity detail fetched successfully for id: {}", id);
        return teamCapacityDTO;
    }

    /**
     * Delete team capacity.
     *
     * @param id the id
     */

    public void deleteTeamCapacity(Long id) {
        if (id == null) {
            log.error("Team capacity id not found");
            throw new DataValidationException(MessageCode.ERROR_CAPACITY_ID_NOT_FOUND);
        }
        repository.deleteById(id);
        log.info("Team capacity with id {} deleted successfully.", id);
    }

    public TeamCapacityDTO saveTeamCapacity(TeamCapacityDTO capacityDTO) {
        log.info("Saving team capacity...");
        final Long capacityId = capacityDTO.getCapacityId();

        TeamCapacityEntity teamCapacityEntity = new TeamCapacityEntity();

        // update when team capacity id is not null
        if (capacityId != null) {
            teamCapacityEntity = repository.findById(capacityId).orElseThrow(() -> {
                log.error("Team capacity with id {} not found.", capacityId);
                return new DataValidationException(MessageCode.ERROR_CAPACITY_ID_NOT_FOUND);
            });
        }

        final String userName = authenticationService.getUser().getUsername();
        if (userName == null) {
            throw new ObjectNotFoundException("User not found");
        }

        final UserEntity userEntity = userRepository.findById(authenticationService.getUser().getId()).get();
        // set properties
        BeanUtils.copyProperties(capacityDTO, teamCapacityEntity);
//        systemEntity.setSystemName(systemDTO.getSystemName());
//        systemEntity.setCreatedBy(userEntity);
//        systemEntity.setUpdatedBy(userEntity);
        teamCapacityEntity = repository.save(teamCapacityEntity);
        log.info("System saved successfully.");
        return new TeamCapacityDTO(teamCapacityEntity);
    }

    private void buildSortCondition(Map<String, SortCriteria> sort) {
        if (sort.isEmpty()) {
            sort.put("updatedDate", new SortCriteria("updatedDate", SortType.DESC.getText()));
            return;
        }
        sort.forEach((key, criteria) -> {
            switch (key) {
                case "capacityId":
                    criteria.setFieldName("id");
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

