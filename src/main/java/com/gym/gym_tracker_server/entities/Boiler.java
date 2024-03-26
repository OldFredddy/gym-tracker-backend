package com.gym.gym_tracker_server.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Boiler {
    private int isOk;  // 0-waiting, 1 - good, 2 - error
    @Setter private String tPod;
    @Setter private String pPod;
    @Setter private String tUlica;
    @Setter private String tPlan;
    @Setter private String tAlarm;
    @Setter private int imageResId;
    @Setter private String pPodLowFixed;
    @Setter private String pPodHighFixed;
    @Setter private String tPodFixed;
    @Setter private Integer id;
    @Setter private long version;
    @Setter private long lastUpdated;
    public void setIsOk(int isOk, long newVersion) {
        if (newVersion > this.version) {
            this.isOk = isOk;
            this.version = newVersion;
        }
    }
}