package com.dyongs.demo.global.exception;

import com.dyongs.demo.domain.category.exception.InvalidSortException;
import com.dyongs.demo.domain.product.exception.ProductAccessDeniedException;
import com.dyongs.demo.domain.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code("USER_NOT_FOUND")
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Validation - @Valid 실패 시 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {

        List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build()
                )
                .toList();

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code("VALIDATION_ERROR")
                .message("입력값이 올바르지 않습니다.")
                .errors(fieldErrors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code("EMAIL_ALREADY_EXISTS")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code("INVALID_CREDENTIALS")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401
    }

    @ExceptionHandler(ProductAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleProductAccessDenied(ProductAccessDeniedException e) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code("FORBIDDEN")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response); // 403
    }

    @ExceptionHandler(InvalidSortException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSortException(InvalidSortException e) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .code("INVALID_SORT")
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 400
    }

    // 기타 Service, Repository 등에서 RuntimeException 발생 시
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
//
//        ErrorResponse response = ErrorResponse.builder()
//                .success(false)
//                .code("INTERNAL_ERROR")
//                .message(e.getMessage())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}
