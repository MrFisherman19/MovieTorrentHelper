package service.torrent;

import exception.NoTorrentFoundException;
import model.Torrent;

import java.io.IOException;
import java.util.List;

public interface TorrentFinder {

    List<Torrent> findTorrentsForTitle(String phrase) throws IOException, InterruptedException, NoTorrentFoundException;

}
