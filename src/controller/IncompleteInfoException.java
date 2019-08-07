package controller;

import java.io.Serializable;

public class IncompleteInfoException extends Exception implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public IncompleteInfoException(String message) {
		super(message);
	}
}
