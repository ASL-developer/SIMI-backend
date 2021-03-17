package com.aslcittaditorino.SIMI.repositories;


import com.aslcittaditorino.SIMI.entities.Contatto;
import com.aslcittaditorino.SIMI.entities.Pratica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContattoRepository extends JpaRepository<Contatto,Long> {
    public Optional<List<Contatto>> findContattosByPratica(Pratica pratica);
}
