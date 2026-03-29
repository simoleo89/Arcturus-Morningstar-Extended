package com.eu.arcturus.gui;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.gui.controller.ConfigTabController;
import com.eu.arcturus.gui.controller.ConsoleTabController;
import com.eu.arcturus.gui.controller.DashboardTabController;
import com.eu.arcturus.gui.logging.GUILogAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

public class EmulatorGUI extends Application {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmulatorGUI.class);

    @Override
    public void start(Stage primaryStage) {
        registerGUILogAppender();

        ConsoleTabController consoleTab = new ConsoleTabController();
        DashboardTabController dashboardTab = new DashboardTabController();
        ConfigTabController configTab = new ConfigTabController();

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                consoleTab.createTab(),
                dashboardTab.createTab(),
                configTab.createTab()
        );

        Scene scene = new Scene(tabPane, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/gui/style.css") != null
                ? getClass().getResource("/gui/style.css").toExternalForm()
                : "");

        primaryStage.setTitle("Arcturus Morningstar " + Emulator.MAJOR + "." + Emulator.MINOR + "." + Emulator.BUILD);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(450);

        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            LOGGER.info("Shutdown requested from GUI...");
            Emulator.prepareShutdown();
            Platform.exit();
        });

        primaryStage.show();

        // Start the emulator server on a background thread
        Thread.startVirtualThread(() -> {
            try {
                Emulator.startServer();
            } catch (Exception e) {
                LOGGER.error("Failed to start emulator server", e);
            }
        });
    }

    private void registerGUILogAppender() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        GUILogAppender appender = new GUILogAppender();
        appender.setContext(loggerContext);
        appender.setName("GUI");
        appender.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
    }

    public static void launchGUI(String[] args) {
        Application.launch(EmulatorGUI.class, args);
    }
}
