package service.torrent;

import model.Torrent;
import model.TorrentStats;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TorrentHelperTest {

    @Test
    void countTorrentHealthShouldReturn100PercentFor0Leechers() {
        //given
        int seeds = 1;
        int leechers = 0;
        Torrent torrent = new Torrent("title", "magnetLink", new TorrentStats(seeds, leechers));
        //when
        int result = TorrentHelper.countTorrentHealth(torrent);
        //then
        assertEquals(100, result);
    }

    @Test
    void countTorrentHealthShouldReturn0PercentFor0Seeds() {
        //given
        int seeds = 0;
        int leechers = 10;
        Torrent torrent = new Torrent("title", "magnetLink", new TorrentStats(seeds, leechers));
        //when
        int result = TorrentHelper.countTorrentHealth(torrent);
        //then
        assertEquals(0, result);
    }

    @Test
    void countTorrentHealthShouldReturnNegativeValueWhenLeechersGraterThanSeedsAndSeedsGraterThan0() {
        //given
        int seeds = 2;
        int leechers = 10;
        Torrent torrent = new Torrent("title", "magnetLink", new TorrentStats(seeds, leechers));
        //when
        int result = TorrentHelper.countTorrentHealth(torrent);
        //then
        assertTrue(result < 0);
    }

    @Test
    void countTorrentHealthShouldThrowIllegalArgumentExceptionWhenSeedsOrLeechersLessThan0() {
        //given
        int seeds = -10;
        int leechers = -10;
        Torrent torrent = new Torrent("title", "magnetLink", new TorrentStats(seeds, leechers));
        //then
        assertThrows(IllegalArgumentException.class, () -> TorrentHelper.countTorrentHealth(torrent));
    }
}