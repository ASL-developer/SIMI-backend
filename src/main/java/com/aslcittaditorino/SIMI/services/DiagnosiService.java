package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.DTOs.DiagnosiDTO;
import org.springframework.stereotype.Service;

public interface DiagnosiService {

    public DiagnosiDTO getDiagnosi(Long id);

    public long addDiagnosi(DiagnosiDTO diagnosi);

    public long updateDiagnosi(DiagnosiDTO diagnosi);

    public long deleteDiagnosi(Long id);


}
