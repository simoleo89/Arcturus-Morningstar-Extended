package com.eu.habbo.gui.controller;

import com.eu.habbo.Emulator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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

    private Timeline refreshTimeline;

    public Tab createTab() {
        Tab tab = new Tab("Dashboard");
        tab.setClosable(false);

        Label title = new Label("Arcturus Morningstar - Server Dashboard");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        serverStatusLabel = createValueLabel("Starting...");

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(20));

        usersOnlineLabel = createValueLabel("0");
        activeRoomsLabel = createValueLabel("0");
        uptimeLabel = createValueLabel("0s");
        ramUsageLabel = createValueLabel("0 MB");
        threadsLabel = createValueLabel("0");
        totalMemoryLabel = createValueLabel("0 MB");
        cpuCoresLabel = createValueLabel(String.valueOf(Runtime.getRuntime().availableProcessors()));

        int row = 0;
        statsGrid.add(createHeaderLabel("Server Status"), 0, row);
        statsGrid.add(serverStatusLabel, 1, row++);

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

        VBox content = new VBox(15, title, statsGrid);
        content.setPadding(new Insets(15));
        content.setAlignment(Pos.TOP_LEFT);

        tab.setContent(content);

        startRefreshTimer();

        tab.setOnClosed(e -> stopRefreshTimer());

        return tab;
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
            Platform.runLater(() -> serverStatusLabel.setText("Starting..."));
            return;
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
                serverStatusLabel.setText("Online");
                serverStatusLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold; -fx-font-size: 14px;");
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
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setMinWidth(150);
        return label;
    }

    private Label createValueLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.NORMAL, 14));
        return label;
    }
}
