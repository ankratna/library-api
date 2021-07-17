package com.jpmc.dataaccessobject;

import com.jpmc.domainobject.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

	public Tag findByName(String name);

}
