package com.eu.arcturus.messages.incoming.wired;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWired;
import com.eu.arcturus.habbohotel.items.interactions.InteractionWiredTrigger;
import com.eu.arcturus.habbohotel.items.interactions.wired.WiredSettings;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.wired.core.WiredManager;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.generic.alerts.UpdateFailedComposer;
import com.eu.arcturus.messages.outgoing.wired.WiredSavedComposer;

public class WiredTriggerSaveDataEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        int itemId = this.packet.readInt();

        Room room = this.client.getHabbo().getHabboInfo().getCurrentRoom();

        if (room != null) {
            if (room.hasRights(this.client.getHabbo()) || room.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() || this.client.getHabbo().hasPermission(Permission.ACC_ANYROOMOWNER) || this.client.getHabbo().hasPermission(Permission.ACC_MOVEROTATE)) {
                InteractionWiredTrigger trigger = room.getRoomSpecialTypes().getTrigger(itemId);

                if (trigger != null) {
                    WiredSettings settings = InteractionWired.readSettings(this.packet, false);

                    try {
                        boolean saved = trigger.saveData(settings, this.client);

                        if (saved) {
                            this.client.sendResponse(new WiredSavedComposer());
                            trigger.needsUpdate(true);
                            Emulator.getThreading().run(trigger);
                            WiredManager.invalidateRoom(room);
                        } else {
                            this.client.sendResponse(new UpdateFailedComposer("There was an error while saving that trigger"));
                        }
                    } catch (WiredTriggerSaveException e) {
                        this.client.sendResponse(new UpdateFailedComposer(e.getMessage()));
                    }
                }
            }
        }
    }
}
