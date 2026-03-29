package com.eu.arcturus.messages.incoming.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.Guild;
import com.eu.arcturus.habbohotel.permissions.Permission;
import com.eu.arcturus.habbohotel.rooms.Room;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.plugin.events.guilds.GuildChangedNameEvent;

public class GuildChangeNameDescEvent extends MessageHandler {
    @Override
    public int getRatelimit() {
        return 500;
    }

    @Override
    public void handle() throws Exception {
        int guildId = this.packet.readInt();

        Guild guild = Emulator.getGameEnvironment().getGuildManager().getGuild(guildId);

        if (guild != null) {
            if (guild.getOwnerId() == this.client.getHabbo().getHabboInfo().getId() || this.client.getHabbo().hasPermission(Permission.ACC_GUILD_ADMIN)) {
                String newName = Emulator.getGameEnvironment().getWordFilter().filter(this.packet.readString(), this.client.getHabbo());
                String newDesc = Emulator.getGameEnvironment().getWordFilter().filter(this.packet.readString(), this.client.getHabbo());
                GuildChangedNameEvent nameEvent = new GuildChangedNameEvent(guild, newName, newDesc);
                Emulator.getPluginManager().fireEvent(nameEvent);

                if (nameEvent.isCancelled())
                    return;

                if (guild.getName().equals(nameEvent.name) && guild.getDescription().equals(nameEvent.description))
                    return;

                if(nameEvent.name.length() > 29 || nameEvent.description.length() > 254)
                    return;

                guild.setName(nameEvent.name);
                guild.setDescription(nameEvent.description);
                guild.needsUpdate = true;
                guild.run();

                Room room = Emulator.getGameEnvironment().getRoomManager().getRoom(guild.getRoomId());

                if (room != null && !room.getCurrentHabbos().isEmpty()) {
                    room.refreshGuild(guild);
                }
            }
        }
    }
}
