package com.Jas.shop_backend.exception;

import com.Jas.shop_backend.api.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ---------------- VALIDATION (DTO) ---------------- */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleDtoValidation(MethodArgumentNotValidException ex) {

        Map<String, List<String>> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                        .add(error.getDefaultMessage())
        );

        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    /* ---------------- AUTHENTICATION ---------------- */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials() {
        Map<String, List<String>> fieldErrors = Map.of(
                "email", List.of("Invalid email or password")
        );

        return buildError(HttpStatus.UNAUTHORIZED, "Authentication failed", fieldErrors);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleDisabledException(DisabledException ex) {
        ApiResponse<?> response = new ApiResponse<>(
                false,
                "Email is not verified",
                null,
                List.of(Map.of("general", List.of("Email is not verified")))
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        return buildError(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed",
                Map.of("general", List.of(ex.getMessage()))
        );
    }

    /* ---------------- ACCESS DENIED ---------------- */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildError(
                HttpStatus.FORBIDDEN,
                "Access Denied",
                Map.of("general", List.of("You do not have permission to access this resource"))
        );
    }

    /* ---------------- CUSTOM VALIDATION ---------------- */
    @ExceptionHandler(ApiValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiValidation(ApiValidationException ex) {

        Map<String, List<String>> fieldErrors = new HashMap<>();
        ex.getErrors().forEach((k, v) ->
                fieldErrors.put(k, List.of(v))
        );

        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    /* ---------------- NOT FOUND ---------------- */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                Map.of("general", List.of(ex.getMessage()))
        );
    }

    /* ---------------- CONFLICT ---------------- */
    @ExceptionHandler(ApiConflictException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(ApiConflictException ex) {
        return buildError(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                Map.of("general", List.of(ex.getMessage()))
        );
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        return buildError(
                HttpStatus.CONFLICT,
                "Database constraint violation",
                Map.of("error", List.of("This action cannot be completed because the resource is referenced by existing orders or records."))
        );
    }

    /* ---------------- FALLBACK ---------------- */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnknown(Exception ex) {
        ex.printStackTrace(); // optional: log server-side for debugging
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong",
                Map.of("error", List.of("Unexpected server error"))
        );
    }

    /* ---------------- HELPER ---------------- */
    private ResponseEntity<ApiResponse<Object>> buildError(
            HttpStatus status,
            String message,
            Map<String, List<String>> fieldErrors) {

        List<Map<String, List<String>>> errors = List.of(fieldErrors);

        ApiResponse<Object> response = new ApiResponse<>(
                false,
                message,
                null,
                errors
        );

        return ResponseEntity.status(status).body(response);
    }
}
