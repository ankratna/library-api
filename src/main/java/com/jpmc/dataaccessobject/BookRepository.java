package com.jpmc.dataaccessobject;

import com.jpmc.domainobject.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

	public Book findByIsbn(Long isbn);

}
