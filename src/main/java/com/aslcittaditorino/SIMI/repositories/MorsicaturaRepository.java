package com.aslcittaditorino.SIMI.repositories;

import com.aslcittaditorino.SIMI.entities.Morsicatura;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Date;

@Repository
public interface MorsicaturaRepository extends JpaRepository<Morsicatura,Long> {

}
