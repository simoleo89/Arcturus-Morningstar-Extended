package com.eu.arcturus.messages.incoming.camera;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.items.Item;
import com.eu.arcturus.habbohotel.users.Habbo;
import com.eu.arcturus.habbohotel.users.HabboInfo;
import com.eu.arcturus.habbohotel.users.HabboItem;
import com.eu.arcturus.messages.incoming.MessageHandler;
import com.eu.arcturus.messages.outgoing.camera.CameraPurchaseSuccesfullComposer;
import com.eu.arcturus.messages.outgoing.catalog.NotEnoughPointsTypeComposer;
import com.eu.arcturus.messages.outgoing.inventory.AddHabboItemComposer;
import com.eu.arcturus.messages.outgoing.inventory.InventoryRefreshComposer;
import com.eu.arcturus.plugin.events.users.UserPurchasePictureEvent;

public class CameraPurchaseEvent extends MessageHandler {
    public static int CAMERA_PURCHASE_CREDITS = 2;
    public static int CAMERA_PURCHASE_POINTS = 0;
    public static int CAMERA_PURCHASE_POINTS_TYPE = 5;

    @Override
    public void handle() {
        Habbo habbo = this.client.getHabbo();
        HabboInfo habboInfo = habbo.getHabboInfo();

        if (habboInfo.getCredits() < CAMERA_PURCHASE_CREDITS) {
            habbo.alert("You don't have enough credits!");
            this.client.sendResponse(new NotEnoughPointsTypeComposer(true, false, 0));
            return;
        }

        if (habboInfo.getCurrencyAmount(CAMERA_PURCHASE_POINTS_TYPE) < CAMERA_PURCHASE_POINTS) {
            String alertMessage = "You don't have enough " + Emulator.getTexts().getValue("seasonal.name." + CAMERA_PURCHASE_POINTS_TYPE, "currency") + "!";
            habbo.alert(alertMessage);
            this.client.sendResponse(new NotEnoughPointsTypeComposer(false, true, CAMERA_PURCHASE_POINTS_TYPE));
            return;
        }

        if (habboInfo.getPhotoTimestamp() == 0 || habboInfo.getPhotoJSON().isEmpty()
                || !habboInfo.getPhotoJSON().contains(Integer.toString(habboInfo.getPhotoTimestamp())))
            return;

        if (Emulator.getPluginManager().fireEvent(new UserPurchasePictureEvent(habbo, habboInfo.getPhotoURL(), habboInfo.getCurrentRoom().getId(), habboInfo.getPhotoTimestamp())).isCancelled())
            return;

        Item item = Emulator.getGameEnvironment().getItemManager().getItem(Emulator.getConfig().getInt("camera.item_id"));
        if (item == null || !item.getInteractionType().getName().equals("external_image"))
            return;

        HabboItem photoItem = Emulator.getGameEnvironment().getItemManager().createItem(habboInfo.getId(), item, 0, 0, habboInfo.getPhotoJSON());
        if (photoItem != null) {
            photoItem.setExtradata(photoItem.getExtradata().replace("%id%", Integer.toString(photoItem.getId())));
            photoItem.needsUpdate(true);
            habbo.getInventory().getItemsComponent().addItem(photoItem);

            this.client.sendResponse(new CameraPurchaseSuccesfullComposer());
            this.client.sendResponse(new AddHabboItemComposer(photoItem));
            this.client.sendResponse(new InventoryRefreshComposer());

            habbo.giveCredits(-CAMERA_PURCHASE_CREDITS);
            habbo.givePoints(CAMERA_PURCHASE_POINTS_TYPE, -CAMERA_PURCHASE_POINTS);

            AchievementManager.progressAchievement(habbo, Emulator.getGameEnvironment().getAchievementManager().getAchievement("CameraPhotoCount"));
        }
    }
}
