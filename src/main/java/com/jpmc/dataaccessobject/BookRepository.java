package com.jpmc.dataaccessobject;

import com.jpmc.domainobject.BookDO;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookDO,Long> {

}
