package com.sharp.vn.its.management.dto.chart;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
public class UserDataDTO {
    private String userName;
    private String fullName;
    private String firstName;
    private int systemId;
    private String systemName;
    private Map<Integer, Integer> values;
    private int totalCount;

    public UserDataDTO(Map<Integer, Integer> values, int totalCount, String fullName) {
        this.values = values;
        this.totalCount = totalCount;
        this.fullName = fullName;
    }
    public UserDataDTO(String userName, String fullName, String firstName, int systemId, String systemName, Map<Integer, Integer> values, int totalCount) {
        this.userName = userName;
        this.fullName = fullName;
        this.firstName = firstName;
        this.systemId = systemId;
        this.systemName = systemName;
        this.values = values;
        this.totalCount = totalCount;
    }

    public UserDataDTO() {
    }
}
