package com.gym.gym_tracker_server.entities;

import java.util.ArrayList;
import java.util.List;

public class TrainingDay  {
    private List<Exercise> exercises;
    private String title;

    public TrainingDay(List<Exercise> exercises, String title) {
        this.exercises = exercises;
        this.title = title;
    }

    public TrainingDay() {
        exercises = new ArrayList<>();
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addExercise(Exercise exercise) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercises.add(exercise);
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}
