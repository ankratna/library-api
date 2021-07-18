package com.jpmc.service;

import com.jpmc.dto.BookDTO;

import java.util.List;
import java.util.Set;

public interface BookService {

	public BookDTO addBook(BookDTO bookDTO) throws Exception;

	public Set<BookDTO> getAllBooks();

	public String deleteBook(Long isbn) throws Exception;

	public Set<BookDTO> searchByAllTags(List<String> tags);

	public Set<BookDTO> searchByAnyTags(List<String> tags);

	public Set<BookDTO> search(BookDTO bookDTO);

	public BookDTO searchByIsbn(Long isbn);

	public Set<BookDTO> searchByTitle(String title);

	public Set<BookDTO> searchByAuthor(String author);

}
