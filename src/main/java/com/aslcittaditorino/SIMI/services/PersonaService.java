package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.ContattoDTO;
import com.aslcittaditorino.SIMI.DTOs.PersonaDTO;
import com.aslcittaditorino.SIMI.entities.Pratica;
import com.aslcittaditorino.SIMI.entities.Provvedimento;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PersonaService {

    public PersonaDTO getPersonaByCodf(String codf);

    public String addPersona(PersonaDTO persona);

    public String updatePersona(PersonaDTO persona);

    //public List<ContattoDTO> getContattiForPersona(String codf);

    //public List<Provvedimento> getProvvedimentiForPersona(String codf);

    //public List<Pratica> getPraticheForPersona(String codf);
}
