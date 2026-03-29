@echo off
java -Dfile.encoding=UTF8 -Xmx4096m --add-modules javafx.controls,javafx.fxml --add-opens javafx.graphics/javafx.scene=ALL-UNNAMED -jar Habbo-4.1.0-jar-with-dependencies.jar --gui
pause
