package com.aslcittaditorino.SIMI.repositories;


import com.aslcittaditorino.SIMI.entities.Contatto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContattoRepository extends JpaRepository<Contatto,Long> {
}
