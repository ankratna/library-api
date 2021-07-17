package com.jpmc.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BookDTO {

	@NotNull(message = "isbn number can not be null")
	private Long isbn;

	@NotNull(message = "title of book can not be null")
	private String title;

	@NotNull(message = "author of can not be null")
	private String author;

	private Set<String> tags = new HashSet();

}
