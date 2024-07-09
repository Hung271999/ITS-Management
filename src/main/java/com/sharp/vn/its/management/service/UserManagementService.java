package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.constants.FilterType;
import com.sharp.vn.its.management.constants.Role;
import com.sharp.vn.its.management.dto.user.UserDTO;
import com.sharp.vn.its.management.entity.*;
import com.sharp.vn.its.management.exception.DataValidationException;
import com.sharp.vn.its.management.exception.ObjectNotFoundException;
import com.sharp.vn.its.management.filter.CriteriaFilterItem;
import com.sharp.vn.its.management.repositories.RoleRepository;
import com.sharp.vn.its.management.repositories.TaskRepository;
import com.sharp.vn.its.management.repositories.UserRepository;
import com.sharp.vn.its.management.security.UserSecurityDetails;
import com.sharp.vn.its.management.util.CollectionUtils;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.entity.TaskEntity;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.filter.SortCriteria;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.sharp.vn.its.management.util.CriteriaUtil.buildCombinedPredicate;
import static com.sharp.vn.its.management.util.CriteriaUtil.buildPredicate;

/**
 * The type User management service.
 */
@Service
@Slf4j
public class UserManagementService extends BaseService {
    /**
     * The User repository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * The Role repository.
     */
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TaskRepository taskRepository;
    /**
     * The Authentication service.
     */
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Save user user dto.
     *
     * @param request the request
     * @return the user dto
     */
    @Transactional
    public UserDTO saveUser(UserDTO request) {
        log.info("Saving task...");
        final String userName = request.getUserName();
        final String email = request.getEmail();
        final Long userId = request.getUserId();
        final String password = request.getPassword();
        // Check if username or email already exists
        if (StringUtils.isEmpty(userName) || userRepository.existsByUsername(userName,
                userId)) {
            throw new DataValidationException("Username " + userName + " already exists");
        }
        if (StringUtils.isEmpty(email) || userRepository.existsByEmail(email, userId)) {
            throw new DataValidationException("Email " + email + " already exists");
        }
        // set properties
        UserEntity user = new UserEntity();
        if (userId != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new DataValidationException("User ID not found"));
        }
        user.setUsername(request.getUserName());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFullName(user.getFirstName() + " " + user.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        UserSecurityDetails authenticatedUser = authenticationService.getUser();
        if (authenticatedUser != null) {
            UserEntity currentUser = userRepository.findById(authenticatedUser.getId())
                    .orElseThrow(() -> new ObjectNotFoundException(
                            "User not found with id " + authenticatedUser.getId()));
            user.setCreatedBy(currentUser);
            user.setUpdatedBy(currentUser);
        }
        final Role itsRole = request.getRole();
        final RoleEntity role = roleRepository.findByRoleName(itsRole)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Can't find role with name: " + itsRole.name()));
        UserRoleEntity userRole = new UserRoleEntity(user, role);
        user.getRoles().add(userRole);
        UserDTO result = new UserDTO(userRepository.save(user));
        log.info("User saved successfully.");
        return result;

    }

    /**
     * Delete user.
     *
     * @param id the id
     */
    public void deleteUser(Long id) {
        if (id == null) {
            throw new DataValidationException("User ID is null or empty");
        }
        try {
            Boolean hasTasks = taskRepository.existsByPersonInChargeId(id);
            if (hasTasks) {

                throw new DataValidationException("Cannot delete user with associate tasks.");
            }
            userRepository.deleteById(id);
            log.info("User with id {} deleted successfully.", id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Cannot delete user due to existing dependencies.");
        }
    }


    /**
     * Gets all users data.
     *
     * @return the all users data
     */
    public List<UserDTO> getAllUsersData() {
        log.info("Fetching all users...");
        final List<UserDTO> users = userRepository.findAll().stream()
                .map(UserDTO::new)
                .toList();
        log.info("All users fetched successfully.");
        return users;
    }

    /**
     * Gets user detail.
     *
     * @param id the id
     * @return the user detail
     */
    public UserDTO getUserDetail(Long id) {
        if (id == null) {
            throw new DataValidationException("User id not found");
        }
        log.info("Fetching user detail for id: {}", id);
        final UserDTO userDTO = new UserDTO(userRepository.findById(id).orElseThrow(() -> {
            return new ObjectNotFoundException("User not found with id: " + id);
        }));
        log.info("User detail fetched successfully for id: {}", id);
        return userDTO;
    }

    /**
     * Gets list users data.
     *
     * @param request the request
     * @return the list users data
     */
    public Page<UserDTO> getListUsersData(UserDTO request) {
        log.info("Fetching all users...");
        CriteriaSearchRequest filter = request.getFilter();
        Map<String, CriteriaFilterItem> searchParam = filter.getSearchParam();
        Map<String, SortCriteria> sort = filter.getSort();
        buildSortCondition(sort);

        Specification<UserEntity> specification = buildFilterCondition(searchParam);
        Page<UserEntity> pageable = userRepository.findAll(specification, request.getFilter().getPageable());
        log.info("All users fetched successfully.");
        return pageable.map(UserDTO::new);
    }


    /**
     * Build filter condition specification.
     *
     * @param searchParam the search param
     * @return the specification
     */
    public Specification<UserEntity> buildFilterCondition(
            Map<String, CriteriaFilterItem> searchParam) {
        return
                (Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    if (searchParam == null) {
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                    CollectionUtils.addIfNotEmptyOrNull(predicates,
                            buildCombinedPredicate(criteriaBuilder, FilterType.OR,
                                    buildPredicate(criteriaBuilder, root,
                                            searchParam.get("userName")),
                                    buildPredicate(criteriaBuilder, root,
                                            searchParam.get("fullName"))));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
    }
    ;
    private void buildSortCondition(Map<String, SortCriteria> sort) {
        if (sort == null || sort.isEmpty()) {
            sort.put("id", new SortCriteria("id", SortType.DESC.getText()));
            return;
        }
        sort.forEach((key, criteria) -> {
            switch (key) {
                case "userId":
                    criteria.setFieldName("id");
                    break;
                case "firstName":
                    criteria.setFieldName("firstName");
                    break;
                case "lastName":
                    criteria.setFieldName("lastName");
                    break;
                case "email":
                    criteria.setFieldName("email");
                    break;
                case "userName":
                    criteria.setFieldName("username");
                    break;
                case "role":
                    criteria.setFieldName("roles.role.roleName");
                    break;
                default:
                    break;
            }
        });
    }
}