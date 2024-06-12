package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.ResponseData;
import com.sharp.vn.its.management.exception.AuthenticationException;
import com.sharp.vn.its.management.exception.DataValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * The type Exception handling controller.
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    /**
     * The constant LINE_SEPARATOR.
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Exception handler response api.
     *
     * @param request the request
     * @param response the response
     * @param ex the ex
     * @return the response api
     * @throws Exception the exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ResponseData exceptionHandler(final HttpServletRequest request,
            final HttpServletResponse response, final Exception ex) throws Exception {
        String msgExt = "";
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 1) {
            final StackTraceElement ste = stackTrace[0];
            msgExt = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        final String message = ex.getMessage();
        log.error(ex.getClass().getName() + " " + "Path: " + getRequestURL(request) + " Error "
                + message + LINE_SEPARATOR + "\t" + msgExt);
        return generateBody("Internal server error", "HTTP Error 500 - Internal Server Error",
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle data validation exception response api.
     *
     * @param request the request
     * @param response the response
     * @param ex the ex
     * @return the response api
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataValidationException.class})
    public @ResponseBody ResponseData handleDataValidationException(final HttpServletRequest request,
            final HttpServletResponse response, final DataValidationException ex) {
        String msgExt = "";
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 1) {
            final StackTraceElement ste = stackTrace[0];
            msgExt = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        final String message = ex.getMessage();
        log.error("Path: " + getRequestURL(request) + " Error " + message + LINE_SEPARATOR + "\t"
                + msgExt);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return generateBadRequestBody(message);
    }

    /**
     * Handle method argument not valid exception response api.
     *
     * @param request the request
     * @param response the response
     * @param ex the ex
     * @return the response api
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public @ResponseBody ResponseData handleArgumentNotValidException(
            final HttpServletRequest request,
            final HttpServletResponse response, final MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        StringBuilder builder = new StringBuilder();
        String msgExt = "";
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 1) {
            final StackTraceElement ste = stackTrace[0];
            msgExt = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        for (FieldError error : fieldErrors) {
            builder.append(error.getField() + " : " + error.getDefaultMessage());
        }
        final String message = builder.toString();
        log.error("Path: " + getRequestURL(request) + " Error " + message + LINE_SEPARATOR + "\t"
                + msgExt);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return generateBadRequestBody(message);
    }

    /**
     * Handle authentication exception response data.
     *
     * @param request the request
     * @param response the response
     * @param ex the ex
     * @return the response data
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({AuthenticationException.class,
            org.springframework.security.core.AuthenticationException.class})
    public @ResponseBody ResponseData handleAuthenticationException(
            final HttpServletRequest request,
            final HttpServletResponse response, final RuntimeException ex) {
        String msgExt = "";
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 1) {
            final StackTraceElement ste = stackTrace[0];
            msgExt = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        final String message = ex.getMessage();
        log.error("Path: " + getRequestURL(request) + " Error " + message + LINE_SEPARATOR + "\t"
                + msgExt);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return generateUnauthorizedBody(message);
    }


    /**
     * Gets request url.
     *
     * @param request the request
     * @return the request url
     */
    private String getRequestURL(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (request.getQueryString() != null) {
            requestURI += "?" + request.getQueryString();
        }
        return requestURI;
    }

    /**
     * Generate bad request body response api.
     *
     * @param message the message
     * @return the response api
     */
    private ResponseData generateBadRequestBody(final String message) {
        return generateBody(message, "HTTP Error 400 - Bad request",
                HttpServletResponse.SC_BAD_REQUEST);
    }

    /**
     * Generate unauthorized body response data.
     *
     * @param message the message
     * @return the response data
     */
    private ResponseData generateUnauthorizedBody(final String message) {
        return generateBody(message, "HTTP Error 401 - Unauthorized",
                HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * Generate body response api.
     *
     * @param message the message
     * @param error the error
     * @param status the status
     * @return the response api
     */
    private ResponseData generateBody(final String message, final String error, final int status) {
        final ResponseData responseAPI = new ResponseData<>();
        responseAPI.setError(error);
        responseAPI.setStatus(status);
        responseAPI.setMessage(message);
        return responseAPI;
    }


}
