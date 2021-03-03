package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.ProvvedimentoDTO;
import org.springframework.stereotype.Service;

@Service
public interface ProvvedimentoService {

    public ProvvedimentoDTO getProvvedimento(long id);

    public long addProvvedimento(ProvvedimentoDTO provvedimentoDTO);

    //public PraticaDTO getPraticaForProvvedimento(long id);

    //public PersonaDTO getPersonaForProvvedimento(long id);

}
