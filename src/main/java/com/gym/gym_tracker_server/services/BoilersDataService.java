package com.gym.gym_tracker_server.services;

import com.gym.gym_tracker_server.utils.JsonMapper;
import com.gym.gym_tracker_server.entities.Boiler;
import com.google.gson.Gson;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BoilersDataService {
    @Getter
    private List<Boiler> boilers = new ArrayList<>();
    @Getter
    private TemperatureCorrections corrections = new TemperatureCorrections();
    private final int BOILERS_COUNT = 14;
    private final WebClient webClient;
    private final HttpClient httpClient;
    private final ConnectionProvider connectionProvider;
    private final AtomicBoolean isUpdateInProgress = new AtomicBoolean(false);
    private final AtomicBoolean isUpdateInProgress2 = new AtomicBoolean(false);
    private static final String IP = "http://46.61.160.6:4567";//85.175.232.186:4567
    public BoilersDataService(WebClient.Builder webClientBuilder) {
        for (int i = 0; i < BOILERS_COUNT; i++) {
            Boiler boiler = new Boiler();
            boiler.setId(i);
            boiler.setIsOk(1,1);
            boiler.setPPod("0.4");
            boiler.setTAlarm("65");
            boiler.setTPlan("70");
            boiler.setTPod("68");
            boiler.setImageResId(2);
            boiler.setTUlica("0");
            boiler.setPPodHighFixed("-1");
            boiler.setPPodLowFixed("-1");
            boiler.setTPodFixed("-1");
            boilers.add(boiler);
        }
        connectionProvider = ConnectionProvider.builder("custom")
                .maxConnections(500)
                .maxIdleTime(Duration.ofMinutes(30))
                .maxLifeTime(Duration.ofHours(1))
                .build();

        this.httpClient = HttpClient.create(connectionProvider);
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(IP)
                .build();
        // webClient = webClientBuilder.baseUrl(IP).build();;
    }
    @PreDestroy
    public void onDestroy() {
        this.connectionProvider.dispose();
        System.out.println("clean BoilersDataService");
    }
    public boolean forFirstStart = true;
    @Scheduled(fixedRate = 3000)
    public void fetchBoilerData() {
        if (isUpdateInProgress.compareAndSet(false, true)) {
            getBoilersFromClient()
                    .subscribe(
                            boilersList -> {
                                for (int i = 0; i < boilersList.size(); i++) {
                                    this.boilers.get(i).setTPod(boilersList.get(i).getTPod());
                                    this.boilers.get(i).setPPod(boilersList.get(i).getPPod());
                                    this.boilers.get(i).setTUlica(boilersList.get(i).getTUlica());
                                    this.boilers.get(i).setTPodFixed(boilersList.get(i).getTPodFixed());
                                    this.boilers.get(i).setPPodHighFixed(boilersList.get(i).getPPodHighFixed());
                                    this.boilers.get(i).setPPodLowFixed(boilersList.get(i).getPPodLowFixed());
                                    this.boilers.get(i).setTPlan(boilersList.get(i).getTPlan());
                                    this.boilers.get(i).setImageResId(boilersList.get(i).getImageResId());
                                    this.boilers.get(i).setId(boilersList.get(i).getId());
                                    this.boilers.get(i).setTAlarm(boilersList.get(i).getTAlarm());

                                }
                                if (forFirstStart){
                                    for (Boiler boiler : this.boilers) {
                                        boiler.setIsOk(1, 2);
                                    }
                                    forFirstStart=false;
                                }

                                isUpdateInProgress.set(false);
                            },
                            error -> {
                                System.err.println("Ошибка при получении данных: " + error.getMessage());
                                isUpdateInProgress.set(false);
                            }
                    );

        }
        if (isUpdateInProgress2.compareAndSet(false, true)) {
            getCorrectionsTpodFromClient()
                    .subscribe(
                            temperatureCorrections -> {
                                this.corrections = temperatureCorrections;
                                isUpdateInProgress2.set(false);
                            },
                            error -> {
                                System.err.println("Ошибка при получении данных: " + error.getMessage());
                                isUpdateInProgress2.set(false);
                            }
                    );
        }
    }
    public void setCorrectionsTpod(String[] corrections1){
        this.corrections.setCorrectionTpod(corrections1);
        setCorrectionsTpodToClient(corrections.getCorrectionTpod())
                .subscribe(
                        response -> System.out.println("Ответ сервера: " + response),
                        error -> System.err.println("Ошибка: " + error.getMessage())
                );
    }
    public void setCorrectionsTAlarm(String[] corrections1){
        this.corrections.setTAlarmCorrectionFromUsers(corrections1);
        setTAlarmToClient(corrections.getTAlarmCorrectionFromUsers())
                .subscribe(
                        response -> System.out.println("Ответ сервера: " + response),
                        error -> System.err.println("Ошибка: " + error.getMessage())
        );
    }
    public Mono<List<Boiler>> getBoilersFromClient() {
        return webClient.get()
                .uri("/getclientparams")
                .retrieve()
                .bodyToMono(String.class)
                .map(JsonMapper::mapJsonToBoilers);
    }

    public Mono<String[]> setCorrectionsTpodToClient(String[] correctionsTpod) {
        Gson gson = new Gson();
        String json = gson.toJson(correctionsTpod);
        return webClient.post()
                .uri("/setclientparamstPod")
                .body(Mono.just(json), String.class)
                .retrieve()
                .bodyToMono(String[].class);
    }
    public Mono<TemperatureCorrections> getCorrectionsTpodFromClient() {
        return webClient.get()
                .uri("/getcorrect")
                .retrieve()
                .bodyToMono(String.class)
                .map(JsonMapper::mapJsonToCorrections);
    }
    public Mono<String[]> setTAlarmToClient(String[] correctionsTAlarm) {// Метод для отправки корректировок tAlarm TODO тут проверить и дописать, тут быть повнимательнее
        Gson gson = new Gson();
        String json = gson.toJson(correctionsTAlarm);
        return webClient.post()
                .uri("/setclientparamstAlarm")
                .body(Mono.just(correctionsTAlarm), String[].class)
                .retrieve()
                .bodyToMono(String[].class);
    }
}