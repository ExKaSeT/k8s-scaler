package ru.sberbank.k8sscaler.exception;

public class ScaleDeploymentsException extends RuntimeException {

    public ScaleDeploymentsException(String message, Throwable cause) {
        super(message, cause);
    }
}
