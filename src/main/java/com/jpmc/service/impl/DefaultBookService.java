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
import com.jpmc.util.CSVHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class DefaultBookService implements BookService {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultBookService.class);

	private final BookRepository bookRepository;

	private final TagRepository tagRepository;

	private final BookMapper bookMapper;

	private final SearchStrategy searchWithAnyMatch;

	private final SearchStrategy searchWithAllMatch;

	@Autowired
	private BookSpecification bookSpecification;

	@Autowired
	public DefaultBookService(BookRepository bookRepository, TagRepository tagRepository, BookMapper bookMapper,
			@Qualifier("anyMatch") SearchStrategy searchWithAnyMatch,
			@Qualifier("allMatch") SearchStrategy searchWithAllMatch) {
		this.bookRepository = bookRepository;
		this.tagRepository = tagRepository;
		this.bookMapper = bookMapper;
		this.searchWithAnyMatch = searchWithAnyMatch;
		this.searchWithAllMatch = searchWithAllMatch;
	}

	@Override
	public BookDTO addBook(BookDTO bookDTO) throws BookAlreadyExistException {
		if (isBookExist(bookDTO.getIsbn())) {
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
	public BookDTO updateBook(Long isbn, BookDTO bookDTO) throws BookNotFoundException {
		Book book = bookRepository.findByIsbn(isbn);
		if (Objects.isNull(book)) {
			String message = String.format("Book with ISBN: %s does not exist", isbn);
			LOG.warn(message);
			throw new BookNotFoundException(message);
		}
		book.getTags().clear();
		bookMapper.mapDtoToEntity(bookDTO, book);
		Book updatedBook = bookRepository.save(book);
		return bookMapper.mapEntityToDto(updatedBook).get();
	}

	@Override
	public String uploadBooks(MultipartFile file) {
		StringBuilder message = new StringBuilder();

		if (CSVHelper.hasCSVFormat(file)) {
			try {
				List<BookDTO> bookDTOS = CSVHelper.csvToBooks(file.getInputStream());
				for (BookDTO bookDTO : bookDTOS) {
					addBook(bookDTO);
				}
				return message.append("Uploaded the file successfully: " + file.getOriginalFilename()).toString();
			}
			catch (Exception e) {
				return message.append("Could not upload the file: " + file.getOriginalFilename() + "!").toString();
			}
		}

		return message.append("Please upload a csv file!").toString();
	}

	@Override
	public Set<BookDTO> getAllBooks() {
		Set<BookDTO> allBooksDto = new HashSet<>();

		bookRepository.findAll().stream().filter(book -> !book.getDeleted()).forEach(book -> {
			Optional<BookDTO> bookDTO = bookMapper.mapEntityToDto(book);
			if (bookDTO.isPresent()) {
				allBooksDto.add(bookDTO.get());
			}
		});

		return allBooksDto;
	}

	@Override
	public String deleteBook(Long isbn) throws BookNotFoundException {
		Book book = bookRepository.findByIsbn(isbn);

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
	public Set<BookDTO> searchByAllTags(List<String> tagNames) {
		return searchWithAllMatch.searchByTagList(tagNames);
	}

	@Override
	public Set<BookDTO> searchByAnyTags(List<String> tagNames) {
		return searchWithAnyMatch.searchByTagList(tagNames);
	}

	@Override
	public Set<BookDTO> search(BookDTO searchRequest) {

		List<Specification<Book>> specs = new ArrayList<>();
		if (Objects.nonNull(searchRequest.getIsbn())) {
			specs.add(bookSpecification.findWithIsbn(searchRequest.getIsbn()));
		}
		if (Objects.nonNull(searchRequest.getTitle())) {
			specs.add(bookSpecification.findWithTitle(searchRequest.getTitle()));
		}
		if (Objects.nonNull(searchRequest.getAuthor())) {
			specs.add(bookSpecification.findWithAuthor(searchRequest.getAuthor()));
		}

		List<String> inputTagList = new ArrayList<>();
		if (Objects.nonNull(searchRequest.getTags()) && searchRequest.getTags().size() > 0) {
			inputTagList.addAll(searchRequest.getTags());
		}

		if (specs.size() > 0) {
			Specification<Book> finalSpecs = where(specs.get(0));

			for (int i = 1; i < specs.size(); i++) {
				finalSpecs = finalSpecs.and(specs.get(i));
			}

			return bookRepository.findAll(finalSpecs).stream().map(book -> bookMapper.mapEntityToDto(book))
					.filter(bookDTO -> bookDTO.isPresent()).map(bookDTO -> bookDTO.get())
					.filter(bookDTO -> bookDTO.getTags().containsAll(inputTagList)).collect(Collectors.toSet());
		}
		return searchWithAllMatch.searchByTagList(inputTagList);

	}

	@Override
	public BookDTO searchByIsbn(Long isbn) {
		Book book = bookRepository.findByIsbn(isbn);
		Optional<BookDTO> bookDTO = bookMapper.mapEntityToDto(book);
		return bookDTO.isPresent() ? bookDTO.get() : null;
	}

	@Override
	public Set<BookDTO> searchByTitle(String title) {
		return bookRepository.findByTitle(title).stream().map(book -> bookMapper.mapEntityToDto(book))
				.filter(bookDTO -> bookDTO.isPresent()).map(bookDTO -> bookDTO.get()).collect(Collectors.toSet());
	}

	@Override
	public Set<BookDTO> searchByAuthor(String author) {
		return bookRepository.findByAuthor(author).stream().map(book -> bookMapper.mapEntityToDto(book))
				.filter(bookDTO -> bookDTO.isPresent()).map(bookDTO -> bookDTO.get()).collect(Collectors.toSet());
	}

	private boolean isBookExist(Long isbn) {
		Book book = bookRepository.findByIsbn(isbn);
		return Objects.nonNull(book) && !book.getDeleted();
	}

}
