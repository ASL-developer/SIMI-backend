package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PraticaService {

    public List<MaxiPraticaDTO> getAllPratiche();

    public long getNextPraticaId();

    public long addPratica(PraticaDTO pratica);

    public PraticaDTO getPratica(Long id);

    public long updatePratica(PraticaDTO pratica);

    public boolean addContattoToPratica(Long idPratica,Long idContatto);

    public List<ContattoDTO> getContattiForPratica(Long idPratica);

    public boolean addPazienteToPratica(Long idPratica,String codf);

    public PersonaDTO getPazienteForPratica(Long idPratica);

    public boolean addDiagnosiToPratica(Long idPratica, Long idDiagnosi);

    public List<DiagnosiDTO> getAllDiagnosiForPratica(Long idPratica);

    public boolean addMorsicaturaToPratica(Long idPratica,Long idMorsicatura);

    public MorsicaturaDTO getMorsicaturaForPratica(Long idPratica);

    public boolean addProvvedimentoToPratica(Long idPratica,Long idProvvedimento);

    public List<ProvvedimentoDTO> getProvvedimentiForPratica (Long idPratica);
}
