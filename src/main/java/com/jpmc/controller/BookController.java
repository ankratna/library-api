package com.jpmc.controller;

import com.jpmc.datatransferobject.BookDTO;
import com.jpmc.domainobject.BookDO;
import com.jpmc.exception.ConstraintsViolationException;
import com.jpmc.mapper.BookMapper;
import com.jpmc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO insertBook(@Valid @RequestBody BookDTO bookDTO) throws Exception {
        BookDO bookDO = BookMapper.makeBookDO(bookDTO);
        System.out.println(bookDO.getAuthor());
        bookService.createBook(bookDO);
        //  return null;
        return bookDTO;
    }

}

