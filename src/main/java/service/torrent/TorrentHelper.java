package service.torrent;

import model.Torrent;

import java.util.function.BinaryOperator;

public final class TorrentHelper {

    private final static BinaryOperator<Integer> countPercent = (a, b) -> (int) (100 - (double) a / b * 100);

    public static int countTorrentHealth(final Torrent torrent) {
        int seeds = torrent.getTorrentStats().getSeeds();
        int leechers = torrent.getTorrentStats().getLeechers();

        if (seeds < 0 || leechers < 0) {
            throw new IllegalArgumentException();
        }

        double healthPercentage = 0.0;
        if (seeds != 0) {
            healthPercentage = countPercent.apply(leechers, seeds);
        }
        return (int) healthPercentage;
    }
}
