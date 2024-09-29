package ru.sberbank.k8sscaler.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sberbank.k8sscaler.dto.SuccessContainerDto;
import ru.sberbank.k8sscaler.exception.ScaleDeploymentsException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({ScaleDeploymentsException.class})
    public ResponseEntity<?> handleException(ScaleDeploymentsException e) {
        var message = String.format("%s; Reason: %s", e.getMessage(), e.getCause().getMessage());
        log.error(message);
        return ResponseEntity.status(BAD_GATEWAY)
                .body(new SuccessContainerDto(false, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new SuccessContainerDto(false, null));
    }
}
