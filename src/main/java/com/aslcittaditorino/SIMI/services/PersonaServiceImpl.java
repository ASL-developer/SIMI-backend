package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.PersonaDTO;
import com.aslcittaditorino.SIMI.entities.Persona;
import com.aslcittaditorino.SIMI.exceptions.PersonaServiceException;
import com.aslcittaditorino.SIMI.repositories.PersonaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImpl implements PersonaService {
    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public PersonaDTO getPersonaByCodf(String codf) {
        return null;
    }

    @Override
    public List<PersonaDTO> getAllPersone() {
        if(!personaRepository.findAll().isEmpty()) {
            return personaRepository.findAll().stream().map(item->{
                return modelMapper.map(item,PersonaDTO.class);
            }).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Long addPersona(PersonaDTO personaDTO) {
        System.out.println("eseguo salvataggio");
        Persona persona = modelMapper.map(personaDTO,Persona.class);
        return personaRepository.save(persona).getId();
    }

    @Override
    public String updatePersona(PersonaDTO personaDTO) {
        Optional<Persona> persona = personaRepository.getByCodFEquals(personaDTO.getCodF());
        if(persona.isEmpty()) throw new PersonaServiceException("Persona non presente");

        persona.get().setCodF(personaDTO.getCodF());
        persona.get().setNome(personaDTO.getNome());
        persona.get().setCognome(personaDTO.getCognome());
        persona.get().setCellulare(personaDTO.getCellulare());
        persona.get().setDataDiNascita(personaDTO.getDataDiNascita());
        persona.get().setSesso(personaDTO.getSesso());
        persona.get().setNazionalita(personaDTO.getNazionalita());
        persona.get().setLuogoDiNascita(personaDTO.getLuogoDiNascita());
        persona.get().setResidenza(personaDTO.getResidenza());
        persona.get().setComuneResidenza(personaDTO.getComuneResidenza());
        persona.get().setComuneDomicilio(personaDTO.getComuneDomicilio());
        persona.get().setDomicilio(personaDTO.getDomicilio());
        persona.get().setProfessione(personaDTO.getProfessione());
        persona.get().setCategoriaProfessionale(personaDTO.getCategoriaProfessionale());

        return personaRepository.save(persona.get()).getCodF();
    }
}
