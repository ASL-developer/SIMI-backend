package com.aslcittaditorino.SIMI.repositories;

import com.aslcittaditorino.SIMI.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
