package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.PersonaDTO;
import org.springframework.stereotype.Service;

public interface PersonaService {

    public PersonaDTO getPersonaByCodf(String codf);

}
