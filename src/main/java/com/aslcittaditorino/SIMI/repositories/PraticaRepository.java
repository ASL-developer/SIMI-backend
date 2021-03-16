package com.aslcittaditorino.SIMI.repositories;

import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PraticaRepository extends JpaRepository<Pratica,Long> {

    @Query(value = "select max(id) from pratica where id like ':year%'",nativeQuery = true)
    Optional<Long> findNextIdByYear(@Param("year")String year);
}
