package com.fps.back.shared.controller;

import com.fps.back.shared.model.dto.ResponseJsonError;
import com.fps.back.shared.model.exception.DuplicateDataException;
import com.fps.back.shared.model.exception.JsonNullException;
import com.fps.back.shared.model.exception.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseJsonError> defaultErrorHandler(Exception ex) {
        System.err.println(ex.getMessage());
        log.error(ex.getMessage());
        ex.printStackTrace();
        ResponseJsonError error = new ResponseJsonError(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseJsonError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseJsonError error = new ResponseJsonError(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JsonNullException.class)
    public ResponseEntity<ResponseJsonError> handleJsonNullException(JsonNullException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "The request body is empty or invalid";
        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseJsonError> handelBadCredentialsException(BadCredentialsException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Authentication is required or has failed";
        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseJsonError> handleAccessDeniedException(AccessDeniedException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Access is denied";
        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ResponseJsonError> handleDuplicateDataException(DuplicateDataException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Data Duplicated";
        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseJsonError> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "User Not Found";
        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseJsonError> handleExpiredJwtException(ExpiredJwtException ex) {
        String message = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Token Expired";
        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseJsonError> handleDuplicateEntryException(DataIntegrityViolationException ex) {
        String message = "This data is already associated with another user.";

        if (ex.getCause() instanceof ConstraintViolationException consExp) {

            message = "The value "+consExp.getConstraintName()+" you are trying to save is already in use by another user.";
        }

        ResponseJsonError error = new ResponseJsonError(message, HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
