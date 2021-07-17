package com.jpmc.dao;

import com.jpmc.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BookRepository extends CrudRepository<Book, Integer> {

	public Book findByIsbn(Long isbn);

	public Set<Book> findAll();

}
