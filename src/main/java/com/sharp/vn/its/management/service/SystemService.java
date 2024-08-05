package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.constants.MessageCode;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.SortCriteria;
import com.sharp.vn.its.management.repositories.SystemRepository;
import com.sharp.vn.its.management.repositories.TaskRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * The type System service.
 */
@Service
@Slf4j
public class SystemService extends BaseService {
    /**
     * The System repository.
     */
    @Autowired
    private SystemRepository systemRepository;

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
     * The Task repository.
     */
    @Autowired
    private TaskRepository taskRepository;

    /**
     * Gets all systems data.
     *
     * @return the all systems data
     */
    public List<SystemDTO> getAllSystemsData() {
        log.info("Fetching all systems...");
        final List<SystemDTO> systems =
                systemRepository.findAll().stream().map(SystemDTO::new).toList();
        log.info("All systems fetched successfully.");
        return systems;
    }

    /**
     * Gets all systems data by search param and pageable.
     *
     * @param request the request
     * @return the all systems data
     */
    public Page<SystemDTO> loadAllSystemData(SystemDTO request) {
        Map<String, SortCriteria> sort = request.getFilter().getSort();
        buildSortCondition(sort);
        Page<SystemEntity> systemEntities = systemRepository.findBySystemNameContains(request.getFilter().getSearchKeyword(), request.getFilter().getPageable());
        return systemEntities.map(SystemDTO::new);
    }

    /**
     * Gets system detail.
     *
     * @param id the id
     * @return the system detail
     */
    public SystemDTO getSystemDetail(Long id) {
        if (id == null) {
            log.error("System id not found");
            throw new DataValidationException(MessageCode.ERROR_SYSTEM_ID_NOT_FOUND);
        }
        log.info("Fetching system detail for id: {}", id);
        final SystemDTO systemDTO = new SystemDTO(systemRepository.findById(id).orElseThrow(() -> {
            log.error("System not found");
            return new ObjectNotFoundException(MessageCode.ERROR_SYSTEM_NOT_FOUND);
        }));
        log.info("System detail fetched successfully for id: {}", id);
        return systemDTO;
    }

    /**
     * Delete system.
     *
     * @param id the id
     */
    public void deleteSystem(Long id) {
        if (id == null) {
            log.error("System id not found");
            throw new DataValidationException(MessageCode.ERROR_SYSTEM_ID_NOT_FOUND);
        }
        if(taskRepository.existsBySystemId(id)){
            log.error("System with {} cannot delete because the task associated", id);
           throw new DataValidationException(MessageCode.ERROR_SYSTEM_WITH_FOREIGN_KEY_TO_TASK);
        }
        systemRepository.deleteById(id);
        log.info("System with id {} deleted successfully.", id);
    }

    /**
     * Save system DTO.
     *
     * @param systemDTO the system DTO
     * @return the system DTO
     */
    public SystemDTO saveSystem(SystemDTO systemDTO) {
        log.info("Saving system...");
        final Long systemId = systemDTO.getSystemId();

        SystemEntity systemEntity = new SystemEntity();

        // update when system id is not null
        if (systemId != null) {
            systemEntity = systemRepository.findById(systemId).orElseThrow(() -> {
                log.error("System with id {} not found.", systemId);
                return new DataValidationException(MessageCode.ERROR_SYSTEM_ID_NOT_FOUND);
            });
        }

        final String userName = authenticationService.getUser().getUsername();
        if (userName == null) {
            throw new ObjectNotFoundException("User not found");
        }

        final UserEntity userEntity = userRepository.findById(authenticationService.getUser().getId()).get();
        // set properties
        BeanUtils.copyProperties(systemDTO, systemEntity);
        systemEntity.setSystemName(systemDTO.getSystemName());
        systemEntity.setCreatedBy(userEntity);
        systemEntity.setUpdatedBy(userEntity);
        systemEntity = systemRepository.save(systemEntity);
        log.info("System saved successfully.");
        return new SystemDTO(systemEntity);
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
                case "systemId":
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
