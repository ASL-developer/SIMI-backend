package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import com.aslcittaditorino.SIMI.entities.Pratica;
import com.aslcittaditorino.SIMI.exceptions.PraticaServiceException;
import com.aslcittaditorino.SIMI.repositories.PersonaRepository;
import com.aslcittaditorino.SIMI.repositories.PraticaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PraticaServiceImpl implements PraticaService {
    @Autowired
    PersonaRepository personaRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PraticaRepository praticaRepository;

    @Override
    public long addPratica(PraticaDTO pratica) {
        Pratica temp;
        if(Optional.ofNullable(pratica.getId()).isEmpty())
            throw new PraticaServiceException("Porco giuda");

        temp = modelMapper.map(pratica,Pratica.class);
        praticaRepository.save(temp);
        return temp.getId();
    }


    @Override
    public PraticaDTO getPratica(Long id) {
        Pratica temp = new Pratica();
        praticaRepository.findById(id).ifPresent(item->{
            temp.setId(item.getId());
            temp.setDataSegnalazione(item.getDataSegnalazione());
            temp.setDataRicezione(item.getDataRicezione());
            temp.setStruttDenunciante(item.getStruttDenunciante());
            temp.setDataRegistrazione(item.getDataRegistrazione());
            temp.setDataArchiviazione(item.getDataArchiviazione());
            temp.setOperatore(item.getOperatore());
            temp.setDataSimi(item.getDataSimi());
            temp.setStato(item.getStato());

        });
        return modelMapper.map(temp,PraticaDTO.class);
        
    }

    @Override
    public long addContattoToPratica(Long idPratica, Long idContatto) {
        return 0;
    }

    @Override
    public List<ContattoDTO> getContattiForPratica(Long idPratica) {
        return null;
    }

    @Override
    public long addPazienteToPratica(Long idPratica, String codf) {
        return 0;
    }

    @Override
    public PersonaDTO getPazienteForPratica(Long idPratica) {
        return null;
    }

    @Override
    public long addDiagnosiToPratica(Long idPratica, Long idDiagnosi) {
        return 0;
    }

    @Override
    public List<DiagnosiDTO> getAllDiagnosiForPratica(Long idPratica) {
        return null;
    }

    @Override
    public long addMorsicaturaToPratica(Long idPratica, Long idMorsicatura) {
        return 0;
    }

    @Override
    public MorsicaturaDTO getMorsicaturaForPratica(Long idPratica) {
        return null;
    }

    @Override
    public long addProvvedimentoToPratica(Long idPratica, Long idProvvedimento) {
        return 0;
    }

    @Override
    public List<ProvvedimentoDTO> getProvvedimentiForPratica(Long idPratica) {
        return null;
    }
}
