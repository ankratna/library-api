package com.jpmc.service.impl;

import com.jpmc.dao.BookRepository;
import com.jpmc.dao.TagRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.entity.Tag;
import com.jpmc.exception.BookAlreadyExistException;
import com.jpmc.exception.BookNotFoundException;
import com.jpmc.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultBookService implements BookService {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultBookService.class);

	private final BookRepository bookRepository;

	private final TagRepository tagRepository;

	@Autowired
	public DefaultBookService(final BookRepository bookRepository, final TagRepository tagRepository) {
		this.bookRepository = bookRepository;
		this.tagRepository = tagRepository;
	}

	@Override
	public BookDTO addBook(BookDTO bookDTO) throws Exception {
		if (isBookAlreadyExist(bookDTO.getIsbn())) {
			String message = String.format("Book already Exist in Database with isbn: %s", bookDTO.getIsbn());
			LOG.warn(message);
			throw new BookAlreadyExistException(message);
		}

		Book book = new Book();
		mapDtoToEntity(bookDTO, book);
		bookRepository.save(book);

		LOG.info(String.format("Book with isbn : %s successfully created", book.getIsbn()));
		return bookDTO;
	}

	@Override
	public Set<BookDTO> getAllBooks() {
		Set<BookDTO> allBooksDto = new HashSet<>();

		bookRepository.findAll().stream().filter(book -> !book.getDeleted()).forEach(book -> {
			BookDTO bookDTO = mapEntityToDto(book);
			allBooksDto.add(bookDTO);
		});

		return allBooksDto;
	}

	@Override
	public String deleteBook(Long isbn) throws Exception {
		Book book = bookRepository.findByIsbn((Long) isbn);

		if (Objects.isNull(book)) {
			throw new BookNotFoundException(String.format("Book with ISBN: %s does not exist", isbn));
		}

		book.removeTags();
		bookRepository.deleteById(book.getId());

		return String.format("Book with ISBN: %s is deleted", book.getIsbn());
	}

	private boolean isBookAlreadyExist(Long isbn) {
		Book book = bookRepository.findByIsbn(isbn);
		return Objects.nonNull(book) && !book.getDeleted();
	}

	private void mapDtoToEntity(BookDTO bookDTO, Book book) {
		book.setIsbn(bookDTO.getIsbn());
		book.setAuthor(bookDTO.getAuthor());
		book.setTitle(bookDTO.getTitle());

		if (Objects.isNull(book.getTags())) {
			book.setTags(new HashSet<>());
		}

		bookDTO.getTags().stream().forEach(tagName -> {
			Tag tag = tagRepository.findByName(tagName);
			if (Objects.isNull(tag)) {
				tag = new Tag();
				tag.setBooks(new HashSet<>());
			}
			tag.setName(tagName);
			book.addTag(tag);
		});
	}

	private BookDTO mapEntityToDto(Book book) {
		BookDTO responseDto = new BookDTO();
		responseDto.setIsbn(book.getIsbn());
		responseDto.setAuthor(book.getAuthor());
		responseDto.setTitle(book.getTitle());
		responseDto.setTags(book.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
		return responseDto;
	}

}
