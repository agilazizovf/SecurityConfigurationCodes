package az.project.securityConfiguration.exception;


import az.project.securityConfiguration.dto.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handleNotFound(EntityNotFoundException entityNotFoundException) {
        log.error("ActionLog.error not found: {} ", entityNotFoundException.getMessage());
        return new ExceptionDTO(HttpStatus.NOT_FOUND.value(), entityNotFoundException.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleGlobal(Exception e){
        log.error("ActionLog.error global Exception: {} ", e.getMessage());
        return new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleGlobalRuntime(RuntimeException e){
        log.error("ActionLog.error global RuntimeException: {} ", e.getMessage());
        return new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleConstraintViolation(ConstraintViolationException e) {
        log.error("ActionLog.error ConstraintViolation: {} ", e.getMessage());
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionDTO handleAuthentication(AuthenticationException e) {
        log.error("ActionLog.error Authentication: {} ", e.getMessage());
        return new ExceptionDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDTO handleAlreadyExists(AlreadyExistsException e) {
        log.error("ActionLog.error AlreadyExists: {} ", e.getMessage());
        return new ExceptionDTO(HttpStatus.CONFLICT.value(), e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionDTO handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("ActionLog.error HttpMessageNotReadable: Invalid JSON data: {} ", ex.getMessage());
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("ActionLog.error MethodArgumentNotValidException: {} ", ex.getMessage());
        return new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

}