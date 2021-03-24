package com.aslcittaditorino.SIMI.DTOs;

import com.aslcittaditorino.SIMI.entities.Persona;
import com.aslcittaditorino.SIMI.entities.Pratica;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ProvvedimentoDTO {
    private Long id;
    @NotNull
    @NotEmpty
    private String tipo;
    @NotNull
    @NotEmpty
    private Date data;
    private int persone;
    private Long dosaggio;
}
