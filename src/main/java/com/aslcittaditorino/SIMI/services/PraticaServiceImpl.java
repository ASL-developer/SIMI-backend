package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import com.aslcittaditorino.SIMI.entities.Contatto;
import com.aslcittaditorino.SIMI.entities.Pratica;
import com.aslcittaditorino.SIMI.exceptions.PraticaServiceException;
import com.aslcittaditorino.SIMI.repositories.ContattoRepository;
import com.aslcittaditorino.SIMI.repositories.PersonaRepository;
import com.aslcittaditorino.SIMI.repositories.PraticaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Autowired
    ContattoRepository contattoRepository;

    /*@Override
    public String getNextId() {
        long year = LocalDate.now().getYear();
        return String.valueOf(year).concat("");
    }*/

    @Override
    @Transactional
    public long addPratica(PraticaDTO pratica) {
        Pratica temp;
        if(Optional.ofNullable(pratica.getId()).isEmpty())
            throw new PraticaServiceException("Porco giuda");

        temp = modelMapper.map(pratica,Pratica.class);
        System.out.println(temp);
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
    @Transactional
    public long addContattoToPratica(Long idPratica, Long idContatto) {
        if(Optional.ofNullable(idPratica).isEmpty())
            throw new PraticaServiceException("ERRORE");
        if(Optional.ofNullable(idContatto).isEmpty())
            throw new PraticaServiceException("ERRORE");
        if(praticaRepository.findById(idPratica).isEmpty() || contattoRepository.findById(idContatto).isEmpty()){
            throw new PraticaServiceException("ID PRATICA O CONTATTO NON PRESENTE");
        }else{
            Pratica temp = praticaRepository.findById(idPratica).get();
            List<Contatto> tempList= temp.getContatti();
            tempList.add(contattoRepository.findById(idContatto).get());
            temp.setContatti(tempList);
            praticaRepository.save(temp);
        }
        return 0l;
    }

    @Override
    public List<ContattoDTO> getContattiForPratica(Long idPratica) {
        if(Optional.ofNullable(idPratica).isEmpty())
            throw new PraticaServiceException("ERRORE");
        List<Contatto> contatti = new ArrayList<Contatto>();
        List<ContattoDTO> contattiDTO = new ArrayList<ContattoDTO>();
        contattoRepository.findContattosByPratica(praticaRepository.findById(idPratica).get()).ifPresent(list->{
            list.stream().forEach(item->{
                Contatto temp = new Contatto();
                temp.setId(item.getId());
                temp.setPratica(item.getPratica());
                temp.setPersona(item.getPersona());
                temp.setCausale(item.getCausale());
                temp.setHbsAg(item.getHbsAg());
                temp.setAntiHBs(item.getAntiHBs());
                temp.setAntiHBc(item.getAntiHBc());
                temp.setAntiHBcIgM(item.getAntiHBcIgM());
                temp.setAntiHBe(item.getAntiHBe());
                temp.setAntiHCV(item.getAntiHCV());

                contatti.add(temp);
            });
            
        });
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
