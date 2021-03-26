package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@SequenceGenerator(initialValue = 1,
        name="idgen",
        sequenceName = "provvedimentoId",
        allocationSize = 1)
public class Provvedimento extends DataObject{

    private String tipo;
    private Date dataProvvedimento;
    private int numeroPersone;
    private Long dosaggio;

    @ManyToOne
    private Persona persona;
    @ManyToOne
    private Pratica pratica;

}
