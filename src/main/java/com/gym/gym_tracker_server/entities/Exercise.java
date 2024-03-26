package com.gym.gym_tracker_server.entities;


public class Exercise  {
    private String title;
    private int sets;
    private int maxWeight;
    private int reps;

    public Exercise(String title, int sets, int maxWeight, int reps) {
        this.title = title;
        this.sets = sets;
        this.maxWeight = maxWeight;
        this.reps = reps;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
