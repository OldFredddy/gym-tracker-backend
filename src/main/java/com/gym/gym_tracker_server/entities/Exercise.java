package com.gym.gym_tracker_server.entities;

import lombok.Data;

@Data
public class Exercise  {
    private String title;
    private int sets;
    private int maxWeight;
    private int reps;


}
