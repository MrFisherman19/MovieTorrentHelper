package model;

public class Torrent {

    private String title;
    private String torrentMagnetUrl;
    private TorrentStats torrentStats;

    public Torrent(String title, String torrentMagnetUrl) {
        this.title = title;
        this.torrentMagnetUrl = torrentMagnetUrl;
    }

    public Torrent(String title, String torrentMagnetUrl, TorrentStats torrentStats) {
        this.title = title;
        this.torrentMagnetUrl = torrentMagnetUrl;
        this.torrentStats = torrentStats;
    }

    public String getTorrentMagnetUrl() {
        return torrentMagnetUrl;
    }

    public String getTitle() {
        return title;
    }

    public TorrentStats getTorrentStats() {
        return torrentStats;
    }

    public void setTorrentStats(TorrentStats torrentStats) {
        this.torrentStats = torrentStats;
    }
}
