package com.aslcittaditorino.SIMI.repositories;

import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Repository
public interface PraticaRepository extends JpaRepository<Pratica,Long> {

}
