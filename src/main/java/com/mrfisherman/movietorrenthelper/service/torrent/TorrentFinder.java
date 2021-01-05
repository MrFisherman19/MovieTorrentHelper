package com.mrfisherman.movietorrenthelper.service.torrent;

import com.mrfisherman.movietorrenthelper.exception.TorrentException;
import com.mrfisherman.movietorrenthelper.model.Torrent;

import java.util.List;

public interface TorrentFinder {

    List<Torrent> findTorrentsForTitle(String phrase) throws TorrentException;

}
