package com.ra.advice;

import com.ra.exception.DataNotFoundEx;
import com.ra.exception.MyRuntimeEx;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> HandelValidEx(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(err->errors.put(err.getField(), err.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundEx.class)
    public ResponseEntity<?> HandelNotPoundEx(DataNotFoundEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MyRuntimeEx.class)
    public ResponseEntity<?> MyRuntimeEx(MyRuntimeEx ex){
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
