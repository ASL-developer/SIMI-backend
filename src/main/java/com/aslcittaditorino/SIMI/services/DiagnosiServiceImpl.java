package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.DiagnosiDTO;
import com.aslcittaditorino.SIMI.entities.Diagnosi;
import com.aslcittaditorino.SIMI.exceptions.DiagnosiServiceException;
import com.aslcittaditorino.SIMI.repositories.DiagnosiRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class DiagnosiServiceImpl implements DiagnosiService{
    @Autowired
    DiagnosiRepository diagnosiRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public DiagnosiDTO getDiagnosi(Long id) {
        Optional<Diagnosi> diagnosi = diagnosiRepository.findById(id);
        if(diagnosi.isEmpty()) throw new DiagnosiServiceException("Diagnosi not found");

        return modelMapper.map(diagnosi.get(),DiagnosiDTO.class);
    }

    @Override
    @Transactional
    public long addDiagnosi(DiagnosiDTO diagnosiDTO) {
        Diagnosi diagnosi = modelMapper.map(diagnosiDTO,Diagnosi.class);
        diagnosi.setId(null);
        return diagnosiRepository.save(diagnosi).getId();
    }

    @Override
    @Transactional
    public long updateDiagnosi(DiagnosiDTO diagnosiDTO) {
        Optional<Diagnosi> diagnosi = diagnosiRepository.findById(diagnosiDTO.getId());
        if(diagnosi.isEmpty()) throw new DiagnosiServiceException("Diagnosi not found");

        diagnosi.get().setTipo(diagnosiDTO.getTipo());
        diagnosi.get().setClasse(diagnosiDTO.getClasse());
        diagnosi.get().setRicovero(diagnosiDTO.getRicovero());
        diagnosi.get().setDottore(diagnosiDTO.getDottore());
        diagnosi.get().setDataPrimaDiagnosi(diagnosiDTO.getDataPrimaDiagnosi());
        diagnosi.get().setRisolta(diagnosiDTO.isRisolta());
        diagnosi.get().setDataRisoluzione(diagnosiDTO.getDataRisoluzione());

        return diagnosiRepository.save(diagnosi.get()).getId();
    }

    @Override
    @Transactional
    public long deleteDiagnosi(Long id) {
        Optional<Diagnosi> diagnosi = diagnosiRepository.findById(id);
        if(diagnosi.isEmpty()) throw  new DiagnosiServiceException("Diagnosi not found");

        diagnosi.get().getPratica().getDiagnosi().remove(diagnosi.get());
        diagnosiRepository.delete(diagnosi.get());
        return diagnosi.get().getId();
    }
}
