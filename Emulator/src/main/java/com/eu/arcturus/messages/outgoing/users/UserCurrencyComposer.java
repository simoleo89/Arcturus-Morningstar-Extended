package com.eu.arcturus.messages.outgoing.users;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserCurrencyComposer extends MessageComposer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCurrencyComposer.class);

    private final Habbo habbo;

    public UserCurrencyComposer(Habbo habbo) {
        this.habbo = habbo;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.UserCurrencyComposer);
        String[] pointsTypes = Emulator.getConfig().getValue("seasonal.types").split(";");
        this.response.appendInt(pointsTypes.length);
        for (String s : pointsTypes) {
            int type;
            try {
                type = Integer.parseInt(s);
            } catch (Exception e) {
                LOGGER.error("Caught exception", e);
                return null;
            }

            this.response.appendInt(type);
            this.response.appendInt(this.habbo.getHabboInfo().getCurrencyAmount(type));
        }
        return this.response;
    }

    public Habbo getHabbo() {
        return habbo;
    }
}
