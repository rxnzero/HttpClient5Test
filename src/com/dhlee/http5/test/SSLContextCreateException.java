package com.dhlee.http5.test;

public class SSLContextCreateException extends Exception {
	private static final long serialVersionUID = 1L;

	public SSLContextCreateException(String message) {
        super(message);
    }

    public SSLContextCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
