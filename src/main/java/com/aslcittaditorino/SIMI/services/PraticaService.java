package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PraticaService {
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public MaxiPraticaDTO getMaxiPratica(Long id);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public long addMaxiPratica(MaxiPraticaDTO maxiPraticaDTO);

    //public long updateMaxiPratica(MaxiPraticaDTO maxiPraticaDTO);


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<PraticaSummaryDTO> getAllSummaries();
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public long getNextPraticaId();
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public long addPratica(PraticaDTO pratica);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public PraticaDTO getPratica(Long id);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<String> getAllHospitals();
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public long updatePratica(PraticaDTO pratica);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public boolean addContattoToPratica(Long idPratica,Long idContatto);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<ContattoDTO> getContattiForPratica(Long idPratica);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public boolean addPazienteToPratica(Long idPratica,String codf);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public PersonaDTO getPazienteForPratica(Long idPratica);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public boolean addDiagnosiToPratica(Long idPratica, Long idDiagnosi);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<DiagnosiDTO> getAllDiagnosiForPratica(Long idPratica);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public boolean addMorsicaturaToPratica(Long idPratica,Long idMorsicatura);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public MorsicaturaDTO getMorsicaturaForPratica(Long idPratica);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public boolean addProvvedimentoToPratica(Long idPratica,Long idProvvedimento);
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<ProvvedimentoDTO> getProvvedimentiForPratica (Long idPratica);
}
