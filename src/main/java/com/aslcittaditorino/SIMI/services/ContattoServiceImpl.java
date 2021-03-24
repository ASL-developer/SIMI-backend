package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.ContattoDTO;
import com.aslcittaditorino.SIMI.DTOs.PersonaDTO;
import com.aslcittaditorino.SIMI.DTOs.PraticaDTO;
import com.aslcittaditorino.SIMI.entities.Contatto;
import com.aslcittaditorino.SIMI.exceptions.ContattoServiceException;
import com.aslcittaditorino.SIMI.repositories.ContattoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ContattoServiceImpl implements ContattoService{
    @Autowired
    ContattoRepository contattoRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public ContattoDTO getContatto(Long id) {
        Optional<Contatto> contatto = contattoRepository.findById(id);
        if(contatto.isEmpty())
            throw new ContattoServiceException("Contatto non trovato");

        return modelMapper.map(contatto.get(), ContattoDTO.class);
    }

    @Override
    @Transactional
    public Long addContatto(ContattoDTO contattoDTO) {
        Contatto contatto = modelMapper.map(contattoDTO,Contatto.class);
        contatto.setId(null);
        return contattoRepository.save(contatto).getId();
    }

    @Override
    @Transactional
    public Long deleteContatto(Long id) {
        Optional<Contatto> contatto = contattoRepository.findById(id);
        if(contatto.isEmpty())
            throw new ContattoServiceException("Contatto Not Found");
        contatto.get().getPersona().getContatti().remove(contatto);
        contatto.get().getPratica().getContatti().remove(contatto);

        contattoRepository.delete(contatto.get());
        return id;
    }

    @Override
    public PraticaDTO getPraticaForContatto(Long id) {
        Optional<Contatto> contatto = contattoRepository.findById(id);
        if(contatto.isEmpty()) throw new ContattoServiceException("Contatto not found");

        return modelMapper.map(contatto.get().getPratica(),PraticaDTO.class);
    }

    @Override
    public PersonaDTO getPersonaForContatto(Long id) {
        Optional<Contatto> contatto = contattoRepository.findById(id);
        if(contatto.isEmpty()) throw new ContattoServiceException("Contatto not found");

        return modelMapper.map(contatto.get().getPersona(),PersonaDTO.class);
    }

    @Override
    public long updateContatto(ContattoDTO contattoDTO) {
        Optional<Contatto> contatto = contattoRepository.findById(contattoDTO.getId());
        if(contatto.isEmpty()) throw new ContattoServiceException("Contatto not found");

        contatto.get().setRelazione(contattoDTO.getRelazione());
        contatto.get().setHbsAg(contattoDTO.getHbsAg());
        contatto.get().setAntiHBs(contattoDTO.getAntiHBs());
        contatto.get().setAntiHBc(contattoDTO.getAntiHBc());
        contatto.get().setAntiHBcIgM(contattoDTO.getAntiHBcIgM());
        contatto.get().setAntiHBe(contattoDTO.getAntiHBe());
        contatto.get().setAntiHCV(contattoDTO.getAntiHCV());

        contattoRepository.save(contatto.get());
        return contatto.get().getId();
    }
}
