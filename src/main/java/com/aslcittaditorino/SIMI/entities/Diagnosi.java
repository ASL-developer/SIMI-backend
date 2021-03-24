package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Entity
@Data
@SequenceGenerator(initialValue = 1,name="idgen",sequenceName = "diagnosiId")
public class Diagnosi extends DataObject{

    private String tipo;
    private String classe;
    private String ricovero;
    private String dottore;
    private Date dataPrimaDiagnosi;
    private boolean risolta;
    private Date dataRisoluzione;


    @ManyToOne
    private Pratica pratica;
}
