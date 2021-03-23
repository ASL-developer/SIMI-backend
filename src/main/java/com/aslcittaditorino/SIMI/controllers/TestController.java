package com.aslcittaditorino.SIMI.controllers;

import com.aslcittaditorino.SIMI.DTOs.*;
import com.aslcittaditorino.SIMI.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/API/test")
public class TestController {

    @Autowired
    PraticaService praticaService;

    @Autowired
    PersonaService personaService;

    @Autowired
    DiagnosiService diagnosiService;

    @Autowired
    MorsicaturaService morsicaturaService;

    @Autowired
    ProvvedimentoService provvedimentoService;

    @Autowired
    ContattoService contattoService;


    @GetMapping("/getAllPratiche")
    public List<MaxiPraticaDTO> getAllPratiche(){
        System.out.println(praticaService.getAllPratiche());
        return praticaService.getAllPratiche();
    }

    @PostMapping("/addMaxiPratica")
    public Long addMaxiPratica(@RequestBody MaxiPraticaDTO maxiPraticaDTO){return praticaService.addMaxiPratica(maxiPraticaDTO);}

    @GetMapping("/getNextPraticaId")
    public Long getNextPraticaId(){
        return praticaService.getNextPraticaId();
    }

    @PostMapping("/addPersona")
    public String addPersona(@RequestBody PersonaDTO personaDTO){
        System.out.println("REQUEST:add persona");
        return personaService.addPersona(personaDTO);
    }

    @PostMapping("/addPratica")
    public Long addPratica(@RequestBody PraticaDTO praticaDTO){
        System.out.println("REQUEST:add pratica ");
        return praticaService.addPratica(praticaDTO);
    }

    @PutMapping("/addPazienteToPratica")
    public boolean addPazienteToPratica(@RequestParam(name="codf")String codf,@RequestParam(name="pratica") Long pratica){
        return praticaService.addPazienteToPratica(pratica,codf);
    }

    @PostMapping("/addDiagnosi")
    public Long addDiagnosi(@RequestBody DiagnosiDTO diagnosiDTO){
        System.out.println("REQUEST:add diagnosi ");
        return diagnosiService.addDiagnosi(diagnosiDTO);
    }

    @PutMapping("/addDiagnosiToPratica")
    public boolean addDiagnosiToPratica(@RequestParam(name="diagnosi")Long diagnosi,@RequestParam(name="pratica") Long pratica){
        return praticaService.addDiagnosiToPratica(pratica,diagnosi);
    }

    @PostMapping("/addContatto")
    public Long addContatto(@RequestBody ContattoDTO contattoDTO){
        System.out.println("REQUEST:add contatto ");
        return contattoService.addContatto(contattoDTO);
    }

    @PutMapping("/addContattoToPratica")
    public boolean addContattoToPratica(@RequestParam(name="contatto")Long contatto,@RequestParam(name="pratica") Long pratica){
        return praticaService.addContattoToPratica(pratica,contatto);
    }

    @PostMapping("/addProvvedimento")
    public Long addProvvedimento(@RequestBody ProvvedimentoDTO provvedimentoDTO){
        System.out.println("REQUEST:add provvedimento ");
        return provvedimentoService.addProvvedimento(provvedimentoDTO);
    }

    @PutMapping("/addProvvedimentoToPratica")
    public boolean addProvvedimentoToPratica(@RequestParam(name="provvedimento")Long provvedimento,@RequestParam(name="pratica") Long pratica){
        return praticaService.addProvvedimentoToPratica(pratica,provvedimento);
    }

    @PostMapping("/addMorsicatura")
    public Long addMorsicatura(@RequestBody MorsicaturaDTO morsicaturaDTO){
        System.out.println("REQUEST:add morsicatura ");
        return morsicaturaService.addMorsicatura(morsicaturaDTO);
    }

    @PutMapping("/addMorsicaturaToPratica")
    public boolean addMorsicaturaToPratica(@RequestParam(name="morsicatura")Long morsicatura,@RequestParam(name="pratica") Long pratica){
        return praticaService.addMorsicaturaToPratica(pratica,morsicatura);
    }

}
