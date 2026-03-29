package com.eu.habbo.gui.controller;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PlayersTabController {

    private final ObservableList<PlayerEntry> players = FXCollections.observableArrayList();
    private TextField searchField;
    private Label countLabel;
    private Timeline refreshTimeline;

    public Tab createTab() {
        Tab tab = new Tab("Players");
        tab.setClosable(false);

        TableView<PlayerEntry> table = new TableView<>();
        table.setPlaceholder(new Label("No players online"));

        TableColumn<PlayerEntry, Number> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d -> new SimpleIntegerProperty(d.getValue().id));
        idCol.setPrefWidth(60);

        TableColumn<PlayerEntry, String> nameCol = new TableColumn<>("Username");
        nameCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().username));
        nameCol.setPrefWidth(150);

        TableColumn<PlayerEntry, String> mottoCol = new TableColumn<>("Motto");
        mottoCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().motto));
        mottoCol.setPrefWidth(180);

        TableColumn<PlayerEntry, String> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().room));
        roomCol.setPrefWidth(180);

        TableColumn<PlayerEntry, String> ipCol = new TableColumn<>("IP");
        ipCol.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().ip));
        ipCol.setPrefWidth(120);

        TableColumn<PlayerEntry, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(220);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button kickBtn = new Button("Kick");
            private final Button muteBtn = new Button("Mute");
            private final Button alertBtn = new Button("Alert");
            private final HBox box = new HBox(5, kickBtn, muteBtn, alertBtn);

            {
                kickBtn.getStyleClass().add("danger");
                kickBtn.setStyle("-fx-font-size: 11px; -fx-padding: 2 8;");
                muteBtn.setStyle("-fx-font-size: 11px; -fx-padding: 2 8;");
                alertBtn.setStyle("-fx-font-size: 11px; -fx-padding: 2 8;");

                kickBtn.setOnAction(e -> {
                    PlayerEntry entry = getTableView().getItems().get(getIndex());
                    kickPlayer(entry.id);
                });
                muteBtn.setOnAction(e -> {
                    PlayerEntry entry = getTableView().getItems().get(getIndex());
                    mutePlayer(entry.id);
                });
                alertBtn.setOnAction(e -> {
                    PlayerEntry entry = getTableView().getItems().get(getIndex());
                    alertPlayer(entry.id);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        table.getColumns().addAll(idCol, nameCol, mottoCol, roomCol, ipCol, actionsCol);

        // Search / filter
        searchField = new TextField();
        searchField.setPromptText("Search player...");

        FilteredList<PlayerEntry> filtered = new FilteredList<>(players, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filtered.setPredicate(entry -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return entry.username.toLowerCase().contains(lower)
                        || entry.room.toLowerCase().contains(lower)
                        || String.valueOf(entry.id).contains(lower);
            });
        });
        table.setItems(filtered);

        countLabel = new Label("Online: 0");
        countLabel.setStyle("-fx-font-weight: bold;");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshPlayers());

        HBox toolbar = new HBox(10, searchField, countLabel, refreshBtn);
        toolbar.setPadding(new Insets(5));
        toolbar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox content = new VBox(5, toolbar, table);
        content.setPadding(new Insets(5));
        VBox.setVgrow(table, Priority.ALWAYS);

        tab.setContent(content);

        // Auto refresh every 5 seconds
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> refreshPlayers()));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();

        tab.setOnClosed(e -> refreshTimeline.stop());

        return tab;
    }

    private void refreshPlayers() {
        if (!Emulator.isReady) return;

        try {
            ConcurrentHashMap<Integer, Habbo> onlineHabbos =
                    Emulator.getGameEnvironment().getHabboManager().getOnlineHabbos();

            var list = new java.util.ArrayList<PlayerEntry>();
            for (Habbo habbo : onlineHabbos.values()) {
                HabboInfo info = habbo.getHabboInfo();
                if (info == null) continue;

                String roomName = "";
                Room currentRoom = info.getCurrentRoom();
                if (currentRoom != null) {
                    roomName = currentRoom.getName();
                }

                list.add(new PlayerEntry(
                        info.getId(),
                        info.getUsername(),
                        info.getMotto(),
                        roomName,
                        info.getIpLogin()
                ));
            }

            Platform.runLater(() -> {
                players.setAll(list);
                countLabel.setText("Online: " + list.size());
            });
        } catch (Exception ignored) {
        }
    }

    private void kickPlayer(int userId) {
        if (!Emulator.isReady) return;
        Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
        if (habbo != null) {
            habbo.disconnect();
        }
    }

    private void mutePlayer(int userId) {
        if (!Emulator.isReady) return;
        Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
        if (habbo != null) {
            habbo.mute(300, false);
        }
    }

    private void alertPlayer(int userId) {
        if (!Emulator.isReady) return;

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Alert Player");
        dialog.setHeaderText("Send alert message");
        dialog.setContentText("Message:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(message -> {
            if (!message.isBlank()) {
                Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);
                if (habbo != null) {
                    habbo.alert(message);
                }
            }
        });
    }

    private record PlayerEntry(int id, String username, String motto, String room, String ip) {}
}
