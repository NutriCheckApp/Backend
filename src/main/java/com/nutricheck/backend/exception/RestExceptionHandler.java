package com.nutricheck.backend.exception;

import com.nutricheck.backend.exception.exception.CalendarEntryNotFoundException;
import com.nutricheck.backend.exception.exception.ImageNotExistsException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@Hidden
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDto> defaultExceptionHandler(HttpServletRequest req, Exception e) {
        var error = ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .path(req.getContextPath() + req.getServletPath())
                .message(e.getLocalizedMessage());

        if (e instanceof UsernameNotFoundException
            || e instanceof BadCredentialsException) {
            return new ResponseEntity<>(error.build(), HttpStatus.UNAUTHORIZED);
        } else if (e instanceof CalendarEntryNotFoundException
                   || e instanceof ImageNotExistsException) {
            return new ResponseEntity<>(error.build(), HttpStatus.NOT_FOUND);
        } else if (e instanceof MissingServletRequestParameterException
            || e instanceof HttpMessageNotReadableException
            || e instanceof ConstraintViolationException) {
            return new ResponseEntity<>(error.build(), HttpStatus.NOT_ACCEPTABLE);
        } else {
            return new ResponseEntity<>(error.build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> MethodArgumentNotValidExceptionHandler(HttpServletRequest req,
                                                                           MethodArgumentNotValidException ex) {
        List<String> missingParams = ex
                .getBindingResult()
                .getFieldErrors().stream()
                .map(param -> "Missing param in JSON '" + param.getField() + "'. " + param.getDefaultMessage())
                .toList();
        ErrorDto errorDto = ErrorDto.builder()
                .timestamp(LocalDateTime.now())
                .path(req.getContextPath() + req.getServletPath())
                .message(missingParams.toString())
                .build();
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_ACCEPTABLE);
    }
}

