package com.jpmc.dao;

import com.jpmc.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

	public Tag findByName(String name);

}
