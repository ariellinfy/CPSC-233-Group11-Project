package model;

/**
 * A custom exception class that will throw an InvalidUndoException when an undo
 * can not be performed or an error occurs while performing the undo.
 * 
 * @author Fu-Yin Lin
 *
 */
public class InvalidUndoException extends Exception {
	// Below constructors come from superclass Exception.
	public InvalidUndoException() {
	}

	public InvalidUndoException(String message) {
		super(message);
	}

	public InvalidUndoException(Throwable cause) {
		super(cause);
	}

	public InvalidUndoException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidUndoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
