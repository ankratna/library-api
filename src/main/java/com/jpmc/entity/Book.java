package com.jpmc.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "BOOK")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
	@SequenceGenerator(name = "student_sequence", sequenceName = "book_sequence")
	private Integer id;

	@Column(nullable = false)
	@NotNull(message = "isbn number can not be null")
	private Long isbn;

	@Column(nullable = false)
	@NotNull(message = "title of book can not be null")
	private String title;

	@Column(nullable = false)
	@NotNull(message = "author of book can not be null")
	private String author;

	@Column(nullable = false)
	private Boolean deleted = false;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@JoinTable(name = "BOOK_TAG", joinColumns = { @JoinColumn(name = "BOOK_ID") },
			inverseJoinColumns = { @JoinColumn(name = "TAG_ID") })
	private Set<Tag> tags;

	public void addTag(Tag tag) {
		this.tags.add(tag);
		tag.getBooks().add(this);
	}

	public void removeTag(Tag tag) {
		this.getTags().remove(tag);
		tag.getBooks().remove(this);
	}

	public void removeTags() {
		for (Tag course : new HashSet<>(this.getTags())) {
			removeTag(course);
		}
	}

}
