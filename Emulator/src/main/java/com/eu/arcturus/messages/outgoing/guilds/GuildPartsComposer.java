package com.eu.arcturus.messages.outgoing.guilds;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.guilds.GuildPart;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

public class GuildPartsComposer extends MessageComposer {
    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.GroupPartsComposer);
        this.response.appendInt(Emulator.getGameEnvironment().getGuildManager().getBases().size());
        for (GuildPart part : Emulator.getGameEnvironment().getGuildManager().getBases()) {
            this.response.appendInt(part.id);
            this.response.appendString(part.valueA);
            this.response.appendString(part.valueB);
        }

        this.response.appendInt(Emulator.getGameEnvironment().getGuildManager().getSymbols().size());
        for (GuildPart part : Emulator.getGameEnvironment().getGuildManager().getSymbols()) {
            this.response.appendInt(part.id);
            this.response.appendString(part.valueA);
            this.response.appendString(part.valueB);
        }

        this.response.appendInt(Emulator.getGameEnvironment().getGuildManager().getBaseColors().size());
        for (GuildPart part : Emulator.getGameEnvironment().getGuildManager().getBaseColors()) {
            this.response.appendInt(part.id);
            this.response.appendString(part.valueA);
        }

        this.response.appendInt(Emulator.getGameEnvironment().getGuildManager().getSymbolColors().size());
        for (GuildPart part : Emulator.getGameEnvironment().getGuildManager().getSymbolColors()) {
            this.response.appendInt(part.id);
            this.response.appendString(part.valueA);
        }

        this.response.appendInt(Emulator.getGameEnvironment().getGuildManager().getBackgroundColors().size());
        for (GuildPart part : Emulator.getGameEnvironment().getGuildManager().getBackgroundColors()) {
            this.response.appendInt(part.id);
            this.response.appendString(part.valueA);
        }

        return this.response;
    }
}
