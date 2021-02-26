package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
public class Provvedimento {
    @Id
    private Long id;
    private String tipo;
    private Date dataProvvedimento;
    private int numeroPersone;
    private Long dosaggio;

    @ManyToOne
    private Persona persona;
    @ManyToOne
    private Pratica pratica;

}
