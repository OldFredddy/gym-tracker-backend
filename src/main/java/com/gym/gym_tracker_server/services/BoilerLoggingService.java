package com.gym.gym_tracker_server.services;
import com.gym.gym_tracker_server.entities.BoilerLog;
import com.gym.gym_tracker_server.repository.BoilerLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BoilerLoggingService {

    private final BoilerLogRepository logRepository;

    @Autowired
    public BoilerLoggingService(BoilerLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void logBoilerStatus(Boiler boiler, String errorDesc) {
        BoilerLog log = logRepository.findByBoiler(boiler);
        if (log == null) {
            log = new BoilerLog();
            log.setBoiler(boiler);
            log.setErrorDescs(new ArrayList<>());
            log.setTimestamp(System.currentTimeMillis());
        }
        log.getErrorDescs().add(errorDesc);
        log.setTimestamp(System.currentTimeMillis());
        logRepository.save(log);
    }
}
