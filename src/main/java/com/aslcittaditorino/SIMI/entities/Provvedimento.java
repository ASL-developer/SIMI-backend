package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@SequenceGenerator(initialValue = 1,name="idgen",allocationSize = 1,sequenceName = "provvedimentoId")
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
