package com.meizhuo.etips.model;

public class ETipsException extends Exception {

	public ETipsException() {
		 
	}

	public ETipsException(String detailMessage) {
		super(detailMessage);
		 
	}

	public ETipsException(Throwable throwable) {
		super(throwable);
	 
	}

	public ETipsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	 
	}

}
