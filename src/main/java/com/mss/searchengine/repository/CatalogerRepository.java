package com.mss.searchengine.repository;

import com.mss.searchengine.model.Cataloger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// repository layer to interact with the database
@Repository
public interface CatalogerRepository extends JpaRepository<Cataloger, Long> {

    @Query(value = "SELECT email FROM cataloger where email = :email", nativeQuery = true)
    String findEmailByEmail(@Param(value = "email") String email);

    Optional<Cataloger> findByEmail(String email);
}
