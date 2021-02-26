package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
@Entity
@Data
public class Diagnosi {
    @Id
    private long id;
    private String tipo;
    private String classe;
    private String ricovero;
    private String dottore;
    private Date dataPrimaDiagnosi;
    private boolean risolta;
    private Date dataRisoluzione;


}
