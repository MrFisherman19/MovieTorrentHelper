package service.torrent;

public class PirateBayMatcherFinderTest extends TorrentFinderTest<PirateBayMatcherFinder> {

    @Override
    protected PirateBayMatcherFinder createInstance() {
        return PirateBayMatcherFinder.getInstance();
    }
}
