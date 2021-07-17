package com.jpmc.service;

import com.jpmc.domainobject.BookDO;
import com.jpmc.exception.ConstraintsViolationException;

public interface BookService {

	void createBook(BookDO bookDO) throws Exception;

}
