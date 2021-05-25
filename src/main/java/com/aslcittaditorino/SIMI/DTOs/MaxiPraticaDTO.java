package com.aslcittaditorino.SIMI.DTOs;

import com.aslcittaditorino.SIMI.entities.Provvedimento;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MaxiPraticaDTO {
    private Long id;
    private Date dataSegnalazione;
    private Date dataRicezione;
    private String struttDenunciante;
    private String operatore;
    private String stato;

    private List<ProvvedimentoDTO> provvedimenti;
    private Long pazienteId;
    private PersonaDTO paziente;
    private List<CorrelatoDTO> correlati;
    private List <DiagnosiDTO> diagnosiList;
    private MorsicaturaDTO morsicatura;
    private Long proprietarioId;
    private PersonaDTO proprietario;

}
