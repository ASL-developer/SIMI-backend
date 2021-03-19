package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import java.util.Date;

@Data
public class PraticaDTO {
    private Long id;
    private Date dataSegnalazione;
    private Date dataRicezione;
    private String struttDenunciante;
    private String operatore;
    private String stato;


}
