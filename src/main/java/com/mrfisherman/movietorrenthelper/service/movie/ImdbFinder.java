package com.mrfisherman.movietorrenthelper.service.movie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.mrfisherman.movietorrenthelper.exception.MovieException;
import com.mrfisherman.movietorrenthelper.model.Movie;
import com.mrfisherman.movietorrenthelper.service.util.JsonParserUtil;
import com.mrfisherman.movietorrenthelper.service.util.PropertiesHelper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ImdbFinder implements MovieFinder {

    private static volatile ImdbFinder instance;
    private final Properties properties;
    private final ObjectMapper objectMapper;

    private ImdbFinder() {
        objectMapper = new ObjectMapper();
        properties = PropertiesHelper.loadPropertiesFileForFilename("ImdbHttpInfo.properties");
    }

    public static ImdbFinder getInstance() {
        if (instance == null) {
            synchronized (ImdbFinder.class) {
                instance = new ImdbFinder();
            }
        }
        return instance;
    }

    @Override
    public List<Movie> findMovies(String phrase) {
        return mapTitlesToMovieList(foundMovies(phrase).body());
    }

    private List<Movie> mapTitlesToMovieList(String response) throws MovieException {
        Optional<JsonArray> jsonArray = Optional.ofNullable(
                JsonParserUtil.parseStringToJsonObject(response).getAsJsonArray("titles"));

        List<Movie> movieList = new ArrayList<>();
        if (jsonArray.isPresent()) {
            try {
                movieList = objectMapper.readValue(jsonArray.get().toString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new MovieException(e.getMessage());
            }
        }

        return movieList;
    }

    private HttpResponse<String> foundMovies(String title) throws MovieException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(properties.getProperty("search-api-url") + formatTitle(title)))
                .header("x-rapidapi-key", properties.getProperty("x-rapidapi-key"))
                .header("x-rapidapi-host", properties.getProperty("x-rapidapi-host"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new MovieException(e.getMessage());
        }
        return response;
    }

    private String formatTitle(String title) {
        return title.replaceAll("\\s+", "%20")
                .replaceAll("[^a-zA-Z\\d\\s%]", "");
    }
}
