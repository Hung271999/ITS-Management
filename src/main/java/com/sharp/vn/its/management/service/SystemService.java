package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.entity.SystemEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.repositories.SystemRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @return the all systems data
     */
    public Page<SystemDTO> loadAllSystemData(String searchParam, Pageable pageable) {
        Page<SystemEntity> systemEntities = systemRepository.findAllSystems(searchParam, pageable);
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
            throw new DataValidationException("System id not found");
        }
        log.info("Fetching system detail for id: {}", id);
        final SystemDTO systemDTO = new SystemDTO(systemRepository.findById(id).orElseThrow(() -> {
            return new ObjectNotFoundException("System not found with id: " + id);
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
            throw new DataValidationException("System id not found");
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
            systemEntity = systemRepository.findById(systemId).orElseThrow(
                    () -> new DataValidationException("System not found with id: " + systemId));
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
}
