package com.valuablecode.transform;

/**
 * Base exception for all transformation exceptions.
 */
@SuppressWarnings("serial")
public class TransformationException extends RuntimeException {

    public TransformationException() {
        super();
    }

    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(Throwable cause) {
        this("Unexpected error during transformation. Please consult the log files.", cause);
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause);
    }

}
