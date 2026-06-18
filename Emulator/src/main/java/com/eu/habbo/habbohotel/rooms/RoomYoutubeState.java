package com.eu.habbo.habbohotel.rooms;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Holds the YouTube ("TV") playback state of a room: whether the feature is
 * enabled, the currently playing video and who queued it, the playlist, and
 * the set of users currently watching.
 *
 * <p>Extracted from {@link Room} to keep that (very large) class more focused
 * and to make this self-contained concern unit-testable. {@code Room} keeps its
 * existing public {@code youtube*} API and simply delegates to an instance of
 * this class, so external callers are unaffected.
 */
public class RoomYoutubeState {
    private boolean enabled = false;
    private String currentVideo = "";
    private String senderName = "";
    private final List<String> playlist = new CopyOnWriteArrayList<>();
    private final Set<Integer> watchers = ConcurrentHashMap.newKeySet();

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCurrentVideo() {
        return this.currentVideo;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public List<String> getPlaylist() {
        return this.playlist;
    }

    public Set<Integer> getWatchers() {
        return this.watchers;
    }

    /** Sets the currently playing video and replaces the playlist. */
    public void setVideo(String videoId, String senderName, List<String> playlist) {
        this.currentVideo = videoId;
        this.senderName = senderName;
        this.playlist.clear();
        if (playlist != null) {
            this.playlist.addAll(playlist);
        }
    }

    /** Stops playback: clears the current video, sender and playlist. */
    public void clearVideo() {
        this.currentVideo = "";
        this.senderName = "";
        this.playlist.clear();
    }

    /**
     * Removes a user from the watchers set.
     *
     * @return {@code true} if the user was watching (and was removed).
     */
    public boolean removeWatcher(int userId) {
        return this.watchers.remove(userId);
    }
}
