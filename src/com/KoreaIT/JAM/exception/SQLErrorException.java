package com.KoreaIT.JAM.exception;

public class SQLErrorException extends RuntimeException{
	
	private static final long serialVersionUID = 2654253136391794657L;
	private Exception origin;

	public SQLErrorException(String message, Exception origin) {
		super(message);
		this.origin = origin;
	}

	public Exception getOrigin() {
		return origin;
	}
}