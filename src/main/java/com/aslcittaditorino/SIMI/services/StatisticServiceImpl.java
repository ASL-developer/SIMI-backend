package com.aslcittaditorino.SIMI.services;

import com.aslcittaditorino.SIMI.entities.Session;
import com.aslcittaditorino.SIMI.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    SessionRepository sessionRepo;


    @Override
    public boolean checkSingleLogin(String token) {
        if (Optional.ofNullable(token).isEmpty())
            return true;
        else return sessionRepo.getOne(getCurrentUsername()).getToken().equals(token);
    }

    @Override
    public void sessionSaver(String token, String currentUsername) {
        if (!sessionRepo.existsById(currentUsername)) {
            Session session = new Session();
            session.setUsername(currentUsername);
            session.setToken(token);
            sessionRepo.save(session);
        } else {
            Session tmp = sessionRepo.getOne(currentUsername);
            tmp.setToken(token);
            sessionRepo.save(tmp);
        }
    }

    private String getCurrentUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
