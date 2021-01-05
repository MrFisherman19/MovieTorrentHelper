package com.mrfisherman.movietorrenthelper.service.torrent;

import com.mrfisherman.movietorrenthelper.exception.TorrentException;
import com.mrfisherman.movietorrenthelper.model.Torrent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class TorrentFinderTest<T extends TorrentFinder> {

    private T torrentFinder;

    protected abstract T createInstance();

    //Inception phrase have almost always about 500 results
    String inceptionMovieTitle = "Inception";

    @BeforeAll
    public void setUp() {
        torrentFinder = createInstance();
    }

    @Test
    void findTorrentsForTitleShouldReturnAtLeast15InceptionMoviesTorrents() {
        //when
        List<Torrent> torrents = torrentFinder.findTorrentsForTitle(inceptionMovieTitle);
        //then
        assertTrue(torrents.size() >= 15);
    }

    @Test
    void findTorrentsForTitleFirstResultShouldHaveMoreOrEqualSeedsNumberThanLastResult() {
        //when
        List<Torrent> torrents = torrentFinder.findTorrentsForTitle(inceptionMovieTitle);
        int firstResultSeeds = torrents.get(0).getTorrentStats().getSeeds();
        int lastResultSeeds = torrents.get(torrents.size() - 1).getTorrentStats().getSeeds();
        //then
        assertTrue(firstResultSeeds >= lastResultSeeds);
    }

    @Test
    void findTorrentsForTitleFirstResultShouldHaveMoreOrEqualLeechersNumberThanLastResult() {
        //when
        List<Torrent> torrents = torrentFinder.findTorrentsForTitle(inceptionMovieTitle);
        int firstResultLeechers = torrents.get(0).getTorrentStats().getLeechers();
        int lastResultLeechers = torrents.get(torrents.size() - 1).getTorrentStats().getLeechers();
        //then
        assertTrue(firstResultLeechers >= lastResultLeechers);
    }

    @Test
    void findTorrentsForTitleShouldThrowNoTorrentExceptionWhenThereAreNoResults() {
        //given
        String brokenTitle = ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;";
        //then
        assertThrows(TorrentException.class, () -> torrentFinder.findTorrentsForTitle(brokenTitle));
    }

    @Test
    void findTorrentsForTitleResultShouldContainsAllOfNeededFields() {
        //given
        List<Torrent> torrents = torrentFinder.findTorrentsForTitle(inceptionMovieTitle);
        Torrent firstResult = torrents.get(0);
        //then
        assertAll(
                () -> {
                    assertNotNull(firstResult.getTitle());
                    assertNotNull(firstResult.getTorrentMagnetUrl());
                    assertNotNull(firstResult.getTorrentStats().getSeeds());
                    assertNotNull(firstResult.getTorrentStats().getLeechers());
                });
    }

}
