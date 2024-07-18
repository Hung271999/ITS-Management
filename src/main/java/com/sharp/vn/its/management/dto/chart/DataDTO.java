package com.sharp.vn.its.management.dto.chart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Data dto.
 */
@Data
public class DataDTO {
    private List<UserDataDTO> data;
    private TotalItem total;
}
