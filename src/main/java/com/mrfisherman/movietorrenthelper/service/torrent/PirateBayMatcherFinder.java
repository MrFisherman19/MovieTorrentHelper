package com.mrfisherman.movietorrenthelper.service.torrent;

import com.mrfisherman.movietorrenthelper.exception.MovieException;
import com.mrfisherman.movietorrenthelper.exception.TorrentException;
import com.mrfisherman.movietorrenthelper.model.Torrent;
import com.mrfisherman.movietorrenthelper.model.TorrentStats;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PirateBayMatcherFinder implements TorrentFinder {

    private static volatile PirateBayMatcherFinder instance;

    private static final String SEARCH_URL = "https://tpb.party/search/";

    private static final String MAGNET_REGEX = "magnet:\\?xt=(.*?)(?=\")";
    private static final String TITLE_INSIDE_MAGNET_REGEX = "(?<=&dn=)(.*?)(?=&tr)";
    private static final String SEEDS_LECHERS_PATTERN = "(?<=<td align=\"right\">)([0-9]*?)(?=</td>)";

    private PirateBayMatcherFinder() {
    }

    public static PirateBayMatcherFinder getInstance() {
        if (instance == null) {
            synchronized (PirateBayMatcherFinder.class) {
                instance = new PirateBayMatcherFinder();
            }
        }
        return instance;
    }

    @Override
    public List<Torrent> findTorrentsForTitle(String phrase) {

        String htmlPage = foundTorrents(phrase).body();

        List<Torrent> foundTorrents = new ArrayList<>();

        scrapTorrentInfo(htmlPage, foundTorrents);
        scrapSeedsAndLeechers(htmlPage, foundTorrents);

        return foundTorrents;
    }

    private void scrapTorrentInfo(String htmlPage, List<Torrent> foundTorrents) {
        Matcher magnetMatcher = Pattern.compile(MAGNET_REGEX).matcher(htmlPage);
        Pattern titlePattern = Pattern.compile(TITLE_INSIDE_MAGNET_REGEX);
        Matcher titleMatcher;
        String title;
        String magnet;
        while (magnetMatcher.find()) {
            magnet = magnetMatcher.group();
            titleMatcher = titlePattern.matcher(magnet);
            if (titleMatcher.find()) {
                title = titleMatcher.group();
                foundTorrents.add(new Torrent(title, magnet));
            }
        }
    }

    private void scrapSeedsAndLeechers(String htmlPage, List<Torrent> foundTorrents) {
        final Matcher seedsLeechersMatcher = Pattern.compile(SEEDS_LECHERS_PATTERN).matcher(htmlPage);
        int seeds, lecheers;
        int counter = 0;
        while (seedsLeechersMatcher.find()) {
            seeds = Integer.parseInt(seedsLeechersMatcher.group());
            lecheers = 0;
            if (seedsLeechersMatcher.find()) {
                lecheers = Integer.parseInt(seedsLeechersMatcher.group());
            }
            foundTorrents.get(counter).setTorrentStats(new TorrentStats(seeds, lecheers));
            counter++;
        }
    }

    private HttpResponse<String> foundTorrents(String title) throws TorrentException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SEARCH_URL + title.replaceAll("\\s+", "%20")))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new TorrentException(e.getMessage());
        }
        return response;
    }
}
