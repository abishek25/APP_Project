package controller;

import java.io.Serializable;

/**
 * The user defined exception to handle incomplete information scenarios
 *
 */
public class IncompleteInfoException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor
	 * @param message The exception message
	 */
	public IncompleteInfoException(String message) {
		super(message);
	}
}
