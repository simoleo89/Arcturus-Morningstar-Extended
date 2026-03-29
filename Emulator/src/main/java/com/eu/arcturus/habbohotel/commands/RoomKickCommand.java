package com.eu.arcturus.habbohotel.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.gameclients.GameClient;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.outgoing.generic.alerts.GenericAlertComposer;

public class RoomKickCommand extends Command {
    public RoomKickCommand() {
        super("cmd_kickall", Emulator.getTexts().getValue("commands.keys.cmd_kickall").split(";"));
    }

    @Override
    public boolean handle(GameClient gameClient, String[] params) throws Exception {
        final Room room = gameClient.getHabbo().getHabboInfo().getCurrentRoom();
        if (room != null) {
            if (params.length > 1) {
                StringBuilder message = new StringBuilder();
                for (int i = 1; i < params.length; i++) {
                    message.append(params[i]).append(" ");
                }
                room.sendComposer(new GenericAlertComposer(message + "\r\n-" + gameClient.getHabbo().getHabboInfo().getUsername()).compose());
            }

            for (Habbo habbo : room.getHabbos()) {
                if (!(habbo.hasPermission(Permission.ACC_UNKICKABLE) || habbo.hasPermission(Permission.ACC_SUPPORTTOOL) || room.isOwner(habbo))) {
                    room.kickHabbo(habbo, true);
                }
            }
        }
        return true;
    }
}
