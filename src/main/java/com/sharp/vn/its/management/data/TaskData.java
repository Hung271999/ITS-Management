package com.sharp.vn.its.management.data;
import lombok.Data;

@Data
public class TaskData {
    private Long id;
    private String firstName;
    private String systemName;
    private int  week;
    private int status;
    private int total;

}
