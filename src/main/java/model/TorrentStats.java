package model;

public class TorrentStats {

    private Integer seeds;
    private Integer leechers;

    public TorrentStats(Integer seeds, Integer leechers) {
        this.seeds = seeds;
        this.leechers = leechers;
    }

    public Integer getSeeds() {
        return seeds;
    }

    public Integer getLeechers() {
        return leechers;
    }

    @Override
    public String toString() {
        return "seeds=" + seeds +
                ", leechers=" + leechers;
    }
}
