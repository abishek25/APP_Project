package model;

import java.io.Serializable;

public class InvalidShipException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	public InvalidShipException(String message) {
		super(message);
	}
}
