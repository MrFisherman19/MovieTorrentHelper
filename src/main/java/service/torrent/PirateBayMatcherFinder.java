package service.torrent;

import exception.NoTorrentFoundException;
import model.Torrent;
import model.TorrentStats;

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

    private static PirateBayMatcherFinder instance;

    private static final String SEARCH_URL = "https://tpb.party/search/";

    private static final String MAGNET_REGEX = "magnet:\\?xt=(.*?)(?=\")";
    private static final String TITLE_INSIDE_MAGNET_REGEX = "(?<=&dn=)(.*?)(?=&tr)";
    private static final String SEEDS_LECHERS_PATTERN = "(?<=<td align=\"right\">)([0-9]*?)(?=</td>)";

    private PirateBayMatcherFinder() { }

    public static PirateBayMatcherFinder getInstance() {
        if (instance == null) {
            instance = new PirateBayMatcherFinder();
        }
        return instance;
    }

    @Override
    public List<Torrent> findTorrentsForTitle(String phrase) throws IOException, InterruptedException, NoTorrentFoundException {

        String htmlPage = foundTorrents(phrase).body();

        List<Torrent> foundTorrents = new ArrayList<>();

        scrapTorrentInfo(htmlPage, foundTorrents);
        scrapSeedsAndLeechers(htmlPage, foundTorrents);

        if (foundTorrents.isEmpty()) {
            throw new NoTorrentFoundException();
        }

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
        while(seedsLeechersMatcher.find()) {
            seeds = Integer.parseInt(seedsLeechersMatcher.group());
            lecheers = 0;
            if (seedsLeechersMatcher.find()) {
                lecheers = Integer.parseInt(seedsLeechersMatcher.group());
            }
            foundTorrents.get(counter).setTorrentStats(new TorrentStats(seeds, lecheers));
            counter++;
        }
    }

    private HttpResponse<String> foundTorrents(String title) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SEARCH_URL + title.replaceAll("\\s+","%20")))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
