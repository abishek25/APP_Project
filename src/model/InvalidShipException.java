package model;

import java.io.Serializable;

/**
 * User defined exception to check for invalid ships
 *
 */
public class InvalidShipException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with exception message
	 * @param message The exception message
	 */
	public InvalidShipException(String message) {
		super(message);
	}
}
