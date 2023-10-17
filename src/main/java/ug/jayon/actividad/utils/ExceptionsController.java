package ug.jayon.actividad.utils;

import org.hibernate.exception.DataException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.TransactionRequiredException;

@ControllerAdvice
public class ExceptionsController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleException(Exception exception) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = "Error del servidor";
        String details = exception.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleRuntimeException(RuntimeException exception) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = "Petición no válida";
        String details = exception.getMessage();

        if (exception instanceof DataException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof NoResultException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof IllegalArgumentException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof EntityExistsException) {
            status = HttpStatus.BAD_REQUEST.value();
        } else if (exception instanceof TransactionRequiredException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        } else if (exception.getCause() != null) {
            if (exception.getCause() instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception.getCause())
                        .getConstraintViolations();
                Set<String> messages = new HashSet<>(constraintViolations.size());
                messages.addAll(constraintViolations.stream()
                        .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
                        .collect(Collectors.toList()));
                details = messages.toString();
            } else if (exception.getCause().getCause() instanceof ConstraintViolationException) {
                Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) exception.getCause()
                        .getCause()).getConstraintViolations();
                Set<String> messages = new HashSet<>(constraintViolations.size());
                messages.addAll(constraintViolations.stream()
                        .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
                        .collect(Collectors.toList()));
                details = messages.toString();
            }
        }

        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleJsonParseException(JsonParseException exception) {
        String message = "Petición no válida";
        String details = "Formato incorrecto en el cuerpo de la petición";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.BAD_REQUEST.value(), message, details);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleIOException(IOException exception) {
        String message = "Error del servidor";
        String details = "Error de E/S durante la operación";
        ErrorDetails errorDetails = new ErrorDetails(new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), message,
                details);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorDetails> handleValidationErrors(MethodArgumentNotValidException ex) {
        int status = HttpStatus.BAD_REQUEST.value();
        String message = "Petición no válida";
        String details = ex.getMessage();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> errorMessages = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        details = String.join("; ", errorMessages);

        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, message,
                String.format("Problemas con los campos: { %s }", details));
        return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(status));
    }
}
