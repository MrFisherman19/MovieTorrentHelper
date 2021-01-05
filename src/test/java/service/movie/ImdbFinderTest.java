package service.movie;

import exception.NoMovieFoundException;
import model.Movie;
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
    void getMovieListForSearchedPhraseShouldReturnAtLeastOneInceptionMovie() throws IOException, InterruptedException {
        //given
        String phrase = "Inception";
        //when
        List<Movie> movieList = ImdbFinder.getInstance().getMovieListForSearchedPhrase(phrase);
        //then
        assertTrue(movieList.size() >= 1);
    }

    @Test
    void getMovieListForSearchedPhraseResultShouldContainAllElements() throws IOException, InterruptedException {
        //given
        String phrase = "Inception";
        //when
        List<Movie> movieList = ImdbFinder.getInstance().getMovieListForSearchedPhrase(phrase);
        Movie firstResult = movieList.get(0);
        //then
        assertAll(() -> {
            assertNotNull(firstResult.getId());
            assertNotNull(firstResult.getTitle());
            assertNotNull(firstResult.getImage());
        });
    }

    @Test
    void getMovieListForSearchedPhraseShouldThrowNoMovieFoundExceptionWhen0Results() {
        //given
        String brokenPhrase = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
        //then
        assertThrows(NoMovieFoundException.class, () -> ImdbFinder.getInstance().getMovieListForSearchedPhrase(brokenPhrase));
    }
}