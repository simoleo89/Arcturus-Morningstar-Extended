package com.eu.habbo.habbohotel.items;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class FurnidataWriterTest {

    private static final String SINGLE =
        "{ \"roomitemtypes\": { \"furnitype\": [\n" +
        "  { \"id\": 1, \"classname\": \"01_caterhead\", \"name\": \"old name\", \"description\": \"old desc\" }\n" +
        "] }, \"wallitemtypes\": { \"furnitype\": [] } }";

    @Test
    void writesNameAndDescriptionByClassnameSingleFile() throws Exception {
        Path dir = Files.createTempDirectory("fd");
        Path file = dir.resolve("FurnitureData.json");
        Files.writeString(file, SINGLE);

        FurnidataWriter w = new FurnidataWriter(file, false, 64L * 1024 * 1024, 10);
        boolean ok = w.write("01_caterhead", "Cat Head", "A cat head");

        assertTrue(ok);
        String after = Files.readString(file);
        assertTrue(after.contains("\"Cat Head\""));
        assertTrue(after.contains("\"A cat head\""));
        assertFalse(after.contains("old name"));
        // backup created
        assertTrue(Files.list(dir).anyMatch(p -> p.getFileName().toString().startsWith("FurnitureData.json.bak")));
    }

    @Test
    void rejectsUnknownClassname() throws Exception {
        Path dir = Files.createTempDirectory("fd");
        Path file = dir.resolve("FurnitureData.json");
        Files.writeString(file, SINGLE);
        FurnidataWriter w = new FurnidataWriter(file, false, 64L * 1024 * 1024, 10);
        assertFalse(w.write("does_not_exist", "x", "y"));
    }
}
