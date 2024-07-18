package com.sharp.vn.its.management.dto.chart;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The type Request dto.
 */
@Getter
@Setter
public class RequestDTO {
    private List<Long> userIds;
    private List <Integer> years;
}

