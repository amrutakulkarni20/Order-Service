package com.etrusted.interview.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleInvalidInputDataException(Exception exception) {

        final ErrorDetails errorDetails = ErrorDetails.builder()
                .code(ErrorCode.INVALID_REQUEST)
                .message(exception.getMessage())
                .build();

        log.error("Exception occurred: {}", errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {

        final Map<String, String> errorMap = methodArgumentNotValidException.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((org.springframework.validation.FieldError) error).getField(),
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        final ErrorDetails errorDetails = ErrorDetails.builder()
                .code(ErrorCode.INVALID_REQUEST)
                .message("Input request validation failed")
                .errors(errorMap)
                .build();

        log.error("Exception occurred: {}", errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
