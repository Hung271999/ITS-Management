package com.sharp.vn.its.management.data;

import lombok.Data;
import lombok.Getter;

@Data
public class ChartData {
    private Long id;
    private String firstName;
    private String systemName;
    private int status;
    private int total;
}
