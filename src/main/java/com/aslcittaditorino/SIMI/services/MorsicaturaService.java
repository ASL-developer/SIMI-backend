package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.MorsicaturaDTO;
import org.springframework.stereotype.Service;

public interface MorsicaturaService {

    public MorsicaturaDTO getMorsicatura(long id);

    public long addMorsicatura(MorsicaturaDTO morsicaturaDTO);

    public long updateMorsicatura(MorsicaturaDTO morsicaturaDTO);
}
