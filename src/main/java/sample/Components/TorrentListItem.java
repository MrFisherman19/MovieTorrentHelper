package sample.Components;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import model.Torrent;
import service.torrent.TorrentHelper;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TorrentListItem extends HBox {
    private static final double ITEM_PREF_HEIGHT = 30d;

    public TorrentListItem(Torrent torrent) {
        this.setHeight(ITEM_PREF_HEIGHT);
        this.setPadding(new Insets(2,0,2,2));
        Background defaultBackground = getBackground();
        this.setOnMouseEntered(event -> setBackground(
                new Background(new BackgroundFill(Color.rgb(128, 128, 255), null, null))));
        this.setOnMouseExited(event -> setBackground(defaultBackground));
        init(torrent);
    }

    private void init(Torrent torrent) {
        Label label = new Label(torrent.getTitle());
        label.setPrefHeight(ITEM_PREF_HEIGHT);
        label.setMinWidth(415d);
        label.setMaxWidth(415d);

        Label seeds = new Label(torrent.getTorrentStats().getSeeds() + "");
        seeds.setId("seed");
        seeds.setPrefWidth(60d);

        Label leechers = new Label(torrent.getTorrentStats().getLeechers() + "");
        leechers.setId("leecher");
        leechers.setPrefWidth(60d);

        Label torrentHealth = new Label(String.format("%d%%", TorrentHelper.countTorrentHealth(torrent)));
        torrentHealth.setId("health");
        torrentHealth.setPrefWidth(60d);

        Button button = new Button("Open!");
        button.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI(torrent.getTorrentMagnetUrl()));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
        this.getChildren().addAll(label, seeds, leechers, torrentHealth, button);
    }
}