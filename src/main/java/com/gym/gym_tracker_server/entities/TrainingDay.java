package com.gym.gym_tracker_server.entities;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Data
public class TrainingDay {
    private List<Exercise> exercises;
    private String title;

}
