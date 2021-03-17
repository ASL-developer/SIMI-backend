package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Session {
    @Id
    private String username;
    private String token;
}
