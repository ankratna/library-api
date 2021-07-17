package com.jpmc.service.impl;


import com.jpmc.domainobject.BookDO;
import com.jpmc.service.BookService;
import org.springframework.stereotype.Service;

@Service
public class DefaultBookService implements BookService {
    @Override
    public void createBook(BookDO bookDO)
    {
        System.out.println(" in create book service");


    }
}
