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
		return bookService.addBook(bookDTO);

	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> getAllBooks() {
		return bookService.getAllBooks();
	}

	@DeleteMapping("/{isbn}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String deleteBook(@PathVariable Long isbn) throws Exception {
		return bookService.deleteBook(isbn);
	}

}
