package com.jpmc.controller;

import com.jpmc.dto.BookDTO;
import com.jpmc.exception.BookAlreadyExistException;
import com.jpmc.exception.BookNotFoundException;
import com.jpmc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/book")
public class BookController {

	private final BookService bookService;

	@Autowired
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping("/addBook")
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO addBook(@Valid @RequestBody BookDTO bookDTO) throws BookAlreadyExistException {
		return bookService.addBook(bookDTO);

	}

	@GetMapping("/getAllBooks")
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> getAllBooks() {
		return bookService.getAllBooks();
	}

	@DeleteMapping("/deleteBook/{isbn}")
	@ResponseStatus(HttpStatus.OK)
	// @ExceptionHandler(BookNotFoundException.class)
	@ResponseBody
	public String deleteBook(@PathVariable Long isbn) throws BookNotFoundException {
		return bookService.deleteBook(isbn);
	}

	@PostMapping("/searchByAnyTag")
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> searchByAnyTags(@RequestBody List<String> tags) {
		return bookService.searchByAnyTags(tags);
	}

	@PostMapping("/searchByAllTag")
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> searchByAllTags(@RequestBody List<String> tags) {
		return bookService.searchByAllTags(tags);
	}

	@PostMapping("/Search")
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> search(@RequestBody BookDTO searchRequest) {
		return bookService.search(searchRequest);
	}

	@GetMapping("/searchByIsbn/{isbn}")
	@ResponseStatus(HttpStatus.OK)
	public BookDTO searchByIsbn(Long isbn) throws BookNotFoundException {
		return bookService.searchByIsbn(isbn);
	}

	@GetMapping("/searchByTitle/{title}")
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> searchByTitle(String title) {
		return bookService.searchByTitle(title);
	}

	@GetMapping("/searchByAuthor/{author}")
	@ResponseStatus(HttpStatus.OK)
	public Set<BookDTO> searchByAuthor(String author) {
		return bookService.searchByAuthor(author);
	}

	@PutMapping("/updateBook/{isbn}")
	@ResponseStatus(HttpStatus.OK)
	public BookDTO updateBook(Long isbn, @RequestBody BookDTO bookDTO) throws BookNotFoundException {
		System.out.println(bookDTO.toString());
		return bookService.updateBook(isbn, bookDTO);
	}

	@PostMapping("/uploadBooks")
	@ResponseStatus(HttpStatus.OK)
	public String addBooks(@RequestPart(required = true) MultipartFile file) {
		return bookService.uploadBooks(file);
	}

}
