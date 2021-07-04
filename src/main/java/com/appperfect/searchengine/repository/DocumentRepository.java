package com.appperfect.searchengine.repository;

import com.appperfect.searchengine.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// repository layer to interact with database
@Repository
public interface DocumentRepository extends CrudRepository<Document, Long> {

    List<Document> findAllByCatalogerId(int catalogerId);

}
