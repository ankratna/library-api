package com.jpmc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Book does not exist")
public class BookNotFoundException extends Exception {

	public BookNotFoundException(String message) {
		super(message);
	}

}
