package io.github.malikzh.qchain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TransactionPoolException extends ResponseStatusException {
    public TransactionPoolException(HttpStatus status) {
        super(status);
    }

    public TransactionPoolException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public TransactionPoolException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public TransactionPoolException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }
}
