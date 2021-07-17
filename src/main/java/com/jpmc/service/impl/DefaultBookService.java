package com.jpmc.service.impl;

import com.jpmc.dao.BookRepository;
import com.jpmc.dao.TagRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.exception.BookAlreadyExistException;
import com.jpmc.exception.BookNotFoundException;
import com.jpmc.mapper.BookMapper;
import com.jpmc.service.BookService;
import com.jpmc.service.Search.SearchStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DefaultBookService implements BookService {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultBookService.class);

	private final BookRepository bookRepository;

	private final TagRepository tagRepository;

	// @Autowired
	private BookMapper bookMapper;

	// @Autowired
	private SearchStrategy searchWithAnyMatch;

	@Autowired
	public DefaultBookService(BookRepository bookRepository, TagRepository tagRepository, BookMapper bookMapper,
			@Qualifier("anyMatch") SearchStrategy searchWithAnyMatch) {
		this.bookRepository = bookRepository;
		this.tagRepository = tagRepository;
		this.bookMapper = bookMapper;
		this.searchWithAnyMatch = searchWithAnyMatch;
	}

	@Override
	public BookDTO addBook(BookDTO bookDTO) throws Exception {
		if (isBookAlreadyExist(bookDTO.getIsbn())) {
			String message = String.format("Book already Exist in Database with isbn: %s", bookDTO.getIsbn());
			LOG.warn(message);
			throw new BookAlreadyExistException(message);
		}

		Book book = new Book();
		bookMapper.mapDtoToEntity(bookDTO, book);
		bookRepository.save(book);

		LOG.info(String.format("Book with isbn : %s successfully created", book.getIsbn()));
		return bookDTO;
	}

	@Override
	public Set<BookDTO> getAllBooks() {
		Set<BookDTO> allBooksDto = new HashSet<>();

		bookRepository.findAll().stream().filter(book -> !book.getDeleted()).forEach(book -> {
			BookDTO bookDTO = bookMapper.mapEntityToDto(book);
			allBooksDto.add(bookDTO);
		});

		return allBooksDto;
	}

	@Override
	public String deleteBook(Long isbn) throws Exception {
		Book book = bookRepository.findByIsbn((Long) isbn);

		if (Objects.isNull(book)) {
			String message = String.format("Book with ISBN: %s does not exist", isbn);
			LOG.warn(message);
			throw new BookNotFoundException(message);
		}

		book.removeTags();
		bookRepository.deleteById(book.getId());

		/*
		 * In case we do not hard delete we can just toggle the deleted flag
		 * book.setDeleted(true); bookRepository.save(book);
		 */

		return String.format("Book with ISBN: %s is deleted", book.getIsbn());
	}

	@Override
	public Set<BookDTO> getAllBooksWithAnyInputSearchTagPresent(List<String> tagNames) {

		return searchWithAnyMatch.searchByTagList(tagNames);

		/*
		 * Set<Long> isbnSetForAllInputTags = new HashSet<>(); for (String tagName :
		 * tagNames) { Set<Long> isbnSetForTag = getSetOfIsbnForGivenTag(tagName);
		 * isbnSetForAllInputTags.addAll(isbnSetForTag); }
		 *
		 * Set<BookDTO> booksDto = isbnSetForAllInputTags.stream().map(isbn ->
		 * bookRepository.findByIsbn(isbn)) .map(book ->
		 * bookMapper.mapEntityToDto(book)).collect(Collectors.toSet());
		 *
		 * return booksDto;
		 */
	}

	/*
	 * private Set<Long> getSetOfIsbnForGivenTag(String tagName) { Set<Long> isbnSetOfTag
	 * = new HashSet<>(); Tag tag = tagRepository.findByName(tagName); if
	 * (Objects.nonNull(tag)) { Set<Book> books = tag.getBooks(); for (Book book : books)
	 * { isbnSetOfTag.add(book.getIsbn()); } } return isbnSetOfTag; }
	 */

	private boolean isBookAlreadyExist(Long isbn) {
		Book book = bookRepository.findByIsbn(isbn);
		return Objects.nonNull(book) && !book.getDeleted();
	}

	/*
	 * private void mapDtoToEntity(BookDTO bookDTO, Book book) {
	 * book.setIsbn(bookDTO.getIsbn()); book.setAuthor(bookDTO.getAuthor());
	 * book.setTitle(bookDTO.getTitle());
	 *
	 * if (Objects.isNull(book.getTags())) { book.setTags(new HashSet<>()); }
	 *
	 * bookDTO.getTags().stream().forEach(tagName -> { Tag tag =
	 * tagRepository.findByName(tagName); if (Objects.isNull(tag)) { tag = new Tag();
	 * tag.setBooks(new HashSet<>()); } tag.setName(tagName); book.addTag(tag); }); }
	 *
	 * private BookDTO mapEntityToDto(Book book) { BookDTO responseDto = new BookDTO();
	 * responseDto.setIsbn(book.getIsbn()); responseDto.setAuthor(book.getAuthor());
	 * responseDto.setTitle(book.getTitle());
	 * responseDto.setTags(book.getTags().stream().map(Tag::getName).collect(Collectors.
	 * toSet())); return responseDto; }
	 */

}
