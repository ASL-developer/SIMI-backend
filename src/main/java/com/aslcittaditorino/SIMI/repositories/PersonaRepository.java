package com.aslcittaditorino.SIMI.repositories;

import com.aslcittaditorino.SIMI.entities.Persona;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona,Long> {
    public Optional<Persona> getByCodFEquals(String codf);
}
