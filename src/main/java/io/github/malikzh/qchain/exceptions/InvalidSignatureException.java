package io.github.malikzh.qchain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidSignatureException extends ResponseStatusException {
    public InvalidSignatureException(HttpStatus status) {
        super(status);
    }

    public InvalidSignatureException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public InvalidSignatureException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public InvalidSignatureException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
