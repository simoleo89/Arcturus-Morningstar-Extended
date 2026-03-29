package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.modtool.ModToolSanctionItem;
import com.eu.arcturus.habbohotel.modtool.ModToolSanctions;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.ModToolIssueHandledComposer;
import java.util.HashMap;

import java.util.ArrayList;

public class ModToolSanctionTradeLockEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 2000;
    }

    @Override
    public void handle() throws Exception {
        int userId = this.packet.readInt();
        String message = this.packet.readString();
        int duration = this.packet.readInt();
        int cfhTopic = this.packet.readInt();

        if (this.client.getHabbo().hasPermission(Permission.ACC_SUPPORTTOOL)) {
            Habbo habbo = Emulator.getGameEnvironment().getHabboManager().getHabbo(userId);

            if (habbo != null) {
                ModToolSanctions modToolSanctions = Emulator.getGameEnvironment().getModToolSanctions();

                if (Emulator.getConfig().getBoolean("hotel.sanctions.enabled")) {
                    HashMap<Integer, ArrayList<ModToolSanctionItem>> modToolSanctionItemsHashMap = Emulator.getGameEnvironment().getModToolSanctions().getSanctions(userId);
                    ArrayList<ModToolSanctionItem> modToolSanctionItems = modToolSanctionItemsHashMap.get(userId);

                    if (modToolSanctionItems != null && !modToolSanctionItems.isEmpty()) {
                        ModToolSanctionItem item = modToolSanctionItems.get(modToolSanctionItems.size() - 1);

                        if (item.probationTimestamp > 0 && item.probationTimestamp >= Emulator.getIntUnixTimestamp()) {
                            modToolSanctions.run(userId, this.client.getHabbo(), item.sanctionLevel, cfhTopic, message, duration, false, 0);
                        } else {
                            modToolSanctions.run(userId, this.client.getHabbo(), item.sanctionLevel, cfhTopic, message, duration, false, 0);
                        }
                    } else {
                        modToolSanctions.run(userId, this.client.getHabbo(), 0, cfhTopic, message, duration, false, 0);
                    }
                } else {
                    habbo.getHabboStats().setAllowTrade(false);
                    habbo.alert(message);
                }
            } else {
                this.client.sendResponse(new ModToolIssueHandledComposer(Emulator.getTexts().getValue("generic.user.not_found").replace("%user%", Emulator.getConfig().getValue("hotel.player.name"))));
            }
        }
    }
}