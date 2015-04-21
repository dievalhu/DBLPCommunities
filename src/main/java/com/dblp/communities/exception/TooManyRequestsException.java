package com.dblp.communities.exception;

public class TooManyRequestsException extends RuntimeException {

	private static final long serialVersionUID = -8548766652085403732L;

	public TooManyRequestsException() {
		super();
	}
	
	public TooManyRequestsException(String msg) {
		super(msg);
	}
}
