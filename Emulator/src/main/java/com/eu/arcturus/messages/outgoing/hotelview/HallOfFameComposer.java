package com.eu.arcturus.messages.outgoing.hotelview;

import com.eu.arcturus.habbohotel.hotelview.HallOfFame;
import com.eu.arcturus.habbohotel.hotelview.HallOfFameWinner;
import com.eu.arcturus.messages.ServerMessage;
import com.eu.arcturus.messages.outgoing.MessageComposer;
import com.eu.arcturus.messages.outgoing.Outgoing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HallOfFameComposer extends MessageComposer {
    private final HallOfFame hallOfFame;

    public HallOfFameComposer(HallOfFame hallOfFame) {
        this.hallOfFame = hallOfFame;
    }

    @Override
    protected ServerMessage composeInternal() {
        this.response.init(Outgoing.HallOfFameComposer);
        this.response.appendString(this.hallOfFame.getCompetitionName());
        this.response.appendInt(this.hallOfFame.getWinners().size());

        int count = 1;

        List<HallOfFameWinner> winners = new ArrayList<>(this.hallOfFame.getWinners().values());
        Collections.sort(winners);
        for (HallOfFameWinner winner : winners) {
            this.response.appendInt(winner.getId());
            this.response.appendString(winner.getUsername());
            this.response.appendString(winner.getLook());
            this.response.appendInt(count);
            this.response.appendInt(winner.getPoints());
            count++;
        }
        return this.response;
    }

    public HallOfFame getHallOfFame() {
        return hallOfFame;
    }
}
