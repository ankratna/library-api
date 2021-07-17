package com.jpmc.service.impl;


import com.jpmc.dataaccessobject.BookRepository;
import com.jpmc.domainobject.BookDO;
import com.jpmc.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultBookService implements BookService {

    private final BookRepository bookRepository;

    public DefaultBookService(final BookRepository bookRepository){
        this.bookRepository =bookRepository;
    }
    @Override
    public void createBook(BookDO bookDO)
    {
        bookRepository.save(bookDO);
        System.out.println("book created");

    }
}
