package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class CorrelatoDTO {

    private PersonaDTO paziente;
    private ContattoDTO contatto;
    private List<ProvvedimentoDTO> provvedimenti;

}
