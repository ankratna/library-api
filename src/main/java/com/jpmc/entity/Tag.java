package com.jpmc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "TAG")
public class Tag implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_sequence")
	@SequenceGenerator(name = "course_sequence", sequenceName = "tag_sequence")
	private Integer id;

	@Column(nullable = false)
	private String name;

	@ManyToMany(mappedBy = "tags")
	@JsonIgnore
	private Set<Book> books;

}
