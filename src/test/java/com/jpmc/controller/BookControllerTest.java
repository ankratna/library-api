package com.jpmc.controller;

import com.jpmc.dao.BookRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.service.impl.DefaultBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class)
@WithMockUser
class BookControllerTest {

	@MockBean
	DefaultBookService bookService;

	@Autowired
	MockMvc mockMvc;

	Set<BookDTO> mockSearchResults;

	BookDTO bookDTO1;

	BookDTO bookDTO2;

	@BeforeEach
	void setUp() throws Exception {
		// Mockito.ini(this);
		// mock
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
		tags2.addAll(Arrays.asList("tag4", "tag5", "tag3"));
		bookDTO2.setTags(tags2);

		mockSearchResults.add(bookDTO1);
		mockSearchResults.add(bookDTO2);
	}

	@Test
	void addBook() throws Exception {
		Mockito.when(bookService.addBook(Mockito.any(BookDTO.class))).thenReturn(Mockito.any(BookDTO.class));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/addBook").accept(MediaType.APPLICATION_JSON)
				.content(
						"{\"isbn\":123,\"title\":\"title1\",\"author\":\"author1\",\"tags\":[\"tag1\",\"tag2\",\"tag3\"]}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

	}

	@Test
	void getAllBooks() throws Exception {

		Mockito.when(bookService.getAllBooks()).thenReturn(mockSearchResults);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/getAllBooks")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String actual = "[{\"isbn\":123,\"title\":\"title1\",\"author\":\"author1\",\"tags\":[\"tag1\",\"tag2\",\"tag3\"]},{\"isbn\":234,\"title\":\"title2\",\"author\":\"author2\",\"tags\":[\"tag4\",\"tag5\",\"tag3\"]}]";

		// verify
		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void deleteBookIfPresent() throws Exception {

		Mockito.when(bookService.deleteBook(123L)).thenReturn("SUCCESS");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/book/deleteBook/123")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * @Test //@ExceptionHandler(Exception.class) void deleteBookIfNotPresent() throws
	 * Exception {
	 *
	 * org.assertj.core.api.Assertions.assertThatThrownBy(() ->
	 * bookService.deleteBook(123L)) .isInstanceOf(Exception.class);
	 *
	 *
	 * // Mockito.when(bookService.deleteBook(123L)).thenThrow(new Exception()); //
	 * Mockito.doThrow(new Exception()).when(bookService.deleteBook(123L)); RequestBuilder
	 * requestBuilder = MockMvcRequestBuilders.delete("/book/deleteBook/123")
	 * .accept(MediaType.APPLICATION_JSON);
	 *
	 * MvcResult result = mockMvc.perform(requestBuilder).andReturn();
	 * MockHttpServletResponse response = result.getResponse();
	 *
	 * Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()); }
	 */
	/*
	 * @Test void searchByAnyTags() { }
	 *
	 * @Test void searchByAllTags() { }
	 *
	 * @Test void search() { }
	 *
	 * @Test void searchByIsbn() {
	 *
	 * }
	 *
	 * @Test void searchByTitle() { }
	 *
	 * @Test void searchByAuthor() { }
	 *
	 * @Test void updateBook() { }
	 *
	 * @Test void addBooks() { }
	 */

}