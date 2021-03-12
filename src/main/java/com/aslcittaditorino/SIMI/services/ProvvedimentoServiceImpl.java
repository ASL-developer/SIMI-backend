package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.PersonaDTO;
import com.aslcittaditorino.SIMI.DTOs.PraticaDTO;
import com.aslcittaditorino.SIMI.DTOs.ProvvedimentoDTO;
import com.aslcittaditorino.SIMI.entities.Provvedimento;
import com.aslcittaditorino.SIMI.exceptions.ProvvedimentoServiceException;
import com.aslcittaditorino.SIMI.repositories.PersonaRepository;
import com.aslcittaditorino.SIMI.repositories.PraticaRepository;
import com.aslcittaditorino.SIMI.repositories.ProvvedimentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProvvedimentoServiceImpl implements ProvvedimentoService {

    @Autowired
    ProvvedimentoRepository provvedimentoRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PraticaRepository praticaRepository;
    @Autowired
    PersonaRepository personaRepository;


    @Override
    public ProvvedimentoDTO getProvvedimento(long id) {
        Optional<Provvedimento> provvedimento = provvedimentoRepository.findById(id);
        if(provvedimento.isEmpty()) throw new ProvvedimentoServiceException("Provvedimento not found");
        return modelMapper.map(provvedimento.get(),ProvvedimentoDTO.class);

    }

    @Override
    public long addProvvedimento(ProvvedimentoDTO provvedimentoDTO) {
        provvedimentoDTO.setId(null);
        return provvedimentoRepository.save(modelMapper.map(provvedimentoDTO,Provvedimento.class)).getId();
    }

    @Override
    public PraticaDTO getPraticaForProvvedimento(long id) {
        Optional<Provvedimento> provvedimento = provvedimentoRepository.findById(id);
        if(provvedimento.isEmpty()) throw new ProvvedimentoServiceException("Provvedimento not found");
        return modelMapper.map(provvedimento.get().getPratica(), PraticaDTO.class);
    }

    @Override
    public PersonaDTO getPersonaForProvvedimento(long id) {
        Optional<Provvedimento> provvedimento = provvedimentoRepository.findById(id);
        if(provvedimento.isEmpty()) throw new ProvvedimentoServiceException("Provvedimento not found");
        return modelMapper.map(provvedimento.get().getPersona(), PersonaDTO.class);
    }
}
