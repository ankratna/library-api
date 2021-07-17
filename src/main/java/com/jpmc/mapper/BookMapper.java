package com.jpmc.mapper;

import com.jpmc.dao.TagRepository;
import com.jpmc.dto.BookDTO;
import com.jpmc.entity.Book;
import com.jpmc.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BookMapper {

	@Autowired
	private TagRepository tagRepository;

	// public BookMapper(TagRepository tagRepository){
	// this.tagRepository = tagRepository;
	// }

	public void mapDtoToEntity(BookDTO bookDTO, Book book) {
		book.setIsbn(bookDTO.getIsbn());
		book.setAuthor(bookDTO.getAuthor());
		book.setTitle(bookDTO.getTitle());

		if (Objects.isNull(book.getTags())) {
			book.setTags(new HashSet<>());
		}

		bookDTO.getTags().stream().forEach(tagName -> {
			Tag tag = tagRepository.findByName(tagName);
			if (Objects.isNull(tag)) {
				tag = new Tag();
				tag.setBooks(new HashSet<>());
			}
			tag.setName(tagName);
			book.addTag(tag);
		});
	}

	public BookDTO mapEntityToDto(Book book) {
		BookDTO responseDto = new BookDTO();
		responseDto.setIsbn(book.getIsbn());
		responseDto.setAuthor(book.getAuthor());
		responseDto.setTitle(book.getTitle());
		responseDto.setTags(book.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
		return responseDto;
	}

}
