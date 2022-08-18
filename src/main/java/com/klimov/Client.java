package com.klimov;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class Client {

    private static final String sensorName = "test_sensor";
    private static final String measurementsUrl = "http://localhost:8080/measurements/add";
    private static final String registrationUrl = "http://localhost:8080/sensors/registration";

    public static void main(String[] args) {

        registerSensor();

        Random r = new Random();
        for (int i=0; i<1000; i++){
            postMeasurement(
                    r.nextDouble() * 20 + 12,
                    r.nextBoolean()
            );
        }


    }

    private static void registerSensor(){
        makePostRequestJSONData(registrationUrl, new SensorDTO(sensorName));
    }

    private static void postMeasurement(double value, boolean raining){
        makePostRequestJSONData(measurementsUrl,
                new MeasurementDTO(
                        value,
                        raining,
                        new SensorDTO(
                                sensorName
                        )
                ));
    }

    private static void makePostRequestJSONData(String url, Object data){
        final RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Object> request = new HttpEntity<>(data);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Success!");
        } catch (HttpClientErrorException e) {
            System.out.println("Error!");
            System.out.println(e.getMessage());
        }
    }
}
