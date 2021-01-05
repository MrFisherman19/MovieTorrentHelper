import exception.NoMovieFoundException;
import exception.NoTorrentFoundException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Movie;
import model.Torrent;
import sample.Components.MovieListItem;
import sample.Components.TorrentListItem;
import service.movie.ImdbFinder;
import service.movie.MovieFinder;
import service.torrent.PirateBayJSoupFinder;
import service.torrent.TorrentFinder;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ProgressBar progressBar;

    @FXML
    private TextField searcher;

    @FXML
    private Button searchButton;

    @FXML
    private VBox movieListVBox;

    @FXML
    private VBox torrentListVBox;

    private static MovieFinder movieFinder;
    private static TorrentFinder torrentFinder;

    public Controller() {
        movieFinder = ImdbFinder.getInstance();
        torrentFinder = new PirateBayJSoupFinder();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        progressBar.setVisible(false);
    }

    public void findMovies(ActionEvent actionEvent) {
        String searchedPhrase = searcher.getText();
        progressBar.setVisible(true);
        if (movieListVBox != null) {
            clearListVBox(movieListVBox);
        }
        if (actionEvent.getSource() == searchButton) {
            if (searcher != null && !searchedPhrase.isEmpty()) {
                new Thread(() -> {
                    try {
                        List<Movie> movieList = movieFinder.getMovieListForSearchedPhrase(searchedPhrase);

                        Platform.runLater(() ->
                                FXCollections.observableArrayList(movieList)
                                        .forEach(x -> movieListVBox.getChildren().add(new MovieListItem(x, event -> loadTorrentLinks(x.getTitle())))));
                    } catch (NoMovieFoundException e) {
                        Platform.runLater(() -> showInformMessage(String.format("There are no movies found for phrase: %s", searchedPhrase)
                                , MessageType.INFORMATION));
                    } catch (Exception e) {
                        Platform.runLater(() -> showInformMessage(e.getMessage(), MessageType.ERROR));
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(() -> progressBar.setVisible(false));
                    }
                }).start();
            }
        }
    }

    public void loadTorrentLinks(String movieTitle) {
        progressBar.setVisible(true);
        clearListVBox(torrentListVBox);
        new Thread(() -> {
            try {
                List<Torrent> torrentList = torrentFinder.findTorrentsForTitle(movieTitle);

                Platform.runLater(() ->
                    torrentList.forEach(torrent ->
                            torrentListVBox.getChildren().addAll(new TorrentListItem(torrent))));
            } catch (NoTorrentFoundException e) {
                Platform.runLater(() -> showInformMessage(String.format("There are no torrent found for title: %s", movieTitle),
                        MessageType.INFORMATION));
            } catch (Exception e) {
                Platform.runLater(() -> showInformMessage(e.getMessage(), MessageType.ERROR));
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> progressBar.setVisible(false));
            }
        }).start();
    }

    private static void clearListVBox(VBox list) {
        list.getChildren().clear();
    }

    public void showInformMessage(String message, MessageType messageType) {
        Alert errorAlert = getAlertType(messageType);
        errorAlert.setTitle(messageType.name());
        errorAlert.setHeaderText(messageType.name());
        errorAlert.setContentText(message);
        errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        errorAlert.showAndWait();
    }

    private Alert getAlertType(final MessageType messageType) {
        Alert errorAlert;
        switch (messageType) {
            case ERROR:
                errorAlert = new Alert(Alert.AlertType.ERROR);
                break;
            case WARNING:
                errorAlert = new Alert(Alert.AlertType.WARNING);
                break;
            default:
                errorAlert = new Alert(Alert.AlertType.INFORMATION);
                break;
        }
        return errorAlert;
    }
}
