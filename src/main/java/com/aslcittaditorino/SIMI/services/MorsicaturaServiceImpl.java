package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.MorsicaturaDTO;
import com.aslcittaditorino.SIMI.entities.Morsicatura;
import com.aslcittaditorino.SIMI.exceptions.MorsicaturaServiceException;
import com.aslcittaditorino.SIMI.repositories.MorsicaturaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MorsicaturaServiceImpl implements MorsicaturaService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    MorsicaturaRepository morsicaturaRepository;

    @Override
    public MorsicaturaDTO getMorsicatura(long id) {
        Optional<Morsicatura> morsicatura = morsicaturaRepository.findById(id);
        if(morsicatura.isEmpty()) throw new MorsicaturaServiceException("Morsicatura not found");

        return modelMapper.map(morsicatura.get(),MorsicaturaDTO.class);
    }

    @Override
    @Transactional
    public long addMorsicatura(MorsicaturaDTO morsicaturaDTO) {
        if(Optional.ofNullable(morsicaturaDTO).isEmpty()) throw new MorsicaturaServiceException("Morsicatura is empty");
        morsicaturaDTO.setId(null);
        return morsicaturaRepository.save(modelMapper.map(morsicaturaDTO,Morsicatura.class)).getId();
    }

    @Override
    @Transactional
    public long updateMorsicatura(MorsicaturaDTO morsicaturaDTO) {
        Optional<Morsicatura> morsicatura = morsicaturaRepository.findById(morsicaturaDTO.getId());
        if(morsicatura.isEmpty()) throw new MorsicaturaServiceException("Morsicatura not found");
        morsicatura.get().setDataMorsicatura(morsicaturaDTO.getDataMorsicatura());
        morsicatura.get().setAnimale(morsicaturaDTO.getAnimale());
        morsicatura.get().setSedeLesione(morsicaturaDTO.getSedeLesione());
        morsicatura.get().setGradoEsposizione(morsicaturaDTO.getGradoEsposizione());
        morsicatura.get().setLuogoEvento(morsicaturaDTO.getLuogoEvento());
        morsicatura.get().setRischio(morsicaturaDTO.getRischio());
        morsicatura.get().setDataVaccRab(morsicaturaDTO.getDataVaccRab());
        morsicatura.get().setDataVaccTet(morsicaturaDTO.getDataVaccTet());
        morsicatura.get().setImmRab(morsicaturaDTO.getImmRab());
        morsicatura.get().setImmTet(morsicaturaDTO.getImmTet());
        morsicatura.get().setNote(morsicaturaDTO.getNote());

        return morsicaturaRepository.save(morsicatura.get()).getId();
    }
}
