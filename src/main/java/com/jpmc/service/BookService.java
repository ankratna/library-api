package com.jpmc.service;

import com.jpmc.dto.BookDTO;

import java.util.Set;

public interface BookService {

	public BookDTO addBook(BookDTO bookDTO) throws Exception;

	public Set<BookDTO> getAllBooks();

}
