package com.eu.arcturus.habbohotel;

import com.eu.arcturus.Emulator;
import com.eu.arcturus.core.*;
import com.eu.arcturus.habbohotel.achievements.AchievementManager;
import com.eu.arcturus.habbohotel.bots.BotManager;
import com.eu.arcturus.habbohotel.campaign.calendar.CalendarManager;
import com.eu.arcturus.habbohotel.catalog.CatalogManager;
import com.eu.arcturus.habbohotel.commands.CommandHandler;
import com.eu.arcturus.habbohotel.crafting.CraftingManager;
import com.eu.arcturus.habbohotel.guides.GuideManager;
import com.eu.arcturus.habbohotel.guilds.GuildManager;
import com.eu.arcturus.habbohotel.hotelview.HotelViewManager;
import com.eu.arcturus.habbohotel.items.ItemManager;
import com.eu.arcturus.habbohotel.modtool.ModToolManager;
import com.eu.arcturus.habbohotel.modtool.ModToolSanctions;
import com.eu.arcturus.habbohotel.modtool.WordFilter;
import com.eu.arcturus.habbohotel.navigation.NavigatorManager;
import com.eu.arcturus.habbohotel.permissions.PermissionsManager;
import com.eu.arcturus.habbohotel.pets.PetManager;
import com.eu.arcturus.habbohotel.polls.PollManager;
import com.eu.arcturus.habbohotel.rooms.RoomChatBubbleManager;
import com.eu.arcturus.habbohotel.rooms.RoomManager;
import com.eu.arcturus.habbohotel.users.HabboManager;
import com.eu.arcturus.habbohotel.users.subscriptions.SubscriptionManager;
import com.eu.arcturus.habbohotel.users.subscriptions.SubscriptionScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameEnvironment {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameEnvironment.class);

    public CreditsScheduler creditsScheduler;
    public PixelScheduler pixelScheduler;
    public PointsScheduler pointsScheduler;
    public GotwPointsScheduler gotwPointsScheduler;
    public SubscriptionScheduler subscriptionScheduler;

    private HabboManager habboManager;
    private NavigatorManager navigatorManager;
    private GuildManager guildManager;
    private ItemManager itemManager;
    private CatalogManager catalogManager;
    private HotelViewManager hotelViewManager;
    private RoomManager roomManager;
    private CommandHandler commandHandler;
    private PermissionsManager permissionsManager;
    private BotManager botManager;
    private ModToolManager modToolManager;
    private ModToolSanctions modToolSanctions;
    private PetManager petManager;
    private AchievementManager achievementManager;
    private GuideManager guideManager;
    private WordFilter wordFilter;
    private CraftingManager craftingManager;
    private PollManager pollManager;
    private SubscriptionManager subscriptionManager;
    private CalendarManager calendarManager;
    private RoomChatBubbleManager roomChatBubbleManager;

    public void load() throws Exception {
        LOGGER.info("GameEnvironment -> Loading...");

        this.permissionsManager = new PermissionsManager();
        this.habboManager = new HabboManager();
        this.hotelViewManager = new HotelViewManager();
        this.itemManager = new ItemManager();
        this.itemManager.load();
        this.botManager = new BotManager();
        this.petManager = new PetManager();
        this.guildManager = new GuildManager();
        this.catalogManager = new CatalogManager();
        this.roomManager = new RoomManager();
        this.navigatorManager = new NavigatorManager();
        this.commandHandler = new CommandHandler();
        this.modToolManager = new ModToolManager();
        this.modToolSanctions = new ModToolSanctions();
        this.achievementManager = new AchievementManager();
        this.achievementManager.reload();
        this.guideManager = new GuideManager();
        this.wordFilter = new WordFilter();
        this.craftingManager = new CraftingManager();
        this.pollManager = new PollManager();
        this.calendarManager = new CalendarManager();
        this.roomChatBubbleManager = new RoomChatBubbleManager();

        this.roomManager.loadPublicRooms();
        this.navigatorManager.loadNavigator();

        this.creditsScheduler = new CreditsScheduler();
        Emulator.getThreading().run(this.creditsScheduler);
        this.pixelScheduler = new PixelScheduler();
        Emulator.getThreading().run(this.pixelScheduler);
        this.pointsScheduler = new PointsScheduler();
        Emulator.getThreading().run(this.pointsScheduler);
        this.gotwPointsScheduler = new GotwPointsScheduler();
        Emulator.getThreading().run(this.gotwPointsScheduler);

        this.subscriptionManager = new SubscriptionManager();
        this.subscriptionManager.init();

        this.subscriptionScheduler = new SubscriptionScheduler();
        Emulator.getThreading().run(this.subscriptionScheduler);

        LOGGER.info("GameEnvironment -> Loaded!");
    }

    public void dispose() {
        this.pointsScheduler.setDisposed(true);
        this.pixelScheduler.setDisposed(true);
        this.creditsScheduler.setDisposed(true);
        this.gotwPointsScheduler.setDisposed(true);
        this.craftingManager.dispose();
        this.habboManager.dispose();
        this.commandHandler.dispose();
        this.guildManager.dispose();
        this.catalogManager.dispose();
        this.roomManager.dispose();
        this.itemManager.dispose();
        this.hotelViewManager.dispose();
        this.subscriptionManager.dispose();
        this.calendarManager.dispose();
        LOGGER.info("GameEnvironment -> Disposed!");
    }

    public HabboManager getHabboManager() {
        return this.habboManager;
    }

    public NavigatorManager getNavigatorManager() {
        return this.navigatorManager;
    }

    public GuildManager getGuildManager() {
        return this.guildManager;
    }

    public ItemManager getItemManager() {
        return this.itemManager;
    }

    public CatalogManager getCatalogManager() {
        return this.catalogManager;
    }

    public HotelViewManager getHotelViewManager() {
        return this.hotelViewManager;
    }

    public RoomManager getRoomManager() {
        return this.roomManager;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public PermissionsManager getPermissionsManager() {
        return this.permissionsManager;
    }

    public BotManager getBotManager() {
        return this.botManager;
    }

    public ModToolManager getModToolManager() {
        return this.modToolManager;
    }

    public ModToolSanctions getModToolSanctions() {
        return this.modToolSanctions;
    }

    public PetManager getPetManager() {
        return this.petManager;
    }

    public AchievementManager getAchievementManager() {
        return this.achievementManager;
    }

    public GuideManager getGuideManager() {
        return this.guideManager;
    }

    public WordFilter getWordFilter() {
        return this.wordFilter;
    }

    public CraftingManager getCraftingManager() {
        return this.craftingManager;
    }

    public PollManager getPollManager() {
        return this.pollManager;
    }

    public CreditsScheduler getCreditsScheduler() {
        return this.creditsScheduler;
    }

    public PixelScheduler getPixelScheduler() {
        return this.pixelScheduler;
    }

    public PointsScheduler getPointsScheduler() { return this.pointsScheduler;
    }

    public GotwPointsScheduler getGotwPointsScheduler() { return this.gotwPointsScheduler;
    }

    public SubscriptionManager getSubscriptionManager() {
        return this.subscriptionManager;
    }

    public CalendarManager getCalendarManager() { return this.calendarManager; }

    public RoomChatBubbleManager getRoomChatBubbleManager() {
        return roomChatBubbleManager;
    }
}
