package com.sharp.vn.its.management.dto;

import com.sharp.vn.its.management.entity.SystemEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SystemDto {
    Long systemId;
    String systemName;

    public SystemDto(SystemEntity system) {
        this.systemId = system.getId();
        this.systemName = system.getSystemName();
    }
}
