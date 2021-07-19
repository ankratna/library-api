package com.jpmc.service.impl;

import com.jpmc.dao.BookRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.entity.Tag;
import com.jpmc.exception.BookAlreadyExistException;
import com.jpmc.exception.BookNotFoundException;
import com.jpmc.mapper.BookMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DefaultBookServiceTest {

	@Autowired
	DefaultBookService bookService;

	@MockBean
	BookRepository bookRepository;

	Set<BookDTO> mockSearchResults;

	BookDTO bookDTO1;

	BookDTO bookDTO2;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		mockSearchResults = new HashSet<>();
		bookDTO1 = new BookDTO();
		bookDTO1.setTitle("title1");
		bookDTO1.setAuthor("author1");
		bookDTO1.setIsbn(123L);
		Set<String> tags1 = new HashSet<>();
		tags1.addAll(Arrays.asList("tag1", "tag2", "tag3"));
		bookDTO1.setTags(tags1);

		bookDTO2 = new BookDTO();
		bookDTO2.setTitle("title2");
		bookDTO2.setAuthor("author2");
		bookDTO2.setIsbn(234L);
		Set<String> tags2 = new HashSet<>();
		tags2.addAll(Arrays.asList("tag4", "tag5"));
		bookDTO2.setTags(tags2);

		mockSearchResults.add(bookDTO1);
		mockSearchResults.add(bookDTO2);

	}

	@Test
	void addBook() throws Exception {
		Book book = new Book();
		Mockito.when(bookRepository.findByIsbn(123L)).thenReturn(null);
		Mockito.when(bookRepository.save(book)).thenReturn(book);
		BookDTO bookDTO = bookService.addBook(bookDTO1);
		Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(123L);
		Mockito.verify(bookRepository, Mockito.times(1)).save(Mockito.any(Book.class));
	}

	@Test
	void addBookIfAlreadyExist() {
		Book book = new Book();
		Mockito.when(bookRepository.findByIsbn(123L)).thenReturn(book);
		Mockito.when(bookRepository.save(book)).thenReturn(book);
		try {
			BookDTO bookDTO = bookService.addBook(bookDTO1);
		}
		catch (Exception e) {
			Assertions.assertTrue(e instanceof BookAlreadyExistException);
		}
		Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(123L);
		Mockito.verify(bookRepository, Mockito.times(0)).save(Mockito.any(Book.class));
	}

	@Test
	void deleteBook() throws Exception {
		Book book = new Book();
		book.setIsbn(123L);
		book.setId(1);
		HashSet<Tag> tags = new HashSet<>();
		book.setTags(tags);
		Mockito.when(bookRepository.findByIsbn(Mockito.anyLong())).thenReturn(book);
		bookService.deleteBook(123L);
		Mockito.verify(bookRepository, Mockito.times(1)).deleteById(1);

	}

	@Test
	void deleteBookIfNotPresent() {
		Mockito.when(bookRepository.findByIsbn(Mockito.anyLong())).thenReturn(null);
		try {
			bookService.deleteBook(123L);
		}
		catch (Exception e) {
			Assertions.assertTrue(e instanceof BookNotFoundException);
		}
		Mockito.verify(bookRepository, Mockito.times(0)).deleteById(1);

	}

	@Test
	void searchByIsbn() throws Exception {
		Book book = new Book();
		book.setIsbn(123L);
		book.setId(1);
		HashSet<Tag> tags = new HashSet<>();
		book.setTags(tags);
		Mockito.when(bookRepository.findByIsbn(Mockito.anyLong())).thenReturn(book);
		bookService.searchByIsbn(123L);
		Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(123L);
	}

	@Test
	void searchByIsbnIfNotPresent() {
		Book book = new Book();
		book.setIsbn(123L);
		book.setId(1);
		HashSet<Tag> tags = new HashSet<>();
		book.setTags(tags);
		Mockito.when(bookRepository.findByIsbn(Mockito.anyLong())).thenReturn(book);
		try {
			bookService.searchByIsbn(123L);
		}
		catch (Exception e) {
			Assertions.assertTrue(e instanceof BookNotFoundException);
		}
		Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(123L);
	}

}