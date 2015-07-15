package jp.gr.java_conf.islandocean.stockanalysis.util;

public class FailedToFindElementException extends Exception {

	public FailedToFindElementException() {
	}

	public FailedToFindElementException(String message) {
		super(message);
	}

	public FailedToFindElementException(Throwable cause) {
		super(cause);
	}

	public FailedToFindElementException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedToFindElementException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
