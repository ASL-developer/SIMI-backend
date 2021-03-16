package com.aslcittaditorino.SIMI.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class CorrelatoDTO {

    private PersonaDTO proprietario;
    private ContattoDTO contatto;
    private List<ProvvedimentoDTO> provvedimenti;

}
