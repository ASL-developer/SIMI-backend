package com.aslcittaditorino.SIMI.repositories;

import com.aslcittaditorino.SIMI.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
}
