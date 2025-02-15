package com.example.accounts.exception;


import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestControllerAdvice

public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messages;

    private final HttpServletRequest servletRequest;

    public RestResponseEntityExceptionHandler(MessageSource messages, HttpServletRequest servletRequest) {
        this.messages = messages;
        this.servletRequest = servletRequest;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-Type", "application/json");
//        httpHeaders.add("Access-Control-Allow-Origin", "*");
//        httpHeaders.add("Accept", "*");
        return httpHeaders;
    }


    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        logger.error("Entity  not found");
        final ErrorResponseDTO bodyOfResponse = new ErrorResponseDTO(ex.getMessage(), ex.getMessage(), HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.value(), getRequestPath());
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    // 400
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleInvalidData(final RuntimeException ex, final WebRequest request) {
        logger.error("Invalid data");
        final ErrorResponseDTO bodyOfResponse = new ErrorResponseDTO(ex.getMessage(), ex.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.value(),
                getRequestPath());
        if (ex instanceof BadRequestException) {
            BadRequestException badRequestException = (BadRequestException) ex;
            List<String> errors = badRequestException.getErrorList();
            if (errors != null && !errors.isEmpty()) {
                bodyOfResponse.setErrorList(errors);
            }
        }
        return handleExceptionInternal(ex, bodyOfResponse, getHttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // 404
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        final ErrorResponseDTO bodyOfResponse = new ErrorResponseDTO(ex.getMessage(), ex.getMessage(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.value(),
                getRequestPath());
        return handleExceptionInternal(ex, bodyOfResponse, getHttpHeaders(), HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        final ErrorResponseDTO bodyOfResponse = new ErrorResponseDTO("Internal Error Occurred", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                getRequestPath());
        return new ResponseEntity<>(bodyOfResponse, getHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private String getRequestPath() {
        logger.error("Custom Web error path==" + servletRequest.getServletPath());
        String path = servletRequest.getRequestURI();
        if (servletRequest.getQueryString() == null)
            return path;
        return path + "?" + servletRequest.getQueryString();
    }

}
