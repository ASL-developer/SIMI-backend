package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PraticaService {

    public long addPratica(PraticaDTO pratica);

    public PraticaDTO getPratica(Long id);

    public long addContattoToPratica(Long idPratica,Long idContatto);

    public List<ContattoDTO> getContattiForPratica(Long idPratica);

    public long addPazienteToPratica(Long idPratica,String codf);

    public PersonaDTO getPazienteForPratica(Long idPratica);

    public long addDiagnosiToPratica(Long idPratica, Long idDiagnosi);

    public List<DiagnosiDTO> getAllDiagnosiForPratica(Long idPratica);

    public long addMorsicaturaToPratica(Long idPratica,Long idMorsicatura);

    public MorsicaturaDTO getMorsicaturaForPratica(Long idPratica);

    public long addProvvedimentoToPratica(Long idPratica,Long idProvvedimento);

    public List<ProvvedimentoDTO> getProvvedimentiForPratica (Long idPratica);
}
