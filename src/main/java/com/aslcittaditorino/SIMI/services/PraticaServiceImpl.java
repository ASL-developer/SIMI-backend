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

    public MaxiPraticaDTO getMaxiPratica(Long id){
        System.out.println("started getAllPratiche elab");
        Optional<Pratica> pratica = praticaRepository.findById(id);
        if(pratica.isEmpty()) throw new PraticaServiceException("Richiesto id non esistente");

        MaxiPraticaDTO tempDTO = new MaxiPraticaDTO();
        tempDTO.setId(pratica.get().getId());
        tempDTO.setDataSegnalazione(pratica.get().getDataSegnalazione());
        tempDTO.setDataRicezione(pratica.get().getDataRicezione());
        tempDTO.setStruttDenunciante(pratica.get().getStruttDenunciante());
        tempDTO.setOperatore(pratica.get().getOperatore());
        tempDTO.setStato(pratica.get().getStato());
        System.out.println(tempDTO);
        tempDTO.setPaziente(modelMapper.map(pratica.get().getPaziente(),PersonaDTO.class));

        tempDTO.setProvvedimenti(pratica.get().getProvvedimenti().stream().filter(provv-> {
                return provv.getPersona().getCodF() == pratica.get().getPaziente().getCodF();
            })
            .map(prov->{return modelMapper.map(prov,ProvvedimentoDTO.class);})
            .collect(Collectors.toList()));

        if(!pratica.get().getContatti().isEmpty()){
            tempDTO.setCorrelati(pratica.get().getContatti().stream().map(cont->{
                ContattoDTO contattoDTO = modelMapper.map(cont,ContattoDTO.class);
                List<ProvvedimentoDTO> provvedimenti = pratica.get().getProvvedimenti().stream().filter(prov->{
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
        if(!pratica.get().getDiagnosi().isEmpty()) {
            tempDTO.setDiagnosiList(pratica.get().getDiagnosi().stream().map(diag -> {
                return modelMapper.map(diag, DiagnosiDTO.class);
            }).collect(Collectors.toList()));
        }
        if(pratica.get().getMorsicatura()!=null) {
            tempDTO.setMorsicatura(modelMapper.map(pratica.get().getMorsicatura(), MorsicaturaDTO.class));
        }
        if(pratica.get().getMorsicatura()!=null) {
            if(pratica.get().getMorsicatura().getProprietario()!=null) {
                tempDTO.setProprietario(modelMapper.map(pratica.get().getMorsicatura().getProprietario(), PersonaDTO.class));
            }
        }
        return tempDTO;

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
            if(paziente.isPresent()) {
                System.out.println("TROVATO PAZIENTE");
                paziente = Optional.ofNullable(personaRepository.save(paziente.get()));
            }
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
            morsicatura.setPratica(pratica);
            morsicaturaRepository.save(morsicatura);
            pratica.setMorsicatura(morsicatura);
            pratica = praticaRepository.save(pratica);
            Optional<Persona> proprietario = null;
            if(maxiPraticaDTO.getProprietario()!=null){
                proprietario = personaRepository.getByCodFEquals(maxiPraticaDTO.getProprietario().getCodF());
                if(!proprietario.isPresent())
                {
                    proprietario = Optional.ofNullable(personaRepository.save(modelMapper.map(maxiPraticaDTO.getProprietario(),Persona.class)));
                }
                proprietario.get().addMorsicatura(pratica.getMorsicatura());
                proprietario =Optional.ofNullable(personaRepository.save(proprietario.get()));
                pratica.getMorsicatura().setProprietario(proprietario.get());
                proprietario.get().addMorsicatura(pratica.getMorsicatura());
                morsicaturaRepository.save(pratica.getMorsicatura());
                praticaRepository.save(pratica);
                personaRepository.save(proprietario.get());
            }
        }
        System.out.println("acquired morsicatura");

        List<DiagnosiDTO> listDiagnosi = maxiPraticaDTO.getDiagnosiList();
        if(maxiPraticaDTO.getDiagnosiList()!=null){
            if(!listDiagnosi.isEmpty()) {
                for (DiagnosiDTO diagnosiDTO : listDiagnosi) {
                    if(diagnosiDTO.getTipo()!="") {
                        Diagnosi diagnosi = modelMapper.map(diagnosiDTO, Diagnosi.class);
                        diagnosi.setId(null);
                        diagnosi.setPratica(pratica);
                        pratica.addDiagnosi(diagnosi);
                        diagnosiRepository.save(diagnosi);
                    }
                }
            }
        }
        System.out.println("acquired diagnosi");
        pratica = praticaRepository.save(pratica);

        List<ProvvedimentoDTO> listProvvedimenti = maxiPraticaDTO.getProvvedimenti();
        if(listProvvedimenti != null && !listProvvedimenti.isEmpty()){
            for(ProvvedimentoDTO provvedimentoDTO : listProvvedimenti){
                Provvedimento provvedimento = modelMapper.map(provvedimentoDTO,Provvedimento.class);
                provvedimento.setPersona(pratica.getPaziente());
                pratica.getPaziente().addProvvedimento(provvedimento);
                personaRepository.save(pratica.getPaziente());
                provvedimento.setPratica(pratica);
                provvedimento = provvedimentoRepository.save(provvedimento);
                pratica.addProvvedimento(provvedimento);
                pratica=praticaRepository.save(pratica);
            };
        }
        System.out.println("acquired provvedimenti");

        List<CorrelatoDTO> listCorrelati = maxiPraticaDTO.getCorrelati();
        if(listCorrelati!=null){
            for (CorrelatoDTO correlatoDTO : listCorrelati) {
                System.out.println(correlatoDTO);
                Optional<Persona> persona = personaRepository.getByCodFEquals(correlatoDTO.getPaziente().getCodF());
                if (!persona.isPresent()) {
                    persona = Optional.ofNullable(modelMapper.map(correlatoDTO.getPaziente(), Persona.class));
                    persona.get().setId(null);
                }
                Contatto contatto = modelMapper.map(correlatoDTO.getContatto(), Contatto.class);
                contatto.setId(null);

                persona = Optional.ofNullable(personaRepository.save(persona.get()));
                contatto = contattoRepository.save(contatto);
                persona.get().addContatto(contatto);
                contatto.setPratica(pratica);
                contatto.setPersona(persona.get());

                List<ProvvedimentoDTO> listProvvedimentiCorr = correlatoDTO.getProvvedimenti();
                if (listProvvedimenti != null) {
                    for (ProvvedimentoDTO provvedimentoDTO : listProvvedimentiCorr) {
                        Provvedimento provvedimento = modelMapper.map(provvedimentoDTO, Provvedimento.class);
                        provvedimento.setId(null);
                        provvedimento.setPersona(persona.get());
                        provvedimento.setPratica(pratica);
                        pratica.addProvvedimento(provvedimento);
                        persona.get().addProvvedimento(provvedimento);
                        personaRepository.save(persona.get());
                        provvedimentoRepository.save(provvedimento);
                    }
                }
                personaRepository.save(persona.get());
                contattoRepository.save(contatto);
                pratica.addContatto(contatto);
                pratica = praticaRepository.save(pratica);
            };
        }
        System.out.println("acquired correlati");

        praticaRepository.save(pratica);
        return pratica.getId();

    }


    public List<PraticaSummaryDTO> getAllSummaries(){
        List<PraticaSummaryDTO> praticaSummaryDTOS = new ArrayList<>();
        praticaRepository.findAll().forEach(item->{
            List<String> nomi = new ArrayList<>();
            List<String> cognomi = new ArrayList<>();
            List<String> codF = new ArrayList<>();
            String diagnosi = new String("");

            long numProvvedimenti = 0l;
            long numProvvedimentiCorrelati =0l;
            nomi.add(item.getPaziente().getNome());
            cognomi.add(item.getPaziente().getCognome());
            codF.add(item.getPaziente().getCodF());
            if(item.getMorsicatura()!= null){
                diagnosi = "Morsicatura";
                if(item.getMorsicatura().getProprietario() != null) {
                    nomi.add(item.getMorsicatura().getProprietario().getNome());
                    cognomi.add(item.getMorsicatura().getProprietario().getCognome());
                    codF.add(item.getMorsicatura().getProprietario().getCodF());
                }
            }
            if(!item.getDiagnosi().isEmpty()){
                for(Diagnosi diag : item.getDiagnosi()){
                    if(diagnosi.compareTo("")!=0)
                        diagnosi = diagnosi + " - "+ diag.getTipo();
                    else
                        diagnosi = diag.getTipo();
                };
            }
            if(item.getContatti()!=null && !item.getContatti().isEmpty()){
                for(Contatto cont : item.getContatti()){
                    nomi.add(cont.getPersona().getNome());
                    cognomi.add(cont.getPersona().getCognome());
                    codF.add(cont.getPersona().getCodF());
                }
            }
            if(item.getProvvedimenti()!= null && !item.getProvvedimenti().isEmpty()){
                numProvvedimenti = item.getProvvedimenti()
                        .stream()
                        .filter(prov->{
                            System.out.println(prov.getPersona().getCodF());
                            return prov.getPersona().getCodF() == item.getPaziente().getCodF();
                        })
                        .count();
                numProvvedimentiCorrelati = item.getProvvedimenti().size() - numProvvedimenti;
            }
            PraticaSummaryDTO praticaSummaryDTO = new PraticaSummaryDTO();
            praticaSummaryDTO.setId(item.getId());
            praticaSummaryDTO.setDataSegnalazione(item.getDataSegnalazione());
            praticaSummaryDTO.setDataRicezione(item.getDataRicezione());
            praticaSummaryDTO.setStruttDenunciante(item.getStruttDenunciante());
            praticaSummaryDTO.setOperatore(item.getOperatore());
            praticaSummaryDTO.setStato(item.getStato());
            praticaSummaryDTO.setDiagnosi(diagnosi);
            praticaSummaryDTO.setNomi(nomi);
            praticaSummaryDTO.setCognomi(cognomi);
            praticaSummaryDTO.setCodF(codF);
            praticaSummaryDTO.setNumProvvedimenti(numProvvedimenti);
            praticaSummaryDTO.setNumProvvedimentiCorrelati(numProvvedimentiCorrelati);
            praticaSummaryDTOS.add(praticaSummaryDTO);
        });
        return praticaSummaryDTOS;
    }
    /*
    @Override
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

        System.out.println("saved provvedimenti");


        List<Provvedimento> oldProvvedimenti = oldPratica.get().getProvvedimenti();
        List<Provvedimento> newProvvedimenti = new ArrayList<Provvedimento>();

        maxiPraticaDTO.getProvvedimenti().forEach(provvedimentoDTO->{
            newProvvedimenti.add(modelMapper.map(provvedimentoDTO,Provvedimento.class));
        })

        List<Persona> newPersone = new ArrayList<Persona>();
        List<Contatto> newContatti = new ArrayList<Contatto>();
        maxiPraticaDTO.getCorrelati().forEach(correlatoDTO->{
            newPersona.add(modelMapper.map(correlatoDTO.getPaziente(),Persona.class));
            newContatti.add(modelMapper.map(correlatoDTO.getContatto(),Contatto.class));

        })

        oldProvvedimenti.forEach(provvedimento->{
            boolean flag = false;
            for(Provvedimento newProv : newProvvedimenti){
                if(newProv.getId()==provvedimento.getId()){
                    flag = true;
                }

            }
            if(!flag){
                provvedimento.getPratica().removeProvvedimento(provvedimento);
                provvedimento.getPersona().removeProvvedimento(provvedimento);
                personaRepository.save(provvedimento.getPersona());
                praticaRepository.save(provvedimento.getPratica());
                provvedimentoRepository.delete(provvedimento);
            }
        })

        newProvvedimenti.forEach(provvedimento->{
            boolean flag = false;
            for(Provvedimento oldProv : oldProvvedimenti){
                if(oldProv.getId() == provvedimento.getId())
                {
                    flag = true;
                }
            }
            if(!flag){
                provvedimento.setPratica(pratica)
            }
        })
    } */


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
