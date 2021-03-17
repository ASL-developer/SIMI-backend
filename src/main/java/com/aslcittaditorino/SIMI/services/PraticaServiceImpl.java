package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import com.aslcittaditorino.SIMI.entities.*;
import com.aslcittaditorino.SIMI.exceptions.PraticaServiceException;
import com.aslcittaditorino.SIMI.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    DiagnosiRepository diagnosiRepository;
    @Autowired
    MorsicaturaRepository morsicaturaRepository;
    @Autowired
    ProvvedimentoRepository provvedimentoRepository;

    public List<MaxiPraticaDTO> getAllPratiche(){
        System.out.println("ENTRATO");
        return praticaRepository.findAll().stream().map(item->{
            MaxiPraticaDTO tempDTO = new MaxiPraticaDTO();
            System.out.println("test");
            tempDTO.setId(item.getId());
            tempDTO.setDataSegnalazione(item.getDataSegnalazione());
            tempDTO.setDataRicezione(item.getDataRicezione());
            tempDTO.setStruttDenunciante(item.getStruttDenunciante());
            tempDTO.setDataRegistrazione(item.getDataRegistrazione());
            tempDTO.setDataArchiviazione(item.getDataArchiviazione());
            tempDTO.setOperatore(item.getOperatore());
            tempDTO.setDataSimi(item.getDataSimi());
            tempDTO.setStato(item.getStato());
            System.out.println(tempDTO);
            tempDTO.setPaziente(modelMapper.map(item.getPaziente(),PersonaDTO.class));
            if(!item.getProvvedimenti().isEmpty()) {
                System.out.println("saving provvedimenti");
                tempDTO.setProvvedimenti(
                        item.getProvvedimenti()
                                .stream()
                                .map(prov -> {
                                    ProvvedimentoDTO provDTO = modelMapper.map(prov, ProvvedimentoDTO.class);
                                    provDTO.setCodfProprietario(prov.getPersona().getCodf());
                                    return provDTO;
                                }).collect(Collectors.toList()));
            }
            if(!item.getContatti().isEmpty()) {
                System.out.println("saving contatti");
                tempDTO.setContatti(item.getContatti().stream().map(cont -> {
                    ContattoDTO contDTO = modelMapper.map(cont, ContattoDTO.class);
                    contDTO.setCodfProprietario(cont.getPersona().getCodf());
                    return contDTO;
                }).collect(Collectors.toList()));
            }
            if(!item.getContatti().isEmpty()) {
                System.out.println("saving persona correlata");
                tempDTO.setCorrelati(item.getContatti().stream().map(pers -> {
                    return modelMapper.map(pers, PersonaDTO.class);
                }).collect(Collectors.toList()));
            }
            if(!item.getDiagnosi().isEmpty()) {
                System.out.println("saving diagnosi");
                tempDTO.setDiagnosiList(item.getDiagnosi().stream().map(diag -> {
                    return modelMapper.map(diag, DiagnosiDTO.class);
                }).collect(Collectors.toList()));
            }
            if(item.getMorsicatura()!=null) {
                System.out.println("saving morsicatura");
                tempDTO.setMorsicatura(modelMapper.map(item.getMorsicatura(), MorsicaturaDTO.class));
            }
            if(item.getMorsicatura()!=null) {
                if(item.getMorsicatura().getProprietario()!=null) {
                    System.out.println("saving provvedimenti persona");
                    tempDTO.setProprietario(modelMapper.map(item.getMorsicatura().getProprietario(), PersonaDTO.class));
                }
            }
            return tempDTO;
        }).collect(Collectors.toList());
    }


    public long getNextPraticaId() {
        long year = LocalDate.now().getYear();
        Optional<Long> id = praticaRepository.findNextIdByYear(String.valueOf(year));
        if(id.isPresent())
            return id.get();
        else
            return Long.valueOf(String.valueOf(year).concat("0001"));

    }

    @Override
    @Transactional
    public long addPratica(PraticaDTO praticaDTO) {
        Optional<Pratica> pratica = praticaRepository.findById(praticaDTO.getId());
        if(pratica.isPresent())
            return getNextPraticaId();
        praticaRepository.save(modelMapper.map(praticaDTO, Pratica.class));
        return praticaDTO.getId();
    }


    @Override
    @Transactional
    public PraticaDTO getPratica(Long id) {
        Pratica pratica = praticaRepository.findById(id).get();
        if(Optional.ofNullable(pratica).isEmpty())
            throw new PraticaServiceException("Pratica not found");
        else
            return modelMapper.map(pratica,PraticaDTO.class);
    }

    @Override
    public long updatePratica(PraticaDTO praticaDTO) {
        Optional<Pratica> pratica = praticaRepository.findById(praticaDTO.getId());
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica not found");

        pratica.get().setDataSegnalazione(praticaDTO.getDataSegnalazione());
        pratica.get().setDataArchiviazione(praticaDTO.getDataArchiviazione());
        pratica.get().setDataRegistrazione(praticaDTO.getDataRegistrazione());
        pratica.get().setStato(praticaDTO.getStato());
        pratica.get().setOperatore(praticaDTO.getOperatore());
        pratica.get().setDataSimi(praticaDTO.getDataSimi());
        pratica.get().setStruttDenunciante(praticaDTO.getStruttDenunciante());

        praticaRepository.save(pratica.get());
        return 0;
    }

    @Override
    @Transactional
    public boolean addContattoToPratica(Long idPratica, Long idContatto) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        Optional<Contatto> contatto = contattoRepository.findById(idContatto);
        if(!contatto.isPresent())
            throw new PraticaServiceException("Contatto Not Found");

        pratica.get().addContatto(contatto.get());

        praticaRepository.save(pratica.get());
        contattoRepository.save(contatto.get());
        return true;
    }

    @Override
    public List<ContattoDTO> getContattiForPratica(Long idPratica) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica not found");

        return pratica.get().getContatti()
                .stream()
                .map( c -> { return modelMapper.map(c, ContattoDTO.class); })
                .collect(Collectors.toList());

    }

    @Override
    public boolean addPazienteToPratica(Long idPratica, String codf) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        Optional<Persona> persona = personaRepository.getByCodfEquals(codf);
        if(!persona.isPresent())
            throw new PraticaServiceException("Persona Not Found");

        //TODO: controllare funzionamento loop
        pratica.get().setPaziente(persona.get());
        praticaRepository.save(pratica.get());
        personaRepository.save(persona.get());
        return true;
    }

    @Override
    public PersonaDTO getPazienteForPratica(Long idPratica) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        return modelMapper.map(pratica.get().getPaziente(),PersonaDTO.class);
    }

    @Override
    public boolean addDiagnosiToPratica(Long idPratica, Long idDiagnosi) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        Optional<Diagnosi> diagnosi = diagnosiRepository.findById(idDiagnosi);
        if(!diagnosi.isPresent()) throw new PraticaServiceException("Diagnosi not found");

        pratica.get().addDiagnosi(diagnosi.get());
        praticaRepository.save(pratica.get());
        diagnosiRepository.save(diagnosi.get());

        return true;
    }

    @Override
    public List<DiagnosiDTO> getAllDiagnosiForPratica(Long idPratica) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        return pratica.get().getDiagnosi().stream()
                .map(item->{return modelMapper.map(item,DiagnosiDTO.class);})
                .collect(Collectors.toList());
    }

    @Override
    public boolean addMorsicaturaToPratica(Long idPratica, Long idMorsicatura) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        Optional<Morsicatura> morsicatura = morsicaturaRepository.findById(idMorsicatura);
        if(!morsicatura.isPresent()) throw new PraticaServiceException("Morsicatura not found");

        pratica.get().setMorsicatura(morsicatura.get());
        praticaRepository.save(pratica.get());
        morsicaturaRepository.save(morsicatura.get());

        return true;
    }

    @Override
    public MorsicaturaDTO getMorsicaturaForPratica(Long idPratica) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");
        return modelMapper.map(pratica.get().getMorsicatura(),MorsicaturaDTO.class);
    }

    @Override
    public boolean addProvvedimentoToPratica(Long idPratica, Long idProvvedimento) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        Optional<Provvedimento> provvedimento = provvedimentoRepository.findById(idProvvedimento);
        if(!provvedimento.isPresent()) throw new PraticaServiceException("Morsicatura not found");

        pratica.get().addProvvedimento(provvedimento.get());
        praticaRepository.save(pratica.get());
        provvedimentoRepository.save(provvedimento.get());

        return true;
    }

    @Override
    public List<ProvvedimentoDTO> getProvvedimentiForPratica(Long idPratica) {
        Optional<Pratica> pratica = praticaRepository.findById(idPratica);
        if(!pratica.isPresent())
            throw new PraticaServiceException("Pratica Not Found");

        return pratica.get().getProvvedimenti().stream()
                .map(item->{return modelMapper.map(item,ProvvedimentoDTO.class);})
                .collect(Collectors.toList());
    }
}
