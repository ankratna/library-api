package com.jpmc.service;

import com.jpmc.dto.BookDTO;

import java.util.List;
import java.util.Set;

public interface BookService {

	public BookDTO addBook(BookDTO bookDTO) throws Exception;

	public Set<BookDTO> getAllBooks();

	public String deleteBook(Long isbn) throws Exception;

	public Set<BookDTO> getAllBooksWithAnyInputSearchTagPresent(List<String> tags);

}
