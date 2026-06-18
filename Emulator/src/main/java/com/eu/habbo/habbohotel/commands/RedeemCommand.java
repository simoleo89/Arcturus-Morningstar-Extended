package com.eu.habbo.habbohotel.commands;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.gameclients.GameClient;
import com.eu.habbo.habbohotel.rooms.RoomChatMessageBubbles;
import com.eu.habbo.habbohotel.users.HabboItem;
import com.eu.habbo.messages.outgoing.inventory.InventoryRefreshComposer;
import com.eu.habbo.threading.runnables.QueryDeleteHabboItems;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;

public class RedeemCommand extends Command {
    public RedeemCommand() {
        super("cmd_redeem", Emulator.getTexts().getValue("commands.keys.cmd_redeem").split(";"));
    }

    @Override
    public boolean handle(final GameClient gameClient, String[] params) throws Exception {
        if (gameClient.getHabbo().getHabboInfo().getCurrentRoom().getActiveTradeForHabbo(gameClient.getHabbo()) != null)
            return false;
        ArrayList<HabboItem> items = new ArrayList<>();

        int credits = 0;
        int pixels = 0;

        Int2IntMap points = new Int2IntOpenHashMap();

        for (HabboItem item : gameClient.getHabbo().getInventory().getItemsComponent().getItemsAsValueCollection()) {
            if (item.getBaseItem().getName().startsWith("CF_") || item.getBaseItem().getName().startsWith("CFC_") || item.getBaseItem().getName().startsWith("DF_") || item.getBaseItem().getName().startsWith("PF_")) {
                if (item.getUserId() == gameClient.getHabbo().getHabboInfo().getId()) {
                    items.add(item);
                    if ((item.getBaseItem().getName().startsWith("CF_") || item.getBaseItem().getName().startsWith("CFC_")) && !item.getBaseItem().getName().contains("_diamond_")) {
                        try {
                            credits += Integer.parseInt(item.getBaseItem().getName().split("_")[1]);
                        } catch (Exception e) {
                        }

                    } else if (item.getBaseItem().getName().startsWith("PF_")) {
                        try {
                            pixels += Integer.parseInt(item.getBaseItem().getName().split("_")[1]);
                        } catch (Exception e) {
                        }
                    } else if (item.getBaseItem().getName().startsWith("DF_")) {
                        int pointsType;
                        int pointsAmount;

                        pointsType = Integer.parseInt(item.getBaseItem().getName().split("_")[1]);
                        pointsAmount = Integer.parseInt(item.getBaseItem().getName().split("_")[2]);

                        points.mergeInt(pointsType, pointsAmount, Integer::sum);
                    }
                    else if (item.getBaseItem().getName().startsWith("CF_diamond_")) {
                        int pointsType;
                        int pointsAmount;

                        pointsType = 5;
                        pointsAmount = Integer.parseInt(item.getBaseItem().getName().split("_")[2]);

                        points.mergeInt(pointsType, pointsAmount, Integer::sum);
                    }
                }
            }
        }

        Int2ObjectOpenHashMap<HabboItem> deleted = new Int2ObjectOpenHashMap<>();
        for (HabboItem item : items) {
            gameClient.getHabbo().getInventory().getItemsComponent().removeHabboItem(item);
            deleted.put(item.getId(), item);
        }

        Emulator.getThreading().run(new QueryDeleteHabboItems(deleted));

        gameClient.sendResponse(new InventoryRefreshComposer());
        gameClient.getHabbo().giveCredits(credits);
        gameClient.getHabbo().givePixels(pixels);

        final String[] message = {Emulator.getTexts().getValue("generic.redeemed")};

        message[0] += Emulator.getTexts().getValue("generic.credits");
        message[0] += ": " + credits;

        if (pixels > 0) {
            message[0] += ", " + Emulator.getTexts().getValue("generic.pixels");
            message[0] += ": " + pixels + "";
        }

        if (!points.isEmpty()) {
            for (Int2IntMap.Entry e : points.int2IntEntrySet()) {
                int a = e.getIntKey();
                int b = e.getIntValue();
                gameClient.getHabbo().givePoints(a, b);
                message[0] += " ," + Emulator.getTexts().getValue("seasonal.name." + a) + ": " + b;
            }
        }

        gameClient.getHabbo().whisper(message[0], RoomChatMessageBubbles.ALERT);

        return true;
    }
}
