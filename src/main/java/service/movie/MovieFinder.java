package service.movie;

import model.Movie;

import java.io.IOException;
import java.util.List;

public interface MovieFinder {

    Movie getMovieDetailsForMovieId(String id);

    List<Movie> getMovieListForSearchedPhrase(String phrase) throws IOException, InterruptedException;

}
