package com.eu.habbo.habbohotel.rooms;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoomYoutubeStateTest {

    @Test
    void defaultsAreEmptyAndDisabled() {
        RoomYoutubeState state = new RoomYoutubeState();
        assertFalse(state.isEnabled());
        assertEquals("", state.getCurrentVideo());
        assertEquals("", state.getSenderName());
        assertTrue(state.getPlaylist().isEmpty());
        assertTrue(state.getWatchers().isEmpty());
    }

    @Test
    void setVideoStoresVideoSenderAndPlaylist() {
        RoomYoutubeState state = new RoomYoutubeState();
        state.setVideo("abc123", "Alice", List.of("v1", "v2"));

        assertEquals("abc123", state.getCurrentVideo());
        assertEquals("Alice", state.getSenderName());
        assertEquals(List.of("v1", "v2"), state.getPlaylist());
    }

    @Test
    void setVideoReplacesPreviousPlaylist() {
        RoomYoutubeState state = new RoomYoutubeState();
        state.setVideo("first", "Alice", List.of("a", "b"));
        state.setVideo("second", "Bob", List.of("c"));

        assertEquals("second", state.getCurrentVideo());
        assertEquals(List.of("c"), state.getPlaylist());
    }

    @Test
    void setVideoToleratesNullPlaylist() {
        RoomYoutubeState state = new RoomYoutubeState();
        state.setVideo("x", "Alice", null);
        assertTrue(state.getPlaylist().isEmpty());
    }

    @Test
    void clearVideoResetsPlaybackButKeepsEnabledFlag() {
        RoomYoutubeState state = new RoomYoutubeState();
        state.setEnabled(true);
        state.setVideo("x", "Alice", List.of("a"));

        state.clearVideo();

        assertEquals("", state.getCurrentVideo());
        assertEquals("", state.getSenderName());
        assertTrue(state.getPlaylist().isEmpty());
        assertTrue(state.isEnabled(), "clearVideo must not disable the feature");
    }

    @Test
    void removeWatcherReportsWhetherUserWasWatching() {
        RoomYoutubeState state = new RoomYoutubeState();
        state.getWatchers().add(42);

        assertTrue(state.removeWatcher(42));
        assertFalse(state.removeWatcher(42));
        assertTrue(state.getWatchers().isEmpty());
    }
}
