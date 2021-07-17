package com.jpmc.service.impl;

import com.jpmc.dao.BookRepository;
import com.jpmc.dao.TagRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.entity.Tag;
import com.jpmc.exception.BookAlreadyExistException;
import com.jpmc.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Objects;

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

}
