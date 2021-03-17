package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.ContattoDTO;
import com.aslcittaditorino.SIMI.DTOs.PersonaDTO;
import com.aslcittaditorino.SIMI.DTOs.PraticaDTO;
import org.springframework.stereotype.Service;

public interface ContattoService {
    public ContattoDTO getContatto(Long id);

    public Long addContatto(ContattoDTO contatto);

    public Long deleteContatto(Long id);

    public PraticaDTO getPraticaForContatto(Long id);

    public PersonaDTO getPersonaForContatto(Long id);

    public long updateContatto(ContattoDTO contatto);

}
