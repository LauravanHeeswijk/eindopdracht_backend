package nl.laura.boekenapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    // 400
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class,
            ConstraintViolationException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        String message = switch (ex) {
            case MethodArgumentNotValidException manv ->
                    manv.getBindingResult().getFieldErrors().stream()
                            .map(err -> err.getField() + " " + err.getDefaultMessage())
                            .findFirst().orElse("Validation failed");
            case MethodArgumentTypeMismatchException matm ->
                    "Parameter '" + matm.getName() + "' heeft onjuist type";
            case MissingServletRequestParameterException msrp ->
                    "Ontbrekende parameter: " + msrp.getParameterName();
            case HttpMessageNotReadableException hmnr ->
                    "Ongeldige of ontbrekende JSON in request body";
            case ConstraintViolationException cve ->
                    cve.getConstraintViolations().stream().findFirst()
                            .map(v -> v.getPropertyPath() + " " + v.getMessage())
                            .orElse("Constraint violation");
            default -> "Ongeldig verzoek";
        };
        return build(HttpStatus.BAD_REQUEST, message, req);
    }

    // 401
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiError> handleUnauthorized(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Authenticatie vereist of ongeldig", req);
    }

    // 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Geen toegang tot deze resource", req);
    }

    // 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Methode niet toegestaan", req);
    }

    // 409
    @ExceptionHandler({DuplicateLibraryItemException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> handleConflict(Exception ex, HttpServletRequest req) {
        String msg = (ex instanceof DataIntegrityViolationException)
                ? "Constraintfout: mogelijk dubbele of gerelateerde data"
                : ex.getMessage();
        return build(HttpStatus.CONFLICT, msg, req);
    }

    // 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        log.error("Onverwachte fout op {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Er ging iets mis aan serverzijde", req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req) {
        ApiError body = new ApiError(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI()
        );
        return new ResponseEntity<>(body, new HttpHeaders(), status);
    }
}

