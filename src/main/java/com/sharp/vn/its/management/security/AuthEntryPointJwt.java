package com.sharp.vn.its.management.security;

import java.io.IOException;

import com.sharp.vn.its.management.dto.ResponseData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Commence.
     *
     * @param request the request
     * @param response the response
     * @param authException the auth exception
     * @throws IOException the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseData responseAPI = new ResponseData();
        responseAPI.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        responseAPI.setMessage(authException.getMessage());
        responseAPI.setError("HTTP Error 401 - Unauthorized");
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseAPI);
    }

}
