package model;

public class InvalidPlacementException extends Exception {

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
