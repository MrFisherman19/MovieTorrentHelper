package com.mrfisherman.movietorrenthelper.service.movie;

import com.mrfisherman.movietorrenthelper.exception.MovieException;
import com.mrfisherman.movietorrenthelper.model.Movie;

import java.util.List;

public interface MovieFinder {

    List<Movie> findMovies(String phrase) throws MovieException;

}
