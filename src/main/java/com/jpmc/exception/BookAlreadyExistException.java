package com.jpmc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Book already exist")
public class BookAlreadyExistException extends Exception {

	public BookAlreadyExistException(String message) {
		super(message);
	}

}
