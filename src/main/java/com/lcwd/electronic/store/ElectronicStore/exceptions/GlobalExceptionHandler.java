package com.lcwd.electronic.store.ElectronicStore.exceptions;

import com.lcwd.electronic.store.ElectronicStore.payload.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle resource not found  exception
    @ExceptionHandler(ResourseNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourseNotFoundException ex) {
        logger.info("Exception Handler Invoked !!");
        ApiResponseMessage exceptionResponse = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(false) // means ki request aai has successfully but resource nahi mila
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //  Method arguments not valid exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumnetNotValidExceptions(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream()
                .forEach(objectError -> {
                    String message = objectError.getDefaultMessage();
                    String field = ((FieldError) objectError).getField();
                    response.put(field, message);
                });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Email update exception
    @ExceptionHandler(CannotChangeEmailException.class)
    public ResponseEntity<ApiResponseMessage> cannotChangeEmailExceptionHandler(CannotChangeEmailException ex) {
        ApiResponseMessage exceptionResponse = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    // Bad api request exception handler
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> badApiRequestExceptionHandler(BadApiRequestException ex) {
        ApiResponseMessage exceptionResponse = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ZeroQuantityException.class)
    public ResponseEntity<ApiResponseMessage> zeroQuantityExceptionHandler(ZeroQuantityException ex) {
        ApiResponseMessage exceptionResponse = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
