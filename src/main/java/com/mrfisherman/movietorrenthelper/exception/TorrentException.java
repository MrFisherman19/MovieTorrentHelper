package com.mrfisherman.movietorrenthelper.exception;

public class TorrentException extends RuntimeException {

    public TorrentException() { }

    public TorrentException(String message) {
        super(message);
    }
}