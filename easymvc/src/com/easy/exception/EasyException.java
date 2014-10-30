package com.easy.exception;

public class EasyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EasyException(String msg) {
		super(msg);
	}

	public EasyException(String msg, Throwable e) {
		super(msg, e);
	}

}
