package model;

/**
 * A custom exception class that will throw an InvalidPlacementException when
 * user enters/chooses invalid moves.
 * 
 * @author Fu-Yin Lin
 *
 */
public class InvalidPlacementException extends Exception {
	// Below constructors come from superclass Exception.
	public InvalidPlacementException() {
	}

	public InvalidPlacementException(String message) {
		super(message);
	}

	public InvalidPlacementException(Throwable cause) {
		super(cause);
	}

	public InvalidPlacementException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidPlacementException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
