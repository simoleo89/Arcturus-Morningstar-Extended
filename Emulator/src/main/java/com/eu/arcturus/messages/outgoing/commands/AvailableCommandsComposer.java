package com.eu.arcturus.messages.outgoing.commands;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.commands.Command;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.List;

public class AvailableCommandsComposer extends MessageComposer {
    private final List<Command> commands;

    public AvailableCommandsComposer(List<Command> commands) {
        this.commands = commands;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.AvailableCommandsComposer);
        this.response.appendInt(this.commands.size());

        for (Command cmd : this.commands) {
            this.response.appendString(cmd.keys[0]);
            this.response.appendString(
                    Emulator.getTexts().getValue("commands.description." + cmd.permission, cmd.permission)
            );
        }

        return this.response;
    }
}