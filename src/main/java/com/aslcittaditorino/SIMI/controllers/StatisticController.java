/*package com.aslcittaditorino.SIMI.controllers;

import com.aslcittaditorino.SIMI.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docs")
public class StatisticController {

    @Autowired
    StatisticService service;

    @PostMapping("/sessionSaver")
    public void sessionSaver(@RequestBody SessionDTO session) {
        service.sessionSaver(session.getToken(), session.getUsername());
    }

    @GetMapping("/version")
    public String appVersion() {
        return "0.1";
    }


}
*/