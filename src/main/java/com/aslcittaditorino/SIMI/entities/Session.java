package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Session {
    @Id
    private String username;
    @Column(length = 2048)
    private String token;
}
