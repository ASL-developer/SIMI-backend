package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import com.aslcittaditorino.SIMI.entities.*;
import com.aslcittaditorino.SIMI.exceptions.PraticaServiceException;
import com.aslcittaditorino.SIMI.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Autowired
    MorsicaturaService morsicaturaService;
    @Autowired
    PersonaService personaService;

    public List<MaxiPraticaDTO> getAllPratiche(){
        System.out.println("started getAllPratiche elab");
        return praticaRepository.findAll().stream().map(item->{
            MaxiPraticaDTO tempDTO = new MaxiPraticaDTO();
            tempDTO.setId(item.getId());
            tempDTO.setDataSegnalazione(item.getDataSegnalazione());
            tempDTO.setDataRicezione(item.getDataRicezione());
            tempDTO.setStruttDenunciante(item.getStruttDenunciante());
            tempDTO.setOperatore(item.getOperatore());
            tempDTO.setStato(item.getStato());
            tempDTO.setPaziente(modelMapper.map(item.getPaziente(),PersonaDTO.class));



            if(!item.getContatti().isEmpty()){
                tempDTO.setCorrelati(item.getContatti().stream().map(cont->{
                    ContattoDTO contattoDTO = modelMapper.map(cont,ContattoDTO.class);
                    List<ProvvedimentoDTO> provvedimenti = item.getProvvedimenti().stream().filter(prov->{
                        return prov.getPersona() == cont.getPersona();
                    }).map(prov-> {
                        return modelMapper.map(prov, ProvvedimentoDTO.class);
                    }).collect(Collectors.toList());
                    PersonaDTO personaDTO = modelMapper.map(cont.getPersona(),PersonaDTO.class);
                    CorrelatoDTO correlatoDTO = new CorrelatoDTO();
                    correlatoDTO.setContatto(contattoDTO);
                    correlatoDTO.setPaziente(personaDTO);
                    correlatoDTO.setProvvedimenti(provvedimenti);
                    return correlatoDTO;
                }).collect(Collectors.toList()));
            }
            if(!item.getDiagnosi().isEmpty()) {
                tempDTO.setDiagnosiList(item.getDiagnosi().stream().map(diag -> {
                    return modelMapper.map(diag, DiagnosiDTO.class);
                }).collect(Collectors.toList()));
            }
            if(item.getMorsicatura()!=null) {
                tempDTO.setMorsicatura(modelMapper.map(item.getMorsicatura(), MorsicaturaDTO.class));
            }
            if(item.getMorsicatura()!=null) {
                if(item.getMorsicatura().getProprietario()!=null) {
                    tempDTO.setProprietario(modelMapper.map(item.getMorsicatura().getProprietario(), PersonaDTO.class));
                }
            }
            System.out.println(tempDTO);
            return tempDTO;
        }).collect(Collectors.toList());
    }

    @Transactional
    public long addMaxiPratica(MaxiPraticaDTO maxiPraticaDTO){
        if(praticaRepository.findById(maxiPraticaDTO.getId()).isPresent() || maxiPraticaDTO.getId() != getNextPraticaId())
            throw new PraticaServiceException("ID NON CORRETTO");
        Pratica pratica = new Pratica();

        pratica.setId(maxiPraticaDTO.getId());
        pratica.setDataSegnalazione(maxiPraticaDTO.getDataSegnalazione());
        pratica.setDataRicezione(maxiPraticaDTO.getDataRicezione());
        pratica.setStruttDenunciante(maxiPraticaDTO.getStruttDenunciante());
        pratica.setOperatore(maxiPraticaDTO.getOperatore());
        pratica.setStato(maxiPraticaDTO.getStato());

        pratica = praticaRepository.save(pratica);

        Optional<Persona> paziente=null;

        if(maxiPraticaDTO.getPaziente()!=null){
            paziente = personaRepository.getByCodFEquals(maxiPraticaDTO.getPaziente().getCodF());
            if(paziente.isPresent())
                paziente = Optional.ofNullable(personaRepository.save(paziente.get()));
            else {
                if(maxiPraticaDTO.getPaziente().getCodF().length()!=16) throw new PraticaServiceException("INSERITO DOICE FISCALE INVALIDO");
                paziente = Optional.ofNullable(personaRepository.save(modelMapper.map(maxiPraticaDTO.getPaziente(), Persona.class)));
            }
            pratica.setPaziente(paziente.get());
            paziente.get().addPratica(pratica);
            personaRepository.save(paziente.get());
            pratica = praticaRepository.save(pratica);
        }
        System.out.println("done paziente");
        Morsicatura morsicatura = null;
        if(maxiPraticaDTO.getMorsicatura()!=null) {
            morsicatura = morsicaturaRepository.save(modelMapper.map(maxiPraticaDTO.getMorsicatura(),Morsicatura.class));
            pratica.setMorsicatura(morsicatura);
            morsicatura.setPratica(pratica);
            morsicaturaRepository.save(morsicatura);
            pratica = praticaRepository.save(pratica);
        }
        Persona proprietario = null;
        if(maxiPraticaDTO.getProprietario()!=null){
            proprietario = personaRepository.save(modelMapper.map(maxiPraticaDTO.getProprietario(),Persona.class));
            pratica = praticaRepository.save(pratica);
            proprietario =personaRepository.save(proprietario);
            pratica.getMorsicatura().setProprietario(proprietario);
            proprietario.addMorsicatura(pratica.getMorsicatura());
            pratica = praticaRepository.save(pratica);
            personaRepository.save(proprietario);
        }


        System.out.println("acquired morsicatura");

        if(maxiPraticaDTO.getDiagnosiList()!=null){
            List<DiagnosiDTO> listDiagnosi= maxiPraticaDTO.getDiagnosiList();
            for (DiagnosiDTO diagnosiDTO : listDiagnosi) {
                Diagnosi diagnosi = modelMapper.map(diagnosiDTO,Diagnosi.class);
                diagnosi = diagnosiRepository.save(diagnosi);
                diagnosi.setPratica(pratica);
                pratica.addDiagnosi(diagnosi);
                diagnosiRepository.save(diagnosi);
                pratica = praticaRepository.save(pratica);
            }
        }

        System.out.println("acquired diagnosi");
        pratica = praticaRepository.save(pratica);

        System.out.println("starting correlati");
        List<CorrelatoDTO> listCorrelati = maxiPraticaDTO.getCorrelati();
        if(listCorrelati!=null)
            for(CorrelatoDTO correlatoDTO : listCorrelati){

                Persona persona = modelMapper.map(correlatoDTO.getPaziente(),Persona.class);
                Contatto contatto = modelMapper.map(correlatoDTO.getContatto(),Contatto.class);
                persona.addContatto(contatto);
                pratica.addContatto(contatto);
                contatto.setPratica(pratica);
                contatto.setPersona(persona);

                List<ProvvedimentoDTO> listProvvedimenti = correlatoDTO.getProvvedimenti();
                for(ProvvedimentoDTO provvedimentoDTO : listProvvedimenti){
                    Provvedimento provvedimento = modelMapper.map(provvedimentoDTO,Provvedimento.class);
                    provvedimento.setPersona(persona);
                    provvedimento.setPratica(pratica);
                    pratica.addProvvedimento(provvedimento);
                    persona.addProvvedimento(provvedimento);
                    provvedimentoRepository.save(provvedimento);
                }
                personaRepository.save(persona);
                contattoRepository.save(contatto);
            };


        System.out.println("acquired provvedimenti");

        return praticaRepository.save(pratica).getId();

    }

    /*@Override
    @Transactional
    public long updateMaxiPratica(MaxiPraticaDTO maxiPraticaDTO) {
        Optional<Pratica> oldPratica=praticaRepository.findById(maxiPraticaDTO.getId());

        if(!oldPratica.isPresent()) throw new PraticaServiceException("Non esiste nessuna pratica con id selezionato");
        Pratica newPratica = modelMapper.map(maxiPraticaDTO,Pratica.class);
        newPratica.setId(oldPratica.get().getId());
        newPratica.setPaziente(oldPratica.get().getPaziente());
        newPratica.setDiagnosi(oldPratica.get().getDiagnosi());
        newPratica.setContatti(oldPratica.get().getContatti());
        newPratica.setProvvedimenti(oldPratica.get().getProvvedimenti());


        Morsicatura oldMorsicatura = oldPratica.get().getMorsicatura();
        if(oldMorsicatura != null){
            if(Optional.ofNullable(maxiPraticaDTO.getMorsicatura()).isEmpty()) throw new PraticaServiceException("Cancellata unica morsicatura");
            oldMorsicatura.setDataMorsicatura(maxiPraticaDTO.getMorsicatura().getDataMorsicatura());
            oldMorsicatura.setAnimale(maxiPraticaDTO.getMorsicatura().getAnimale());
            oldMorsicatura.setSedeLesione(maxiPraticaDTO.getMorsicatura().getSedeLesione());
            oldMorsicatura.setGradoEsposizione(maxiPraticaDTO.getMorsicatura().getGradoEsposizione());
            oldMorsicatura.setLuogoEvento(maxiPraticaDTO.getMorsicatura().getLuogoEvento());
            oldMorsicatura.setRischio(maxiPraticaDTO.getMorsicatura().getRischio());
            oldMorsicatura.setDataVaccRab(maxiPraticaDTO.getMorsicatura().getDataVaccRab());
            oldMorsicatura.setDataVaccTet(maxiPraticaDTO.getMorsicatura().getDataVaccTet());
            oldMorsicatura.setImmRab(maxiPraticaDTO.getMorsicatura().getImmRab());
            oldMorsicatura.setImmTet(maxiPraticaDTO.getMorsicatura().getImmTet());
            oldMorsicatura.setNote(maxiPraticaDTO.getMorsicatura().getNote());

            if(maxiPraticaDTO.getProprietario()!=null) {
                Optional<Persona> oldProprietario = personaRepository.getByCodFEquals(maxiPraticaDTO.getProprietario().getCodF());
                Persona newProprietario = modelMapper.map(maxiPraticaDTO.getProprietario(),Persona.class);
                if(oldProprietario.isPresent()){
                    oldProprietario.ifPresent(item->{
                        newProprietario.setProvvedimenti(item.getProvvedimenti());
                        newProprietario.setMorsicature(item.getMorsicature());
                        newProprietario.setContatti(item.getContatti());
                        newProprietario.setPratiche(item.getPratiche());
                    });
                }else{
                    newProprietario.addMorsicatura(oldMorsicatura);
                    oldMorsicatura.setProprietario(newProprietario);
                }
            }else{
                oldMorsicatura.setProprietario(null);
            }
            oldMorsicatura = morsicaturaRepository.save(oldMorsicatura);

        }

        List<Diagnosi> oldDiagnosiList = oldPratica.get().getDiagnosi();
        List<Diagnosi> newDiagnosiList = new ArrayList<Diagnosi>();
        maxiPraticaDTO.getDiagnosiList().forEach(diagnosiDTO -> {
            Diagnosi newDiag = modelMapper.map(diagnosiDTO,Diagnosi.class);
            newDiag.setPratica(oldPratica.get());
            newDiagnosiList.add(newDiag);
        });
        if(!oldDiagnosiList.isEmpty()) {
            if (Optional.ofNullable(maxiPraticaDTO.getDiagnosiList()).isEmpty()) throw new PraticaServiceException("Impossibile avere zero diagnosi");
            oldDiagnosiList.forEach(oldDiagnosi->{
                boolean flag =false;
                for(Diagnosi newDiagnosi : newDiagnosiList){
                    if(newDiagnosi.getId()==oldDiagnosi.getId()) {
                        flag = true;
                        oldDiagnosi.setTipo(newDiagnosi.getTipo());
                        oldDiagnosi.setClasse(newDiagnosi.getClasse());
                        oldDiagnosi.setRicovero(newDiagnosi.getRicovero());
                        oldDiagnosi.setDottore(newDiagnosi.getDottore());
                        oldDiagnosi.setDataPrimaDiagnosi(newDiagnosi.getDataPrimaDiagnosi());
                        oldDiagnosi.setRisolta(newDiagnosi.isRisolta());
                        oldDiagnosi.setDataRisoluzione(newDiagnosi.getDataRisoluzione());
                        diagnosiRepository.save(oldDiagnosi);
                    }
                }
                if(!flag){
                    newPratica.removeDiagnosi(oldDiagnosi);
                    diagnosiRepository.delete(oldDiagnosi);
                }
            });
            newDiagnosiList.forEach(newDiagnosi->{
                boolean flag =false;
                for(Diagnosi oldDiagnosi : newDiagnosiList){
                    if(newDiagnosi.getId()==oldDiagnosi.getId()) {
                        flag = true;
                    }
                }
                if(!flag){
                    newPratica.setId(null);
                    newPratica.addDiagnosi(newDiagnosi);
                    newDiagnosi.setPratica(newPratica);
                    diagnosiRepository.save(newDiagnosi);
                }
            });
        };



        List<Provvedimento> oldProvvedimenti = oldPratica.get().getProvvedimenti();
        List<Provvedimento> newProvvedimenti =
    }
    */

    public long getNextPraticaId() {
        long year = LocalDate.now().getYear();

        Optional<Long> id = praticaRepository.findNextIdByYear(String.valueOf(year));
        if(id.isPresent())
            return id.get()+1;
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
        pratica.get().setStato(praticaDTO.getStato());
        pratica.get().setOperatore(praticaDTO.getOperatore());
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

        Optional<Persona> persona = personaRepository.getByCodFEquals(codf);
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
