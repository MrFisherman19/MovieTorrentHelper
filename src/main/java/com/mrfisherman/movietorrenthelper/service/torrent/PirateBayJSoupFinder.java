package com.mrfisherman.movietorrenthelper.service.torrent;

import com.mrfisherman.movietorrenthelper.exception.TorrentException;
import com.mrfisherman.movietorrenthelper.model.Torrent;
import com.mrfisherman.movietorrenthelper.model.TorrentStats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PirateBayJSoupFinder implements TorrentFinder {

    private static final String SEARCH_URL = "https://tpb.party/search/";

    @Override
    public List<Torrent> findTorrentsForTitle(final String title) throws TorrentException {

        Document document;
        try {
            document = Jsoup.connect(SEARCH_URL + title.replaceAll("\\s+", "%20")).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new TorrentException(e.getMessage());
        }

        if (document != null) {
            List<String> titles = scrapTitles(document);
            List<String> magnets = scrapMagnetLinks(document);
            List<TorrentStats> torrentStats = scrapTorrentStats(document);
            return mergeInsideTorrentList(titles, magnets, torrentStats);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Torrent> mergeInsideTorrentList(final List<String> titles, final List<String> magnets,
                                                 final List<TorrentStats> torrentStats) {
        return IntStream.range(0, titles.size())
                .mapToObj(i -> new Torrent(titles.get(i), magnets.get(i), torrentStats.get(i)))
                .collect(Collectors.toList());
    }

    private List<String> scrapTitles(final Document document) {
        final Elements elements = document.select(".detLink");
        return elements.parallelStream()
                .map(Element::text)
                .collect(Collectors.toList());
    }

    private List<String> scrapMagnetLinks(final Document document) {
        final Elements elements = document.select("td > a[href~=magnet:]");
        return elements.parallelStream()
                .map(e -> e.attr("href"))
                .collect(Collectors.toList());
    }

    private List<TorrentStats> scrapTorrentStats(final Document document) {
        final Elements elements3 = document.select("table[id~=searchResult] tr td[align~=right]");
        return IntStream.iterate(0, i -> i + 2)
                .limit(elements3.size() / 2)
                .mapToObj(i -> new TorrentStats(Integer.parseInt(elements3.get(i).text()),
                        Integer.parseInt(elements3.get(i + 1).text())))
                .collect(Collectors.toList());
    }
}
