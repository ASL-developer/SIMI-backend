package com.aslcittaditorino.SIMI.DTOs;

import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class DiagnosiDTO {
    private long id;
    private String tipo;
    private String classe;
    private String ricovero;
    private String dottore;
    private Date dataPrimaDiagnosi;
    private boolean risolta;
    private Date dataRisoluzione;
}
