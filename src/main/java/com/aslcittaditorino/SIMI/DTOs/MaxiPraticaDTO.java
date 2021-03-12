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
    private Date dataRegistrazione;
    private Date dataArchiviazione;
    private String operatore;
    private Date dataSimi;
    private String stato;

    private PersonaDTO paziente;
    private List<ProvvedimentoDTO> provvedimenti;
    private List<ContattoDTO> contatti;
    private List<PersonaDTO> correlati;
    private List <DiagnosiDTO> diagnosiList;
    private MorsicaturaDTO morsicatura;
    private PersonaDTO proprietario;
}
