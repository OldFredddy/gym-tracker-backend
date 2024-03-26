package com.gym.gym_tracker_server.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gym.gym_tracker_server.entities.TrainingDay;

import java.lang.reflect.Type;
import java.util.List;

public class JsonMapper {

    public static List<TrainingDay> mapJsonToTrainigDays(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<TrainingDay>>(){}.getType();
        return gson.fromJson(json, listType);
    }

}