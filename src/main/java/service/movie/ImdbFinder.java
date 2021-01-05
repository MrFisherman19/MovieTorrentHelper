package service.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import exception.NoMovieFoundException;
import model.Movie;
import service.util.JsonParserUtil;
import service.util.PropertiesHelper;

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

    private static ImdbFinder instance;
    private static Properties properties;
    private static ObjectMapper objectMapper;

    private ImdbFinder() { }

    public static ImdbFinder getInstance() {
        if (instance == null) {
            instance = new ImdbFinder();
            objectMapper = new ObjectMapper();
            properties = PropertiesHelper.loadPropertiesFileForFilename("ImdbHttpInfo.properties");
        }
        return instance;
    }

    @Override
    public Movie getMovieDetailsForMovieId(String movieId) {
        return new Movie();
    }

    @Override
    public List<Movie> getMovieListForSearchedPhrase(String phrase) throws IOException, InterruptedException, NoMovieFoundException {
        List<Movie> movies = mapTitlesToMovieList(foundMovies(phrase).body());
        if (movies == null || movies.isEmpty()) {
            throw new NoMovieFoundException();
        }
        return movies;
    }

    private List<Movie> mapTitlesToMovieList(String response) throws IOException {
        Optional<JsonArray> jsonArray = Optional.ofNullable(
                JsonParserUtil.parseStringToJsonObject(response).getAsJsonArray("titles"));

        List<Movie> movieList = new ArrayList<>();
        if (jsonArray.isPresent()) {
            movieList = objectMapper.readValue(jsonArray.get().toString(), new TypeReference<>() {});
        }

        return movieList;
    }

    private HttpResponse<String> foundMovies(String title) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(properties.getProperty("search-api-url") + formatTitle(title)))
                .header("x-rapidapi-key", properties.getProperty("x-rapidapi-key"))
                .header("x-rapidapi-host", properties.getProperty("x-rapidapi-host"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String formatTitle(String title) {
        return title.replaceAll("\\s+", "%20")
                .replaceAll("[^a-zA-Z\\d\\s%]", "");
    }
}
