package com.sharp.vn.its.management.dto.chart;

import lombok.*;


import java.util.Map;

/**
 * The type Chart dto.
 */
@Data
public class ChartDTO {
    private Long systemId;
    private String systemName;
    private Map<Integer, Integer> value;
    private int totalCount;


    /**
     * Instantiates a new Chart dto.
     *
     * @param systemId   the system id
     * @param systemName the system name
     * @param totalCount the total count
     * @param value      the value
     */
    public ChartDTO(Long systemId, String systemName, int totalCount, Map<Integer, Integer> value) {
        this.systemId = systemId;
        this.systemName = systemName;
        this.totalCount = totalCount;
        this.value = value;
    }

    /**
     * Instantiates a new Chart dto.
     */
    public ChartDTO()
    {}
}





