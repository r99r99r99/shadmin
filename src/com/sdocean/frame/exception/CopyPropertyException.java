package com.sdocean.frame.exception;


/**
 * @author liuw
 *
 * */
public class CopyPropertyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CopyPropertyException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public CopyPropertyException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public CopyPropertyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public CopyPropertyException(Throwable arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
	}
}
