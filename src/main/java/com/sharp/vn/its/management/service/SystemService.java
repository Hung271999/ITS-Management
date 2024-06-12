package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.dto.system.SystemDTO;
import com.sharp.vn.its.management.repositories.SystemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
