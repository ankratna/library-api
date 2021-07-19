package com.jpmc.service.impl;

import com.jpmc.dao.BookRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.mapper.BookMapper;
import com.jpmc.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

@RunWith(MockitoJUnitRunner.class)
class DefaultBookServiceTest {

 @Mock
 BookRepository bookRepository;

 @Autowired
 DefaultBookService bookService;

 @InjectMocks
   BookMapper bookMapper;


    @BeforeEach
    void setUp() {
       MockitoAnnotations.initMocks(this);

    }

    @Test
    void addBook() {
       // BookDTO bookDto = new BookDTO();
     //  Mockito.when(bookRepository)

    }

    @Test
    void updateBook() {
    }

    @Test
    void uploadBooks() {
    }

    @Test
    void getAllBooks() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void searchByAllTags() {
    }

    @Test
    void searchByAnyTags() {
    }

    @Test
    void search() {
    }

    @Test
    void searchByIsbn() {
       Book book = new Book();
       book.setIsbn(1234L);
       book.setAuthor("author1");
       book.setTitle("title1");
       book.setTags(new HashSet<>());
       Mockito.when(bookRepository.findByIsbn(Mockito.anyLong())).thenReturn(book);

       BookDTO actual = bookService.searchByIsbn(1234L);

       BookDTO expected = new BookDTO();
       expected.setIsbn(1234L);
       expected.setAuthor("author1");
       expected.setTitle("title1");
       expected.setTags(new HashSet<>());

       Assertions.assertSame(actual,expected);
    }

    @Test
    void searchByTitle() {
    }

    @Test
    void searchByAuthor() {
    }
}