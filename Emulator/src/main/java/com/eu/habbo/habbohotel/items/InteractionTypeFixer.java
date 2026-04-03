package com.eu.habbo.habbohotel.items;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Scans items_base for furniture with incorrect or missing interaction_type values
 * and fixes them based on known naming patterns and rules.
 */
public class InteractionTypeFixer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionTypeFixer.class);

    /**
     * A rule that maps an item name pattern to the correct interaction_type.
     */
    private static class FixRule {
        final Pattern namePattern;
        final String correctInteractionType;
        final String description;

        FixRule(String regex, String correctInteractionType, String description) {
            this.namePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.correctInteractionType = correctInteractionType;
            this.description = description;
        }
    }

    /**
     * Result of a single fix operation.
     */
    public static class FixResult {
        public final int itemId;
        public final String itemName;
        public final String oldInteractionType;
        public final String newInteractionType;
        public final String rule;

        public FixResult(int itemId, String itemName, String oldInteractionType, String newInteractionType, String rule) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.oldInteractionType = oldInteractionType;
            this.newInteractionType = newInteractionType;
            this.rule = rule;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s: '%s' -> '%s' (%s)", itemId, itemName, oldInteractionType, newInteractionType, rule);
        }
    }

    /**
     * Summary of a full scan/fix run.
     */
    public static class FixSummary {
        public final List<FixResult> fixes;
        public final List<String> warnings;
        public final int totalScanned;
        public final int totalFixed;
        public final int totalInvalid;

        public FixSummary(List<FixResult> fixes, List<String> warnings, int totalScanned, int totalFixed, int totalInvalid) {
            this.fixes = Collections.unmodifiableList(fixes);
            this.warnings = Collections.unmodifiableList(warnings);
            this.totalScanned = totalScanned;
            this.totalFixed = totalFixed;
            this.totalInvalid = totalInvalid;
        }
    }

    // Ordered list of rules - first match wins
    private static final List<FixRule> RULES = new ArrayList<>();

    static {
        // === Wired Triggers ===
        RULES.add(new FixRule("^wf_trg_", "default", "wired trigger prefix (handled by specific wf_trg_ types)"));

        // === Wired Effects ===
        RULES.add(new FixRule("^wf_act_", "default", "wired effect prefix (handled by specific wf_act_ types)"));

        // === Wired Conditions ===
        RULES.add(new FixRule("^wf_cnd_", "default", "wired condition prefix (handled by specific wf_cnd_ types)"));

        // === Wired Extras ===
        RULES.add(new FixRule("^wf_xtra_", "default", "wired extra prefix (handled by specific wf_xtra_ types)"));

        // === Rollers ===
        RULES.add(new FixRule("^roller_.*", "roller", "roller item"));
        RULES.add(new FixRule(".*_roller$", "roller", "roller item (suffix)"));

        // === Teleports ===
        RULES.add(new FixRule(".*_teleport$", "teleport", "teleport item"));
        RULES.add(new FixRule("^teleport_.*", "teleport", "teleport item"));

        // === Dice ===
        RULES.add(new FixRule("^edice_.*", "dice", "external dice"));
        RULES.add(new FixRule("^dice$", "dice", "dice"));
        RULES.add(new FixRule(".*_dice$", "dice", "dice item (suffix)"));

        // === Gates ===
        RULES.add(new FixRule("^gate_.*", "gate", "gate item"));
        RULES.add(new FixRule(".*_gate$", "gate", "gate item (suffix)"));

        // === One Way Gates ===
        RULES.add(new FixRule(".*oneway.*gate.*", "onewaygate", "one-way gate"));
        RULES.add(new FixRule(".*one_way.*gate.*", "onewaygate", "one-way gate"));

        // === Trophies ===
        RULES.add(new FixRule("^trophy_.*", "trophy", "trophy item"));
        RULES.add(new FixRule("^prizetrophy_.*", "trophy", "prize trophy"));
        RULES.add(new FixRule(".*_trophy$", "trophy", "trophy item (suffix)"));

        // === Mannequins ===
        RULES.add(new FixRule("^mannequin_.*", "mannequin", "mannequin"));

        // === Post-it / Sticky ===
        RULES.add(new FixRule("^postit$", "postit", "post-it note"));
        RULES.add(new FixRule("^postit_.*", "postit", "post-it note"));
        RULES.add(new FixRule("^sticky_pole_.*", "sticky_pole", "sticky pole"));

        // === Dimmer / Moodlight ===
        RULES.add(new FixRule("^dimmer$", "dimmer", "moodlight dimmer"));
        RULES.add(new FixRule("^roomdimmer_.*", "dimmer", "room dimmer"));

        // === Vending Machines ===
        RULES.add(new FixRule(".*vend(ing)?_?machine.*", "vendingmachine", "vending machine"));
        RULES.add(new FixRule(".*_vend$", "vendingmachine", "vending machine (suffix)"));

        // === Pressure Plates ===
        RULES.add(new FixRule(".*pressure_?plate.*", "pressureplate", "pressure plate"));
        RULES.add(new FixRule(".*ringplate.*", "pressureplate", "ring plate (pressure plate)"));

        // === Multi-height ===
        RULES.add(new FixRule(".*_multiheight$", "multiheight", "multi-height item"));

        // === Background Toner ===
        RULES.add(new FixRule("^bg_toner$", "background_toner", "background toner"));
        RULES.add(new FixRule("^roombg_.*", "background_toner", "room background toner"));

        // === Badge Display ===
        RULES.add(new FixRule("^badge_display.*", "badge_display", "badge display"));
        RULES.add(new FixRule("^badgedisplay.*", "badge_display", "badge display"));

        // === Love Lock ===
        RULES.add(new FixRule("^love_lock.*", "love_lock", "love lock"));
        RULES.add(new FixRule("^lovelock.*", "love_lock", "love lock"));

        // === Guild/Group Furni ===
        RULES.add(new FixRule("^guild_gate$", "guild_gate", "guild gate"));
        RULES.add(new FixRule("^guild_forum$", "guild_furni", "guild forum"));
        RULES.add(new FixRule("^gld_.*", "guild_furni", "guild furni"));

        // === Cannon ===
        RULES.add(new FixRule("^cannon$", "cannon", "cannon"));
        RULES.add(new FixRule(".*_cannon$", "cannon", "cannon (suffix)"));

        // === Clothing ===
        RULES.add(new FixRule("^clothing_.*", "clothing", "clothing item"));

        // === Gift ===
        RULES.add(new FixRule("^present_gen.*", "gift", "gift wrap"));
        RULES.add(new FixRule("^present_wrap.*", "gift", "gift wrap"));

        // === Stack Helper ===
        RULES.add(new FixRule("^stack_helper$", "stack_helper", "stack helper"));

        // === Puzzle Box ===
        RULES.add(new FixRule("^puzzlebox_.*", "puzzle_box", "puzzle box"));

        // === Hopper ===
        RULES.add(new FixRule("^hopper_.*", "hopper", "hopper"));

        // === Costume Hopper ===
        RULES.add(new FixRule("^costumehopper.*", "costume_hopper", "costume hopper"));

        // === Color Wheel ===
        RULES.add(new FixRule("^colorwheel$", "colorwheel", "color wheel"));
        RULES.add(new FixRule("^colour_wheel.*", "colorwheel", "color wheel"));

        // === Fireworks ===
        RULES.add(new FixRule("^firework.*", "fireworks", "fireworks"));

        // === Jukebox ===
        RULES.add(new FixRule("^jukebox$", "jukebox", "jukebox"));
        RULES.add(new FixRule("^jukebox_.*", "jukebox", "jukebox"));
        RULES.add(new FixRule("^sound_machine.*", "jukebox", "sound machine (jukebox)"));

        // === Music Disc ===
        RULES.add(new FixRule("^song_disk_.*", "musicdisc", "music disc"));
        RULES.add(new FixRule("^musicdisc_.*", "musicdisc", "music disc"));

        // === YouTube TV ===
        RULES.add(new FixRule("^youtube_.*", "youtube", "youtube tv"));

        // === External Image ===
        RULES.add(new FixRule("^external_image_.*", "external_image", "external image"));
        RULES.add(new FixRule("^ads_.*_image$", "external_image", "external image (ads)"));

        // === Room Ads ===
        RULES.add(new FixRule("^ads_mpu_.*", "ads_bg", "room ads background"));

        // === Tent ===
        RULES.add(new FixRule(".*_tent$", "tent", "tent"));
        RULES.add(new FixRule("^tent_.*", "tent", "tent"));

        // === Water ===
        RULES.add(new FixRule("^water$", "water", "water tile"));
        RULES.add(new FixRule("^pool_.*", "water", "pool water tile"));

        // === Nest ===
        RULES.add(new FixRule("^nest_.*", "nest", "pet nest"));
        RULES.add(new FixRule("^petnest_.*", "nest", "pet nest"));

        // === Pet Food ===
        RULES.add(new FixRule("^petfood\\d+$", "pet_food", "pet food"));
        RULES.add(new FixRule("^pet_food_.*", "pet_food", "pet food"));

        // === Pet Drink ===
        RULES.add(new FixRule("^petdrink_.*", "pet_drink", "pet drink"));
        RULES.add(new FixRule("^pet_waterbowl.*", "pet_drink", "pet water bowl"));

        // === Pet Toy ===
        RULES.add(new FixRule("^pettoy_.*", "pet_toy", "pet toy"));
        RULES.add(new FixRule("^pet_toy_.*", "pet_toy", "pet toy"));

        // === Pet Tree ===
        RULES.add(new FixRule("^pet_tree.*", "pet_tree", "pet tree"));

        // === Breeding Nest ===
        RULES.add(new FixRule("^breeding_.*", "breeding_nest", "breeding nest"));
        RULES.add(new FixRule("^pet_breeding_.*", "breeding_nest", "breeding nest"));

        // === Monsterplant Seed ===
        RULES.add(new FixRule("^mnstr_seed.*", "monsterplant_seed", "monsterplant seed"));
        RULES.add(new FixRule("^monsterplant_seed.*", "monsterplant_seed", "monsterplant seed"));

        // === Crackable ===
        RULES.add(new FixRule("^crackable_.*", "crackable", "crackable item"));

        // === Vote Counter ===
        RULES.add(new FixRule("^vote_counter$", "vote_counter", "vote counter"));

        // === Battle Banzai ===
        RULES.add(new FixRule("^bb_patch$", "battlebanzai_tile", "banzai tile"));
        RULES.add(new FixRule("^bb_random_teleport$", "battlebanzai_random_teleport", "banzai teleporter"));
        RULES.add(new FixRule("^bb_blue_gate$", "battlebanzai_gate_blue", "banzai gate blue"));
        RULES.add(new FixRule("^bb_green_gate$", "battlebanzai_gate_green", "banzai gate green"));
        RULES.add(new FixRule("^bb_red_gate$", "battlebanzai_gate_red", "banzai gate red"));
        RULES.add(new FixRule("^bb_yellow_gate$", "battlebanzai_gate_yellow", "banzai gate yellow"));
        RULES.add(new FixRule("^bb_blue_score$", "battlebanzai_counter_blue", "banzai scoreboard blue"));
        RULES.add(new FixRule("^bb_green_score$", "battlebanzai_counter_green", "banzai scoreboard green"));
        RULES.add(new FixRule("^bb_red_score$", "battlebanzai_counter_red", "banzai scoreboard red"));
        RULES.add(new FixRule("^bb_yellow_score$", "battlebanzai_counter_yellow", "banzai scoreboard yellow"));

        // === Freeze ===
        RULES.add(new FixRule("^freeze_block$", "freeze_block", "freeze block"));
        RULES.add(new FixRule("^freeze_tile$", "freeze_tile", "freeze tile"));
        RULES.add(new FixRule("^freeze_exit$", "freeze_exit", "freeze exit"));
        RULES.add(new FixRule("^freeze_blue_gate$", "freeze_gate_blue", "freeze gate blue"));
        RULES.add(new FixRule("^freeze_green_gate$", "freeze_gate_green", "freeze gate green"));
        RULES.add(new FixRule("^freeze_red_gate$", "freeze_gate_red", "freeze gate red"));
        RULES.add(new FixRule("^freeze_yellow_gate$", "freeze_gate_yellow", "freeze gate yellow"));

        // === Football ===
        RULES.add(new FixRule("^football$", "football", "football"));
        RULES.add(new FixRule("^footballgate_.*", "football_gate", "football gate"));

        // === Ice Tag ===
        RULES.add(new FixRule("^icetag_pole$", "icetag_pole", "ice tag pole"));
        RULES.add(new FixRule("^icetag_field$", "icetag_field", "ice tag field"));

        // === Bunny Run ===
        RULES.add(new FixRule("^bunnyrun_pole$", "bunnyrun_pole", "bunnyrun pole"));
        RULES.add(new FixRule("^bunnyrun_field$", "bunnyrun_field", "bunnyrun field"));

        // === Rollerskate ===
        RULES.add(new FixRule("^rollerskate_field$", "rollerskate_field", "rollerskate field"));

        // === Game Timer ===
        RULES.add(new FixRule("^game_timer$", "game_timer", "game timer"));
        RULES.add(new FixRule("^gametimer.*", "game_timer", "game timer"));

        // === Totem ===
        RULES.add(new FixRule("^totem_leg.*", "totem_leg", "totem legs"));
        RULES.add(new FixRule("^totem_head.*", "totem_head", "totem head"));
        RULES.add(new FixRule("^totem_planet.*", "totem_planet", "totem planet"));

        // === FX Box ===
        RULES.add(new FixRule("^fxbox_.*", "fx_box", "FX box"));

        // === Effect Toggle ===
        RULES.add(new FixRule("^effect_toggle_.*", "effect_toggle", "effect toggle"));

        // === Gym Equipment ===
        RULES.add(new FixRule(".*_gym_.*", "gym_equipment", "gym equipment"));

        // === Rentable Space ===
        RULES.add(new FixRule("^rentable_space.*", "rentable_space", "rentable space"));

        // === Pyramid ===
        RULES.add(new FixRule("^pyramid$", "pyramid", "pyramid"));
        RULES.add(new FixRule("^egypt_pyramid$", "pyramid", "pyramid"));

        // === Mute Area ===
        RULES.add(new FixRule("^mutearea_.*", "mutearea", "mute area"));

        // === Build Area ===
        RULES.add(new FixRule("^buildarea_.*", "buildarea", "build area"));

        // === Random State ===
        RULES.add(new FixRule("^random_state_.*", "random_state", "random state item"));

        // === Talking Furni ===
        RULES.add(new FixRule("^talking_furni_.*", "talking_furni", "talking furniture"));

        // === Wired Highscore ===
        RULES.add(new FixRule("^wf_highscore.*", "wf_highscore", "wired highscore"));
        RULES.add(new FixRule("^highscore_.*", "wf_highscore", "wired highscore"));
    }

    /**
     * Get the set of all valid interaction type names registered in ItemManager.
     */
    private static Set<String> getValidInteractionTypes() {
        Set<String> valid = new HashSet<>();
        for (String name : Emulator.getGameEnvironment().getItemManager().getInteractionList()) {
            valid.add(name.toLowerCase());
        }
        return valid;
    }

    /**
     * Scan all items in items_base and find those with invalid interaction types.
     * Does NOT modify the database.
     *
     * @return a summary with proposed fixes and warnings
     */
    public static FixSummary scan() {
        Set<String> validTypes = getValidInteractionTypes();
        List<FixResult> fixes = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        int totalScanned = 0;
        int totalInvalid = 0;

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery("SELECT id, item_name, interaction_type FROM items_base ORDER BY id ASC")) {

            while (set.next()) {
                totalScanned++;
                int id = set.getInt("id");
                String itemName = set.getString("item_name");
                String currentType = set.getString("interaction_type").toLowerCase().trim();

                // Skip items that already have a valid, non-default interaction type
                if (!currentType.isEmpty() && !currentType.equals("default") && validTypes.contains(currentType)) {
                    continue;
                }

                // Try to find a matching rule
                String suggestedType = findCorrectType(itemName);

                if (suggestedType != null && !suggestedType.equals(currentType)) {
                    if (validTypes.contains(suggestedType)) {
                        fixes.add(new FixResult(id, itemName, currentType, suggestedType, getRuleDescription(itemName)));
                    } else {
                        warnings.add(String.format("[%d] %s: suggested '%s' but it's not a registered interaction type", id, itemName, suggestedType));
                    }
                } else if (currentType.isEmpty()) {
                    totalInvalid++;
                    warnings.add(String.format("[%d] %s: empty interaction_type, no matching rule found", id, itemName));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error scanning items_base for interaction type fixes", e);
        }

        return new FixSummary(fixes, warnings, totalScanned, fixes.size(), totalInvalid);
    }

    /**
     * Scan and apply fixes to the database.
     *
     * @return a summary of all changes made
     */
    public static FixSummary fix() {
        FixSummary scanResult = scan();

        if (scanResult.fixes.isEmpty()) {
            LOGGER.info("InteractionTypeFixer: No fixes needed.");
            return scanResult;
        }

        int applied = 0;
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE items_base SET interaction_type = ? WHERE id = ?")) {

            for (FixResult fix : scanResult.fixes) {
                stmt.setString(1, fix.newInteractionType);
                stmt.setInt(2, fix.itemId);
                stmt.addBatch();
                applied++;
            }

            stmt.executeBatch();
            LOGGER.info("InteractionTypeFixer: Applied {} fixes to items_base.", applied);
        } catch (SQLException e) {
            LOGGER.error("Error applying interaction type fixes", e);
        }

        return new FixSummary(scanResult.fixes, scanResult.warnings, scanResult.totalScanned, applied, scanResult.totalInvalid);
    }

    /**
     * Fix a single item by ID, setting a specific interaction type.
     */
    public static boolean fixSingle(int itemId, String newInteractionType) {
        Set<String> validTypes = getValidInteractionTypes();
        if (!validTypes.contains(newInteractionType.toLowerCase())) {
            LOGGER.warn("InteractionTypeFixer: '{}' is not a registered interaction type.", newInteractionType);
            return false;
        }

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE items_base SET interaction_type = ? WHERE id = ?")) {
            stmt.setString(1, newInteractionType);
            stmt.setInt(2, itemId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                LOGGER.info("InteractionTypeFixer: Fixed item {} -> '{}'", itemId, newInteractionType);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("Error fixing single item interaction type", e);
        }
        return false;
    }

    /**
     * Find items whose interaction_type in the DB is not registered in the emulator.
     */
    public static List<FixResult> findUnregisteredTypes() {
        Set<String> validTypes = getValidInteractionTypes();
        List<FixResult> results = new ArrayList<>();

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery(
                     "SELECT id, item_name, interaction_type FROM items_base " +
                     "WHERE interaction_type != '' ORDER BY interaction_type, id")) {

            while (set.next()) {
                String currentType = set.getString("interaction_type").toLowerCase().trim();
                if (!validTypes.contains(currentType)) {
                    String suggested = findCorrectType(set.getString("item_name"));
                    results.add(new FixResult(
                            set.getInt("id"),
                            set.getString("item_name"),
                            currentType,
                            suggested != null ? suggested : "default",
                            suggested != null ? getRuleDescription(set.getString("item_name")) : "no rule match"
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error finding unregistered interaction types", e);
        }

        return results;
    }

    /**
     * Try to find the correct interaction type for an item name based on rules.
     */
    private static String findCorrectType(String itemName) {
        String lower = itemName.toLowerCase();
        for (FixRule rule : RULES) {
            if (rule.namePattern.matcher(lower).matches()) {
                return rule.correctInteractionType;
            }
        }
        return null;
    }

    /**
     * Get the description of the matching rule for an item name.
     */
    private static String getRuleDescription(String itemName) {
        String lower = itemName.toLowerCase();
        for (FixRule rule : RULES) {
            if (rule.namePattern.matcher(lower).matches()) {
                return rule.description;
            }
        }
        return "unknown";
    }
}
