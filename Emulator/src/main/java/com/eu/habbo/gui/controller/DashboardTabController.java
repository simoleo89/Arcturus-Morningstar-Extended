package com.eu.habbo.gui.controller;

import com.eu.habbo.Emulator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class DashboardTabController {

    private Label usersOnlineLabel;
    private Label activeRoomsLabel;
    private Label uptimeLabel;
    private Label ramUsageLabel;
    private Label threadsLabel;
    private Label totalMemoryLabel;
    private Label cpuCoresLabel;
    private Label serverStatusLabel;
    private Circle statusCircle;

    private Button startButton;
    private Button stopButton;
    private Button restartButton;

    private Timeline refreshTimeline;
    private volatile boolean serverRunning = false;

    public Tab createTab() {
        Tab tab = new Tab("Dashboard");
        tab.setClosable(false);

        Label title = new Label("Arcturus Morningstar - Server Dashboard");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));

        // Status indicator
        statusCircle = new Circle(8);
        statusCircle.setFill(Color.web("#f39c12"));
        serverStatusLabel = new Label("Starting...");
        serverStatusLabel.getStyleClass().add("status-starting");
        HBox statusBox = new HBox(8, statusCircle, serverStatusLabel);
        statusBox.setAlignment(Pos.CENTER_LEFT);

        // Control buttons
        startButton = new Button("Start");
        startButton.getStyleClass().add("success");
        startButton.setDisable(true);
        startButton.setOnAction(e -> startServer());

        stopButton = new Button("Stop");
        stopButton.getStyleClass().add("danger");
        stopButton.setDisable(true);
        stopButton.setOnAction(e -> stopServer());

        restartButton = new Button("Restart");
        restartButton.getStyleClass().add("warning");
        restartButton.setDisable(true);
        restartButton.setOnAction(e -> restartServer());

        HBox controlBox = new HBox(10, startButton, stopButton, restartButton);
        controlBox.setAlignment(Pos.CENTER_LEFT);

        HBox topBar = new HBox(30, statusBox, controlBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(5, 0, 5, 0));

        // Stats grid
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(10, 0, 0, 0));

        usersOnlineLabel = createValueLabel("0");
        activeRoomsLabel = createValueLabel("0");
        uptimeLabel = createValueLabel("0s");
        ramUsageLabel = createValueLabel("0 MB");
        threadsLabel = createValueLabel("0");
        totalMemoryLabel = createValueLabel("0 MB");
        cpuCoresLabel = createValueLabel(String.valueOf(Runtime.getRuntime().availableProcessors()));

        int row = 0;
        statsGrid.add(createHeaderLabel("Users Online"), 0, row);
        statsGrid.add(usersOnlineLabel, 1, row++);
        statsGrid.add(createHeaderLabel("Active Rooms"), 0, row);
        statsGrid.add(activeRoomsLabel, 1, row++);
        statsGrid.add(createHeaderLabel("Uptime"), 0, row);
        statsGrid.add(uptimeLabel, 1, row++);
        statsGrid.add(createHeaderLabel("RAM Usage"), 0, row);
        statsGrid.add(ramUsageLabel, 1, row++);
        statsGrid.add(createHeaderLabel("Total Memory"), 0, row);
        statsGrid.add(totalMemoryLabel, 1, row++);
        statsGrid.add(createHeaderLabel("Active Threads"), 0, row);
        statsGrid.add(threadsLabel, 1, row++);
        statsGrid.add(createHeaderLabel("CPU Cores"), 0, row);
        statsGrid.add(cpuCoresLabel, 1, row++);

        VBox content = new VBox(10, title, topBar, new Separator(), statsGrid);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_LEFT);

        tab.setContent(content);
        startRefreshTimer();
        tab.setOnClosed(e -> stopRefreshTimer());

        return tab;
    }

    private void startServer() {
        startButton.setDisable(true);
        updateStatus("Starting...", "#f39c12", "status-starting");

        Thread.startVirtualThread(() -> {
            try {
                Emulator.startServer();
            } catch (Exception e) {
                Platform.runLater(() -> updateStatus("Error", "#e74c3c", "status-offline"));
            }
        });
    }

    private void stopServer() {
        stopButton.setDisable(true);
        restartButton.setDisable(true);
        updateStatus("Stopping...", "#f39c12", "status-starting");

        Thread.startVirtualThread(() -> {
            try {
                var disposeMethod = Emulator.class.getDeclaredMethod("dispose");
                disposeMethod.setAccessible(true);
                disposeMethod.invoke(null);

                serverRunning = false;
                Platform.runLater(() -> {
                    updateStatus("Offline", "#e74c3c", "status-offline");
                    startButton.setDisable(false);
                    stopButton.setDisable(true);
                    restartButton.setDisable(true);
                });
            } catch (Exception e) {
                Platform.runLater(() -> updateStatus("Error", "#e74c3c", "status-offline"));
            }
        });
    }

    private void restartServer() {
        restartButton.setDisable(true);
        stopButton.setDisable(true);
        updateStatus("Restarting...", "#f39c12", "status-starting");

        Thread.startVirtualThread(() -> {
            try {
                var disposeMethod = Emulator.class.getDeclaredMethod("dispose");
                disposeMethod.setAccessible(true);
                disposeMethod.invoke(null);

                serverRunning = false;
                Thread.sleep(2000);

                Emulator.startServer();
            } catch (Exception e) {
                Platform.runLater(() -> updateStatus("Error", "#e74c3c", "status-offline"));
            }
        });
    }

    private void updateStatus(String text, String color, String styleClass) {
        Platform.runLater(() -> {
            statusCircle.setFill(Color.web(color));
            serverStatusLabel.setText(text);
            serverStatusLabel.getStyleClass().removeAll("status-online", "status-offline", "status-starting");
            serverStatusLabel.getStyleClass().add(styleClass);
        });
    }

    private void startRefreshTimer() {
        refreshTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> refreshStats()));
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void stopRefreshTimer() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }

    private void refreshStats() {
        if (!Emulator.isReady) {
            if (!serverRunning) return;
            return;
        }

        if (!serverRunning) {
            serverRunning = true;
            Platform.runLater(() -> {
                updateStatus("Online", "#2ecc71", "status-online");
                startButton.setDisable(true);
                stopButton.setDisable(false);
                restartButton.setDisable(false);
            });
        }

        try {
            Runtime rt = Emulator.getRuntime();
            long usedMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
            long totalMB = rt.maxMemory() / (1024 * 1024);
            int onlineUsers = Emulator.getGameEnvironment().getHabboManager().getOnlineCount();
            int activeRooms = Emulator.getGameEnvironment().getRoomManager().getActiveRooms().size();
            int activeThreads = Thread.activeCount();
            String uptime = formatUptime(Emulator.getOnlineTime());

            Platform.runLater(() -> {
                usersOnlineLabel.setText(String.valueOf(onlineUsers));
                activeRoomsLabel.setText(String.valueOf(activeRooms));
                uptimeLabel.setText(uptime);
                ramUsageLabel.setText(usedMB + " / " + totalMB + " MB");
                threadsLabel.setText(String.valueOf(activeThreads));
                totalMemoryLabel.setText(totalMB + " MB");
            });
        } catch (Exception ignored) {
        }
    }

    private String formatUptime(int seconds) {
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (days * 24);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long secs = seconds - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        sb.append(secs).append("s");
        return sb.toString();
    }

    private Label createHeaderLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        label.setMinWidth(150);
        return label;
    }

    private Label createValueLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 14));
        return label;
    }
}
