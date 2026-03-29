package com.eu.arcturus.messages.outgoing.guardians;

import com.eu.arcturus.habbohotel.guides.GuardianTicket;
import com.eu.arcturus.habbohotel.modtool.ModToolChatLog;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;
import java.util.HashMap;

import java.util.Calendar;

public class GuardianVotingRequestedComposer extends MessageComposer {
    private final GuardianTicket ticket;

    public GuardianVotingRequestedComposer(GuardianTicket ticket) {
        this.ticket = ticket;
    }

    @Override
    protected ServerMessage composeInternal() {
        HashMap<Integer, Integer> mappedUsers = new HashMap<>();
        mappedUsers.put(this.ticket.getReported().getHabboInfo().getId(), 0);

        Calendar c = Calendar.getInstance();
        c.setTime(this.ticket.getDate());

        StringBuilder fullMessage = new StringBuilder(c.get(Calendar.YEAR) + " ");
        fullMessage.append(c.get(Calendar.MONTH)).append(" ");
        fullMessage.append(c.get(Calendar.DAY_OF_MONTH)).append(" ");
        fullMessage.append(c.get(Calendar.MINUTE)).append(" ");
        fullMessage.append(c.get(Calendar.SECOND)).append(";");

        fullMessage.append("\r");

        for (ModToolChatLog chatLog : this.ticket.getChatLogs()) {
            if (!mappedUsers.containsKey(chatLog.habboId)) {
                mappedUsers.put(chatLog.habboId, mappedUsers.size());
            }

            fullMessage.append("unused;").append(mappedUsers.get(chatLog.habboId)).append(";").append(chatLog.message).append("\r");
        }

        this.response.init(Outgoing.GuardianVotingRequestedComposer);
        this.response.appendInt(this.ticket.getTimeLeft());
        this.response.appendString(fullMessage.toString());

        //2015 10 17 14 24 30
        return this.response;
    }

    public GuardianTicket getTicket() {
        return ticket;
    }
}
