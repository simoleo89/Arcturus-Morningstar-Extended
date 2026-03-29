package com.eu.arcturus.messages.incoming.modtool;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.items.interactions.InteractionExternalImage;
import com.eu.arcturus.habbohotel.modtool.CfhTopic;
import com.eu.arcturus.habbohotel.modtool.ModToolIssue;
import com.eu.arcturus.habbohotel.modtool.ModToolTicketType;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.habbohotel.users.HabboInfo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.modtool.ModToolReportReceivedAlertComposer;
import com.eu.arcturus.threading.runnables.InsertModToolIssue;

public class ReportPhotoEvent extends MessageHandler {
    @Override
    public void handle() throws Exception {
        boolean hasExtradataId = this.packet.readShort() != 0;

        this.packet.getBuffer().resetReaderIndex();

        if (hasExtradataId) {
            this.packet.readString();
        }

        int roomId = this.packet.readInt();
        this.packet.readInt();
        int topicId = this.packet.readInt();
        int itemId = this.packet.readInt();

        CfhTopic topic = Emulator.getGameEnvironment().getModToolManager().getCfhTopic(topicId);

        if (topic == null) return;

        Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(roomId);

        if (room == null) return;

        HabboItem item = room.getHabboItem(itemId);

        if (item == null || !(item instanceof InteractionExternalImage)) return;

        HabboInfo photoOwner = Emulator.getGameEnvironment().getHabboManager().getHabboInfo(item.getUserId());

        if (photoOwner == null) return;

        ModToolIssue issue = new ModToolIssue(this.client.getHabbo().getHabboInfo().getId(), this.client.getHabbo().getHabboInfo().getUsername(), photoOwner.getId(), photoOwner.getUsername(), roomId, "", ModToolTicketType.PHOTO);
        issue.photoItem = item;

        new InsertModToolIssue(issue).run();

        this.client.sendResponse(new ModToolReportReceivedAlertComposer(ModToolReportReceivedAlertComposer.REPORT_RECEIVED, ""));
        Emulator.getGameEnvironment().getModToolManager().addTicket(issue);
        Emulator.getGameEnvironment().getModToolManager().updateTicketToMods(issue);
    }
}
