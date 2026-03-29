package com.eu.arcturus.plugin.events.users.calendar;

import com.eu.arcturus.habbohotel.campaign.calendar.CalendarCampaign;
import com.eu.arcturus.habbohotel.campaign.calendar.CalendarRewardObject;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.plugin.events.users.UserEvent;

public class UserClaimRewardEvent extends UserEvent {

    public CalendarCampaign campaign;
    public int day;
    public CalendarRewardObject reward;
    public boolean force;

    public UserClaimRewardEvent(Habbo habbo, CalendarCampaign campaign, int day, CalendarRewardObject reward, boolean force) {
        super(habbo);

        this.campaign = campaign;
        this.day = day;
        this.reward = reward;
        this.force = force;
    }
}
