package com.sharp.vn.its.management.service;

import com.sharp.vn.its.management.constants.FilterType;
import com.sharp.vn.its.management.constants.MessageCode;
import com.sharp.vn.its.management.constants.Role;
import com.sharp.vn.its.management.data.ChartData;
import com.sharp.vn.its.management.dto.chart.ChartDTO;
import com.sharp.vn.its.management.dto.chart.ChartFilter;
import com.sharp.vn.its.management.dto.chart.TotalItemChart;
import com.sharp.vn.its.management.dto.chart.DataItemChart;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import com.sharp.vn.its.management.constants.SortType;
import com.sharp.vn.its.management.entity.UserEntity;
import com.sharp.vn.its.management.filter.CriteriaSearchRequest;
import com.sharp.vn.its.management.filter.SortCriteria;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * The Task repository.
     */
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
            log.error("Username {} already exists", userName);
            throw new DataValidationException(MessageCode.ERROR_USER_USER_NAME_ALREADY_EXIT);
        }
        if (StringUtils.isEmpty(email) || userRepository.existsByEmail(email, userId)) {
            log.error("Email {} already exists", email);
            throw new DataValidationException(MessageCode.ERROR_USER_EMAIL_ALREADY_EXIT);
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
        if (isValidPassword(userId, password)) {
            user.setPassword(request.getPassword());
        }
        UserSecurityDetails authenticatedUser = authenticationService.getUser();
        if (authenticatedUser != null) {
            UserEntity currentUser = userRepository.findById(authenticatedUser.getId())
                    .orElseThrow(() -> {
                        log.error("User not found with id {}", authenticatedUser.getId());
                        return new ObjectNotFoundException(MessageCode.ERROR_USER_ID_NOT_FOUND);
                    });
            user.setCreatedBy(currentUser);
            user.setUpdatedBy(currentUser);
        }
        final Role itsRole = request.getRole();
        saveUserRole(user, itsRole);
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
            log.error("User ID is null or empty");
            throw new DataValidationException(MessageCode.ERROR_USER_ID_EMPTY_OR_NULL);
        }
        Boolean hasTasks = taskRepository.existsByPersonInChargeId(id);
        if (hasTasks) {
            log.error("User with id {} can not delete because task associate", id);
            throw new DataValidationException(MessageCode.ERROR_USER_WITH_FOREIGN_KEY_TO_TASK);
        }
        userRepository.deleteById(id);
        log.info("User with id {} deleted successfully.", id);
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
            log.error("User ID is null or empty.");
            throw new DataValidationException(MessageCode.ERROR_USER_ID_EMPTY_OR_NULL);
        }
        log.info("Fetching user detail for id: {}", id);
        final UserDTO userDTO = new UserDTO(userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with id: {}", id);
            return new ObjectNotFoundException(MessageCode.ERROR_USER_ID_NOT_FOUND);
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
        Page<UserEntity> pageable =
                userRepository.findAll(specification, request.getFilter().getPageable());
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

    /**
     * Save User Role
     *
     * @param user    the UserEntity
     * @param itsRole the Role
     */
    private void saveUserRole(UserEntity user, Role itsRole) {
        final RoleEntity role = roleRepository.findByRoleName(itsRole)
                .orElseThrow(() -> {
                    log.error("Can't find role with name: {}", itsRole.name());
                    return new ObjectNotFoundException(
                            MessageCode.ERROR_USER_CANNOT_FIND_ROLE_WITH_NAME);
                });
        Set<UserRoleEntity> roles = user.getRoles();
        UserRoleEntity userRole = new UserRoleEntity(user, role);
        if (roles.isEmpty()) {
            user.getRoles().add(userRole);
            return;
        }
        roles.clear();
        roles.add(userRole);
    }

    private boolean isValidPassword(Long userId, final String password) {
        if (userId != null && StringUtils.isEmpty(password)) {
            return false;
        }
        if (password.length() < 6) {
            throw new DataValidationException(MessageCode.ERROR_USER_PASSWORD_TOO_SHORT);
        }
        return true;
    }


    private int getCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.getYear();
    }

    /**
     * Gets user task data.
     *
     * @return the user task data
     */
    public ChartDTO getUserTaskData(ChartFilter filter) {
        List<ChartData> data = userRepository.findUserGroupByNameAndStatus(filter.getUserIds(), filter.getYears());
        Map<Long, List<ChartData>> mapGroupByUserid = data.stream()
                .collect(Collectors.groupingBy(ChartData::getId));
        List<DataItemChart> dataItemCharts = new ArrayList<>();
        mapGroupByUserid.forEach((id, chartDataList) -> {
            DataItemChart item = new DataItemChart();
            item.setFirstName(chartDataList.get(0).getFirstName());
            item.setValues(chartDataList.stream()
                    .collect(Collectors.toMap(ChartData::getStatus, ChartData::getTotal)));
            item.setTotalCount(chartDataList.stream()
                    .mapToInt(ChartData::getTotal)
                    .sum());
            dataItemCharts.add(item);
        });
        TotalItemChart total = new TotalItemChart(data.stream()
                .collect(Collectors.groupingBy(
                        ChartData::getStatus,
                        Collectors.summingInt(ChartData::getTotal)
                )), dataItemCharts.stream()
                .mapToInt(DataItemChart::getTotalCount)
                .sum());
        return new ChartDTO(total,dataItemCharts);
    }
}
