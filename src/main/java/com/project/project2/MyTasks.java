package com.project.project2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.entity.ContentType;
import org.junit.BeforeClass;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.http.entity.StringEntity;




@Component
public class MyTasks {

    int id = 0;
    ObjectMapper mapper = new ObjectMapper();
    File file = new File("./inventory.txt");

    // run every 5 seconds
    @Scheduled(fixedDelay = 5000)
    public void addVehicle() throws IOException, URISyntaxException, InterruptedException {


        URL url = new URL("http://localhost:8080/addVehicle");

        List<String> models = new ArrayList<>();
        models.add("Ford Fusion");
        models.add("BMW m4");
        models.add("Honda Accord");
        models.add("Kia Stinger");

        id++;
        Random r = new Random();
        // creates random int for the size of the list
        int randString = r.nextInt(models.size());
        // assigns a string to a random index in list
        String makeModel = models.get(randString);
        // creates random year from 1986-2016
        int year = r.nextInt(2016) + 1986;
        double retail = r.nextInt(45000) + 15000;

        Vehicle vehicle = new Vehicle(id, makeModel, year, retail);
        URI uri = new URI("http://localhost:8080/addVehicle");
        String requestBody = "{\"makeModel\":\"" + vehicle.getMakeModel() + "\",\"year\":" + vehicle.getYear() + ",\"retailPrice\":" + vehicle.getRetailPrice() + "}";
        //ObjectMapper objectMapper = new ObjectMapper();
        //String request = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();


        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::statusCode).thenAccept(System.out::println);
    }


    @Scheduled(fixedDelay = 10000)
    public void deleteVehicle() throws IOException, InterruptedException {


        Random r = new Random();
        int id = r.nextInt(100);
        HttpClient clients = HttpClient.newHttpClient();
        HttpRequest requests = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/getVehicle/" + id)).build();

        HttpResponse<String> responses = clients.send(requests, HttpResponse.BodyHandlers.ofString());
        if(responses.equals("500")){
            System.out.println("Unable to find a vehicle to delete at id:" + id);
            return;
        }else{
            String deleteEndpoint = "http://localHost:8080/deleteVehicle/" + id;

            var request = HttpRequest.newBuilder().uri(URI.create(deleteEndpoint)).header("Content-Type", "application/json").DELETE().build();

            var client = HttpClient.newHttpClient();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());


            System.out.println(response.statusCode());
            System.out.println(response.body());
        }

    }

    @Scheduled(fixedDelay = 20000)
    public void updateVehicle() throws IOException, URISyntaxException, InterruptedException {
        //URI uri = new URI("http://localhost:8080/updateVehicle");
        Random r = new Random();
        int id = r.nextInt(20);
        String makeModel = "Honda Accord";
        int year = 2017;
        double retail = 25000;
        Vehicle updated = new Vehicle(id, makeModel, year, retail);

        String requestBody = "{\"id\":" + id +",\"makeModel\":\"" + updated.getMakeModel() + "\",\"year\":" + updated.getYear() + ",\"retailPrice\":" + updated.getRetailPrice() + "}";

        URI uri = new URI("http://localhost:8080/updateVehicle");

        Gson g = new Gson();
        String finals = g.toJson(updated);
        System.out.println(finals);
        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/updateVehicle")).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(finals)).build();
        var client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
    //prints list of 10 vehicles to console
    @Scheduled(cron = "0 0 * * * *")
    public void latestVehiclesReport() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/getLatestVehicles")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
          System.out.println(response.body());
    }

}

