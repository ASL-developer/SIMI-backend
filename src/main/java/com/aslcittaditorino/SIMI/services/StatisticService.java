package com.aslcittaditorino.SIMI.services;

public interface StatisticService {

    boolean checkSingleLogin(String token);

    void sessionSaver(String token, String currentUsername);

}
