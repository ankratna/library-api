package com.jpmc.service.impl;

import com.jpmc.dataaccessobject.BookRepository;
import com.jpmc.domainobject.BookDO;
import com.jpmc.exception.BookAlreadyExistException;
import com.jpmc.exception.ConstraintsViolationException;
import com.jpmc.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultBookService implements BookService {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultBookService.class);

	private final BookRepository bookRepository;

	public DefaultBookService(final BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public void createBook(BookDO bookDO) throws Exception {
		if (isBookAlreadyExist(bookDO.getIsbn())) {
			String message = String.format("Book already Exist in Database with isbn: %s", bookDO.getIsbn());
			LOG.warn(message);
			throw new BookAlreadyExistException(message);
		}

		BookDO book;
		try {
			book = bookRepository.save(bookDO);
		}
		catch (DataIntegrityViolationException e) {
			throw new ConstraintsViolationException(e.getMessage());
		}
		System.out.println("book created successfully");

	}

	private boolean isBookAlreadyExist(Long isbn) {
		Optional<BookDO> bookDO = bookRepository.findById(isbn);
		return bookDO.isPresent() && !bookDO.get().getDeleted();
	}

}
