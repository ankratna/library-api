package com.jpmc.service;

import com.jpmc.datatransferobject.BookDTO;
import com.jpmc.domainobject.Book;

public interface BookService {

	public BookDTO addBook(BookDTO bookDTO) throws Exception;

}
