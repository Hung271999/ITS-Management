package com.sharp.vn.its.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * The type Response api.
 *
 * @param <T> the type parameter
 */
@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {

    /**
     * The Message.
     */
    private String message = "Ok";
    /**
     * The Data.
     */
    private T data;
    /**
     * The Status.
     */
    private int status = 200;
    /**
     * The Error.
     */
    private String error;

    /**
     * Instantiates a new Response api.
     *
     * @param data the data
     */
    public ResponseData(T data) {
        this.data = data;
    }

    /**
     * Instantiates a new Response api.
     */
    public ResponseData() {
    }

}
