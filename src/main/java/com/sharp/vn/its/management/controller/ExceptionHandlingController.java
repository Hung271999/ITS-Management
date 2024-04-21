package com.sharp.vn.its.management.controller;

import com.sharp.vn.its.management.dto.ResponseAPI;
import com.sharp.vn.its.management.exception.DataValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ResponseAPI exceptionHandler(final HttpServletRequest request,
                                 final HttpServletResponse response, final Exception ex) throws Exception {
        String msgExt = "";
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 1) {
            final StackTraceElement ste = stackTrace[0];
            msgExt = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        final String message = ex.getMessage();
        log.error(ex.getClass().getName() + " " + "Path: " + getRequestURL(request) + " Error " + message + LINE_SEPARATOR + "\t" + msgExt );
        return generateBody("Unexpected error","HTTP Error 500 - Internal Server Error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataValidationException.class})
    public @ResponseBody ResponseAPI handleValidateEx(final HttpServletRequest request,
                                                      final HttpServletResponse response, final DataValidationException ex) {
        String msgExt = "";
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        if (stackTrace.length > 1) {
            final StackTraceElement ste = stackTrace[0];
            msgExt = ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber();
        }
        final String message = ex.getMessage();
        log.error("Path: " + getRequestURL(request) + " Error " + message + LINE_SEPARATOR + "\t" + msgExt);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return generateBadRequestBody(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public @ResponseBody ResponseAPI handleValidateEx(final HttpServletRequest request,
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
        for (FieldError error : fieldErrors ) {
            builder.append(error.getField() + " : " + error.getDefaultMessage());
        }
        final String message = builder.toString();
        log.error("Path: " + getRequestURL(request) + " Error " + message + LINE_SEPARATOR + "\t" + msgExt);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return generateBadRequestBody(message);
    }
    private String getRequestURL(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (request.getQueryString() != null) {
            requestURI += "?" + request.getQueryString(); 
        }
        return requestURI;
    }

    private ResponseAPI generateBadRequestBody(final String message) {
        return generateBody(message, "HTTP Error 400 - Bad request",
                HttpServletResponse.SC_BAD_REQUEST);
    }

    private ResponseAPI generateBody(final String message, final String error, final int status ) {
        final ResponseAPI responseAPI = new ResponseAPI<>();
        responseAPI.setError(error);
        responseAPI.setStatus(status);
        responseAPI.setMessage(message);
        return responseAPI;
    }


}
