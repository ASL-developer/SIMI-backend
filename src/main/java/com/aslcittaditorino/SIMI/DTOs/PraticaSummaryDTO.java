package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class PraticaSummaryDTO {
    private Long id;
    private Date dataSegnalazione;
    private Date dataRicezione;
    private String struttDenunciante;
    private String operatore;
    private String stato;

    private String diagnosi;
    private List<String> nomi;
    private List<String> cognomi;
    private List<String> codF;

    private long numProvvedimenti;
    private long numProvvedimentiCorrelati;
}
