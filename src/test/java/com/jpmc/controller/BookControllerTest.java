package com.jpmc.controller;

import com.google.gson.Gson;
import com.jpmc.dto.BookDTO;
import com.jpmc.service.impl.DefaultBookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;
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

	Gson gson = new Gson();

	BookDTO bookDTO1;

	BookDTO bookDTO2;

	@BeforeEach
	void setUp() {
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
		tags2.addAll(Arrays.asList("tag4", "tag5"));
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
		String actual = "[{\"isbn\":123,\"title\":\"title1\",\"author\":\"author1\",\"tags\":[\"tag1\",\"tag2\",\"tag3\"]},{\"isbn\":234,\"title\":\"title2\",\"author\":\"author2\",\"tags\":[\"tag4\",\"tag5\"]}]";

		// verify
		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void deleteBook() throws Exception {

		Mockito.when(bookService.deleteBook(123L)).thenReturn("SUCCESS");
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/book/deleteBook/123")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void searchByAnyTags() throws Exception {
		List<String> mockInputTags = Arrays.asList("tag1", "tag4");
		Mockito.when(bookService.searchByAnyTags(mockInputTags)).thenReturn(mockSearchResults);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/searchByAnyTag")
				.content(gson.toJson(mockInputTags)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		String expected = gson.toJson(mockSearchResults);
		String actual = response.getContentAsString();

		JSONAssert.assertEquals(actual, expected, false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	@Test
	void searchByAllTags() throws Exception {

		List<String> mockInputTags = Arrays.asList("tag1", "tag2");
		Mockito.when(bookService.searchByAnyTags(mockInputTags)).thenReturn(mockSearchResults);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/searchByAnyTag")
				.content(gson.toJson(mockInputTags)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		String actual = gson.toJson(mockSearchResults);

		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	@Test
	void search() throws Exception {
		Mockito.when(bookService.search(Mockito.any(BookDTO.class))).thenReturn(mockSearchResults);
		String content = gson.toJson(bookDTO1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/book/search").content(content)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		String actual = gson.toJson(mockSearchResults);

		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void searchByIsbn() throws Exception {
		Mockito.when(bookService.searchByIsbn(123L)).thenReturn(bookDTO1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/searchByIsbn/123")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String actual = gson.toJson(bookDTO1);

		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void searchByAuthor() throws Exception {

		Mockito.when(bookService.searchByAuthor(Mockito.anyString())).thenReturn(mockSearchResults);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/searchByAuthor/author1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String actual = gson.toJson(mockSearchResults);

		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void searchByTitle() throws Exception {
		Mockito.when(bookService.searchByTitle(Mockito.anyString())).thenReturn(mockSearchResults);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/book/searchByTitle/title1")
				.accept(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();
		String actual = gson.toJson(mockSearchResults);

		JSONAssert.assertEquals(actual, response.getContentAsString(), false);
		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	@Test
	void updateBook() throws Exception {
		Mockito.when(bookService.updateBook(123L, bookDTO1)).thenReturn(bookDTO1);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/book/updateBook/123")
				.accept(MediaType.APPLICATION_JSON)
				.content(
						"{\"isbn\":123,\"title\":\"title1\",\"author\":\"author1\",\"tags\":[\"tag1\",\"tag2\",\"tag3\"]}")
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse response = result.getResponse();

		Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

}