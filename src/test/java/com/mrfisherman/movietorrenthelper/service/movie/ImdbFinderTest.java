package com.mrfisherman.movietorrenthelper.service.movie;

import com.mrfisherman.movietorrenthelper.model.Movie;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImdbFinderTest {

    @Test
    void getInstanceShouldReturnSameInstanceEveryTime() {
        //given
        ImdbFinder imdbFinder1 = ImdbFinder.getInstance();
        ImdbFinder imdbFinder2 = ImdbFinder.getInstance();
        //then
        assertEquals(imdbFinder1, imdbFinder2);
    }

    @Test
    void getMovieListForSearchedPhraseShouldReturnAtLeastOneInceptionMovie() {
        //given
        String phrase = "Inception";
        //when
        List<Movie> movieList = ImdbFinder.getInstance().findMovies(phrase);
        //then
        assertTrue(movieList.size() >= 1);
    }

    @Test
    void getMovieListForSearchedPhraseResultShouldContainAllElements() {
        //given
        String phrase = "Inception";
        //when
        List<Movie> movieList = ImdbFinder.getInstance().findMovies(phrase);
        Movie firstResult = movieList.get(0);
        //then
        assertAll(() -> {
            assertNotNull(firstResult.getId());
            assertNotNull(firstResult.getTitle());
            assertNotNull(firstResult.getImage());
        });
    }
}