package com.mrfisherman.movietorrenthelper.service.torrent;

class PirateBayJSoupFinderTest extends TorrentFinderTest<PirateBayJSoupFinder> {

    @Override
    protected PirateBayJSoupFinder createInstance() {
        return new PirateBayJSoupFinder();
    }
}