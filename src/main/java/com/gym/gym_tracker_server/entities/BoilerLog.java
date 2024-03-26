package com.gym.gym_tracker_server.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class BoilerLog {
    @Id
    private String id;
    private Boiler boiler;
    private List<String> errorDescs;
    private long timestamp;
}
