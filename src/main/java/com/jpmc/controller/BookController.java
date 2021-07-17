package com.jpmc.controller;

import com.jpmc.dto.BookDTO;
import com.jpmc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/book")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO addBook(@Valid @RequestBody BookDTO bookDTO) throws Exception {
		BookDTO book = bookService.addBook(bookDTO);
		return book;
	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> getAllBooks() {
		Set<BookDTO> books = bookService.getAllBooks();
		return books;
	}

}
