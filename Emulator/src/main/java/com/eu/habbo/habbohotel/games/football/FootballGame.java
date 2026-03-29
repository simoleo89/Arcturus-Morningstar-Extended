package com.eu.habbo.habbohotel.games.football;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.achievements.AchievementManager;
import com.eu.habbo.habbohotel.games.Game;
import com.eu.habbo.habbohotel.games.GamePlayer;
import com.eu.habbo.habbohotel.games.GameTeam;
import com.eu.habbo.habbohotel.games.GameTeamColors;
import com.eu.habbo.habbohotel.items.interactions.games.football.InteractionFootball;
import com.eu.habbo.habbohotel.items.interactions.games.football.scoreboards.InteractionFootballScoreboard;
import com.eu.habbo.habbohotel.rooms.Room;
import com.eu.habbo.habbohotel.rooms.RoomUnit;
import com.eu.habbo.habbohotel.rooms.RoomUserAction;
import com.eu.habbo.habbohotel.users.Habbo;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.outgoing.rooms.users.RoomUserActionComposer;

import java.util.Map;

public class FootballGame extends Game {
    private final Room room;

    public FootballGame(Room room) {
        super(FootballGameTeam.class, FootballGamePlayer.class, room, true);
        this.room = room;
    }

    @Override
    public void initialise() {
        // Reset all scoreboards to 0
        for (HabboItem item : this.room.getRoomSpecialTypes().getItemsOfType(InteractionFootballScoreboard.class)) {
            item.setExtradata("0");
            this.room.updateItemState(item);
        }

        // Reset ball position
        for (HabboItem item : this.room.getRoomSpecialTypes().getItemsOfType(InteractionFootball.class)) {
            item.setExtradata("0");
            this.room.updateItemState(item);
        }

        this.state = com.eu.habbo.habbohotel.games.GameState.RUNNING;
        this.isRunning = true;
    }

    @Override
    public void run() {
        if (!this.isRunning) return;

        // Football game logic runs per-tick via the room cycle
        // Score tracking and team management handled by onScore() callback
        // Game ends when the timer runs out (handled by InteractionGameTimer)
    }

    public void onScore(RoomUnit kicker, GameTeamColors team) {
        if (this.room == null || !this.room.isLoaded())
            return;

        Habbo habbo = this.room.getHabbo(kicker);
        if (habbo != null) {
            AchievementManager.progressAchievement(habbo, Emulator.getGameEnvironment().getAchievementManager().getAchievement("FootballGoalScored"));
            if (habbo.getHabboInfo().getId() != this.room.getOwnerId()) {
                AchievementManager.progressAchievement(this.room.getOwnerId(), Emulator.getGameEnvironment().getAchievementManager().getAchievement("FootballGoalScoredInRoom"));
            }

            // Award points to the goal scorer
            GamePlayer player = habbo.getHabboInfo().getGamePlayer();
            if (player != null) {
                player.addScore(1);
            }
        }

        this.room.sendComposer(new RoomUserActionComposer(kicker, RoomUserAction.WAVE).compose());

        for (Map.Entry<Integer, InteractionFootballScoreboard> scoreBoard : this.room.getRoomSpecialTypes().getFootballScoreboards(team).entrySet()) {
            scoreBoard.getValue().changeScore(1);
        }
    }
}
