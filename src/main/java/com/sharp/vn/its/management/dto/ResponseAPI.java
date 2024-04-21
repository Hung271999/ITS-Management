package com.sharp.vn.its.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAPI<T> {

    private String message = "Ok";
    private T data;
    private int status = 200;
    private String error;

    public ResponseAPI(T data) {
        this.data = data;
    }

    public ResponseAPI() {
    }

}