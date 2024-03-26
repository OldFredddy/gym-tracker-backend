package com.gym.gym_tracker_server.repository;

import com.gym.gym_tracker_server.entities.Boiler;
import com.gym.gym_tracker_server.entities.BoilerLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoilerLogRepository extends MongoRepository<BoilerLog, String> {
    BoilerLog findByBoiler(Boiler boiler);
}
