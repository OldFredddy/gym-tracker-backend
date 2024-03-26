package com.gym.gym_tracker_server;

import com.gym.gym_tracker_server.entities.TrainingDay;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RESTController {
    private final RepositoryService dataServise;

    @Autowired
    public RESTController(RepositoryService dataServise) {
        this.dataServise = dataServise;
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/get_days/{id}")
    public ResponseEntity<String> getParams(@PathVariable String id) {
        try {
            List<TrainingDay> trainingDays = dataServise.getTrainingDaysFromPG(id);
            Gson gson = new Gson();
            String json = gson.toJson(trainingDays);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/avaryreset")
    public String setAvaryReset() {
        try {
            for (int i = 0; i <boilersDataService.getBoilers().size() ; i++) {
                boilersDataService.getBoilers().get(i).setIsOk(1,boilersDataService.getBoilers().get(i).getVersion()+1);//0-waiting 1 - good 2 - error
            }
            telegramService.resetError();
            return "Success!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/setPumpStationParams")
    public String setPumpStationParams(@RequestBody PumpStation pumpStation) {
        try {
            pumpStationDataService.refreshData(pumpStation);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
