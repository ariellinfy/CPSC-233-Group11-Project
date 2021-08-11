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
		// TODO Auto-generated constructor stub
	}

	public InvalidUndoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidUndoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidUndoException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidUndoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
