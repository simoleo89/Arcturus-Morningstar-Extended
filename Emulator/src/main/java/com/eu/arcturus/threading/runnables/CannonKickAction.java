package com.eu.arcturus.threading.runnables;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.items.interactions.InteractionCannon;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.rooms.RoomTile;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.generic.alerts.BubbleAlertComposer;
import java.util.HashMap;

import java.util.List;

public class CannonKickAction implements Runnable {
    private final InteractionCannon cannon;
    private final Room room;
    private final GameClient client;

    public CannonKickAction(InteractionCannon cannon, Room room, GameClient client) {
        this.cannon = cannon;
        this.room = room;
        this.client = client;
    }

    @Override
    public void run() {
        if (this.client != null) {
            this.client.getHabbo().getRoomUnit().setCanWalk(true);
        }
        HashMap<String, String> dater = new HashMap<>();
        dater.put("title", "${notification.room.kick.cannonball.title}");
        dater.put("message", "${notification.room.kick.cannonball.message}");

        int rotation = this.cannon.getRotation();
        List<RoomTile> tiles = this.room.getLayout().getTilesInFront(this.room.getLayout().getTile(this.cannon.getX(), this.cannon.getY()), rotation + 6, 3);

        ServerMessage message = new BubbleAlertComposer("cannon.png", dater).compose();

        for (RoomTile t : tiles) {
            for (Habbo habbo : this.room.getHabbosAt(t.x, t.y)) {
                if (!habbo.hasPermission(Permission.ACC_UNKICKABLE) && !this.room.isOwner(habbo)) {
                    Emulator.getGameEnvironment().getRoomManager().leaveRoom(habbo, this.room);
                    habbo.getClient().sendResponse(message); //kicked composer
                }
            }
        }
    }
}
