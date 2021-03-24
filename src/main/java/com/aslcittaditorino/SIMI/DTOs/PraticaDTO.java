package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class PraticaDTO {
    private Long id;
    @NotNull
    @NotEmpty
    private Date dataSegnalazione;
    @NotNull
    @NotEmpty
    private Date dataRicezione;
    @NotNull
    @NotEmpty
    private String struttDenunciante;
    @NotNull
    @NotEmpty
    private String operatore;
    @NotNull
    @NotEmpty
    private String stato;


}
