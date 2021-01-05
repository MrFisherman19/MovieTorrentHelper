package com.mrfisherman.movietorrenthelper.sample.Components;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import com.mrfisherman.movietorrenthelper.model.Movie;

public class MovieListItem extends HBox {

    private static final double HEIGHT_OF_ITEM = 100d;

    public MovieListItem(Movie movie, EventHandler<MouseEvent> mouseEventEventHandler) {
        this.setHeight(HEIGHT_OF_ITEM);
        Background defaultBackground = getBackground();
        this.setOnMouseEntered(event -> setBackground(
                new Background(new BackgroundFill(Color.valueOf("#1e74c6"), null, null))));
        this.setOnMouseExited(event -> setBackground(defaultBackground));
        this.setOnMouseClicked(mouseEventEventHandler);
        init(movie);
    }

    private void init(Movie movie) {
        ImageView imageView = getImageView(movie);

        VBox vBox = getTitleIdVBox(movie);

        this.getChildren().addAll(imageView, vBox);
    }

    private VBox getTitleIdVBox(Movie movie) {
        VBox vBox = new VBox();
        vBox.setPrefHeight(HEIGHT_OF_ITEM);

        Label titleLabel = new Label();
        titleLabel.setPrefSize(200d, 80d);
        titleLabel.setFont(new Font("Calibri", 22));
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setText(movie.getTitle());

        Label idLabel = new Label();
        idLabel.setPrefSize(200d, 20d);
        idLabel.setFont(new Font("Calibri", 12));
        idLabel.setAlignment(Pos.CENTER);
        idLabel.setText(movie.getId());

        vBox.getChildren().addAll(titleLabel, idLabel);
        return vBox;
    }

    private ImageView getImageView(Movie movie) {
        ImageView imageView = new ImageView(new Image(movie.getImage()));
        imageView.setPreserveRatio(false);
        imageView.setPickOnBounds(false);
        imageView.setFitHeight(HEIGHT_OF_ITEM);
        imageView.setFitWidth(80d);
        return imageView;
    }

}
