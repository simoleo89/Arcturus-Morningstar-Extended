package com.arcturus.plugin.furnifix;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Core engine that scans items_base for furniture with incorrect or missing
 * interaction_type values and fixes them based on known naming patterns,
 * exact name maps, and configurable rules.
 */
public class InteractionTypeFixer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InteractionTypeFixer.class);

    // ── Result types ──────────────────────────────────────────────────

    public static class FixResult {
        public final int itemId;
        public final String itemName;
        public final String oldType;
        public final String newType;
        public final String rule;

        public FixResult(int itemId, String itemName, String oldType, String newType, String rule) {
            this.itemId = itemId;
            this.itemName = itemName;
            this.oldType = oldType;
            this.newType = newType;
            this.rule = rule;
        }

        @Override
        public String toString() {
            return String.format("[%d] %s: '%s' -> '%s' (%s)", itemId, itemName, oldType, newType, rule);
        }
    }

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

    // ── Rule definition ───────────────────────────────────────────────

    private static class FixRule {
        final Pattern namePattern;
        final String correctType;
        final String description;

        FixRule(String regex, String correctType, String description) {
            this.namePattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            this.correctType = correctType;
            this.description = description;
        }
    }

    // ── Exact name → interaction_type map (highest priority) ─────────
    // These come from the official Habbo furnidata and known emulator items.
    private static final Map<String, String> EXACT_MAP = new LinkedHashMap<>();

    // ── Regex rules (second priority, first match wins) ──────────────
    private static final List<FixRule> RULES = new ArrayList<>();

    static {
        // ══════════════════════════════════════════════════════════════
        //  EXACT NAME MAPPINGS  (furnidata / known items)
        // ══════════════════════════════════════════════════════════════

        // ── Post-it / Sticky ──
        EXACT_MAP.put("post.it", "postit");
        EXACT_MAP.put("post.it.vd", "postit");
        EXACT_MAP.put("post_it", "postit");
        EXACT_MAP.put("stickienote", "postit");

        // ── Rollers ──
        EXACT_MAP.put("roller", "roller");
        EXACT_MAP.put("rollercc", "roller");

        // ── Dimmer / Moodlight ──
        EXACT_MAP.put("dimmer", "dimmer");
        EXACT_MAP.put("roomdimmer", "dimmer");

        // ── Dice ──
        EXACT_MAP.put("dice", "dice");
        EXACT_MAP.put("edice", "dice");
        EXACT_MAP.put("edicehc", "dice");

        // ── Color Wheel ──
        EXACT_MAP.put("colorwheel", "colorwheel");
        EXACT_MAP.put("colour_wheel", "colorwheel");
        EXACT_MAP.put("wheel", "colorwheel");

        // ── Teleport ──
        EXACT_MAP.put("teleport_door", "teleport");
        EXACT_MAP.put("teleport_pad", "teleporttile");

        // ── Stack Helper ──
        EXACT_MAP.put("stack_helper", "stack_helper");

        // ── Water ──
        EXACT_MAP.put("water", "water");
        EXACT_MAP.put("water_item", "water_item");

        // ── Background Toner ──
        EXACT_MAP.put("bg_toner", "background_toner");
        EXACT_MAP.put("roombg_toner", "background_toner");

        // ── Cannon ──
        EXACT_MAP.put("cannon", "cannon");

        // ── Pyramid ──
        EXACT_MAP.put("pyramid", "pyramid");
        EXACT_MAP.put("egypt_pyramid", "pyramid");

        // ── Football ──
        EXACT_MAP.put("football", "football");
        EXACT_MAP.put("ball", "football");

        // ── Jukebox / Sound Machine ──
        EXACT_MAP.put("jukebox", "jukebox");
        EXACT_MAP.put("jukebox_big", "jukebox");
        EXACT_MAP.put("sound_machine", "jukebox");
        EXACT_MAP.put("soundmachine", "jukebox");

        // ── Vote Counter ──
        EXACT_MAP.put("vote_counter", "vote_counter");

        // ── Battle Banzai ──
        EXACT_MAP.put("bb_patch", "battlebanzai_tile");
        EXACT_MAP.put("bb_rnd_tele", "battlebanzai_random_teleport");
        EXACT_MAP.put("bb_random_teleport", "battlebanzai_random_teleport");
        EXACT_MAP.put("bb_blue_gate", "battlebanzai_gate_blue");
        EXACT_MAP.put("bb_green_gate", "battlebanzai_gate_green");
        EXACT_MAP.put("bb_red_gate", "battlebanzai_gate_red");
        EXACT_MAP.put("bb_yellow_gate", "battlebanzai_gate_yellow");
        EXACT_MAP.put("bb_blue_score", "battlebanzai_counter_blue");
        EXACT_MAP.put("bb_green_score", "battlebanzai_counter_green");
        EXACT_MAP.put("bb_red_score", "battlebanzai_counter_red");
        EXACT_MAP.put("bb_yellow_score", "battlebanzai_counter_yellow");
        EXACT_MAP.put("bb_sphere", "battlebanzai_sphere");
        EXACT_MAP.put("bb_puck", "battlebanzai_puck");

        // ── Freeze ──
        EXACT_MAP.put("freeze_block", "freeze_block");
        EXACT_MAP.put("freeze_tile", "freeze_tile");
        EXACT_MAP.put("freeze_exit", "freeze_exit");
        EXACT_MAP.put("freeze_blue_gate", "freeze_gate_blue");
        EXACT_MAP.put("freeze_green_gate", "freeze_gate_green");
        EXACT_MAP.put("freeze_red_gate", "freeze_gate_red");
        EXACT_MAP.put("freeze_yellow_gate", "freeze_gate_yellow");
        EXACT_MAP.put("freeze_blue_score", "freeze_counter_blue");
        EXACT_MAP.put("freeze_green_score", "freeze_counter_green");
        EXACT_MAP.put("freeze_red_score", "freeze_counter_red");
        EXACT_MAP.put("freeze_yellow_score", "freeze_counter_yellow");

        // ── Ice Tag / Bunny Run / Rollerskate ──
        EXACT_MAP.put("icetag_pole", "icetag_pole");
        EXACT_MAP.put("icetag_field", "icetag_field");
        EXACT_MAP.put("bunnyrun_pole", "bunnyrun_pole");
        EXACT_MAP.put("bunnyrun_field", "bunnyrun_field");
        EXACT_MAP.put("rollerskate_field", "rollerskate_field");

        // ── Game Timer ──
        EXACT_MAP.put("game_timer", "game_timer");
        EXACT_MAP.put("gametimer", "game_timer");

        // ── Totems ──
        EXACT_MAP.put("totem_leg", "totem_leg");
        EXACT_MAP.put("totem_head", "totem_head");
        EXACT_MAP.put("totem_planet", "totem_planet");

        // ── Snowstorm (placeholder) ──
        EXACT_MAP.put("snowstorm_tree", "snowstorm_tree");
        EXACT_MAP.put("snowstorm_machine", "snowstorm_machine");
        EXACT_MAP.put("snowstorm_pile", "snowstorm_pile");

        // ══════════════════════════════════════════════════════════════
        //  REGEX RULES  (pattern-based, ordered by priority)
        // ══════════════════════════════════════════════════════════════

        // ── Rollers ──
        RULES.add(new FixRule("^roller_.*", "roller", "roller (prefix)"));
        RULES.add(new FixRule(".*_roller$", "roller", "roller (suffix)"));
        RULES.add(new FixRule("^rollercc.*", "roller", "roller cc"));

        // ── Teleports ──
        RULES.add(new FixRule(".*_teleport$", "teleport", "teleport (suffix)"));
        RULES.add(new FixRule("^teleport_.*", "teleport", "teleport (prefix)"));
        RULES.add(new FixRule(".*_tele$", "teleport", "teleport (short suffix)"));

        // ── Dice ──
        RULES.add(new FixRule("^edice_.*", "dice", "external dice"));
        RULES.add(new FixRule("^dice_.*", "dice", "dice (prefix)"));
        RULES.add(new FixRule(".*_dice$", "dice", "dice (suffix)"));
        RULES.add(new FixRule("^edicehc.*", "dice", "HC dice"));

        // ── Gates ──
        RULES.add(new FixRule(".*oneway.*gate.*", "onewaygate", "one-way gate"));
        RULES.add(new FixRule(".*one_way.*gate.*", "onewaygate", "one-way gate"));
        RULES.add(new FixRule("^gate_.*", "gate", "gate (prefix)"));
        RULES.add(new FixRule(".*_gate$", "gate", "gate (suffix)"));

        // ── Trophies ──
        RULES.add(new FixRule("^trophy_.*", "trophy", "trophy"));
        RULES.add(new FixRule("^prizetrophy_.*", "trophy", "prize trophy"));
        RULES.add(new FixRule(".*_trophy$", "trophy", "trophy (suffix)"));
        RULES.add(new FixRule("^gold_trophy.*", "trophy", "gold trophy"));
        RULES.add(new FixRule("^silver_trophy.*", "trophy", "silver trophy"));
        RULES.add(new FixRule("^bronze_trophy.*", "trophy", "bronze trophy"));

        // ── Mannequins ──
        RULES.add(new FixRule("^mannequin_.*", "mannequin", "mannequin"));
        RULES.add(new FixRule(".*_mannequin$", "mannequin", "mannequin (suffix)"));

        // ── Post-it / Sticky ──
        RULES.add(new FixRule("^postit.*", "postit", "post-it"));
        RULES.add(new FixRule("^sticky_pole.*", "sticky_pole", "sticky pole"));

        // ── Dimmer / Moodlight ──
        RULES.add(new FixRule("^roomdimmer.*", "dimmer", "room dimmer"));

        // ── Vending Machines ──
        RULES.add(new FixRule(".*vend(ing)?_?machine.*", "vendingmachine", "vending machine"));
        RULES.add(new FixRule(".*_vend$", "vendingmachine", "vending (suffix)"));
        RULES.add(new FixRule("^vend_.*", "vendingmachine", "vending (prefix)"));

        // ── Pressure Plates ──
        RULES.add(new FixRule(".*pressure_?plate.*", "pressureplate", "pressure plate"));
        RULES.add(new FixRule(".*ringplate.*", "pressureplate", "ring plate"));

        // ── Multi-height ──
        RULES.add(new FixRule(".*_multiheight$", "multiheight", "multi-height"));

        // ── Background Toner ──
        RULES.add(new FixRule("^roombg_.*", "background_toner", "room background toner"));

        // ── Badge Display ──
        RULES.add(new FixRule("^badge_display.*", "badge_display", "badge display"));
        RULES.add(new FixRule("^badgedisplay.*", "badge_display", "badge display"));

        // ── Love Lock ──
        RULES.add(new FixRule("^love_lock.*", "love_lock", "love lock"));
        RULES.add(new FixRule("^lovelock.*", "love_lock", "love lock"));

        // ── Guild Furni ──
        RULES.add(new FixRule("^guild_gate$", "guild_gate", "guild gate"));
        RULES.add(new FixRule("^guild_forum$", "guild_furni", "guild forum"));
        RULES.add(new FixRule("^gld_.*", "guild_furni", "guild furni"));
        RULES.add(new FixRule("^guild_.*", "guild_furni", "guild furni"));

        // ── Clothing ──
        RULES.add(new FixRule("^clothing_.*", "clothing", "clothing item"));

        // ── Gift ──
        RULES.add(new FixRule("^present_gen.*", "gift", "gift wrap"));
        RULES.add(new FixRule("^present_wrap.*", "gift", "gift wrap"));
        RULES.add(new FixRule("^present_.*", "gift", "gift/present"));

        // ── Puzzle Box ──
        RULES.add(new FixRule("^puzzlebox_.*", "puzzle_box", "puzzle box"));

        // ── Hopper ──
        RULES.add(new FixRule("^hopper_.*", "hopper", "hopper"));
        RULES.add(new FixRule("^costumehopper.*", "costume_hopper", "costume hopper"));

        // ── Fireworks ──
        RULES.add(new FixRule("^firework.*", "fireworks", "fireworks"));

        // ── Jukebox / Music ──
        RULES.add(new FixRule("^jukebox.*", "jukebox", "jukebox"));
        RULES.add(new FixRule("^sound_machine.*", "jukebox", "sound machine"));
        RULES.add(new FixRule("^song_disk_.*", "musicdisc", "music disc"));
        RULES.add(new FixRule("^musicdisc_.*", "musicdisc", "music disc"));
        RULES.add(new FixRule("^disk_.*", "musicdisc", "music disc"));

        // ── YouTube ──
        RULES.add(new FixRule("^youtube_.*", "youtube", "youtube tv"));

        // ── External Image / Room Ads ──
        RULES.add(new FixRule("^external_image.*", "external_image", "external image"));
        RULES.add(new FixRule("^ads_.*_image$", "external_image", "external image (ads)"));
        RULES.add(new FixRule("^ads_mpu_.*", "ads_bg", "room ads background"));

        // ── Tent ──
        RULES.add(new FixRule("^tent_.*", "tent", "tent"));
        RULES.add(new FixRule(".*_tent$", "tent", "tent (suffix)"));

        // ── Water / Pool ──
        RULES.add(new FixRule("^pool_.*", "water", "pool water"));
        RULES.add(new FixRule(".*_pool$", "water", "pool (suffix)"));

        // ── Pet Items ──
        RULES.add(new FixRule("^nest_.*", "nest", "pet nest"));
        RULES.add(new FixRule("^petnest_.*", "nest", "pet nest"));
        RULES.add(new FixRule("^petfood\\d+$", "pet_food", "pet food"));
        RULES.add(new FixRule("^pet_food.*", "pet_food", "pet food"));
        RULES.add(new FixRule("^petdrink.*", "pet_drink", "pet drink"));
        RULES.add(new FixRule("^pet_waterbowl.*", "pet_drink", "pet water bowl"));
        RULES.add(new FixRule("^pettoy_.*", "pet_toy", "pet toy"));
        RULES.add(new FixRule("^pet_toy.*", "pet_toy", "pet toy"));
        RULES.add(new FixRule("^pet_tree.*", "pet_tree", "pet tree"));
        RULES.add(new FixRule("^pet_trampoline.*", "pet_trampoline", "pet trampoline"));
        RULES.add(new FixRule("^breeding_.*", "breeding_nest", "breeding nest"));
        RULES.add(new FixRule("^pet_breeding.*", "breeding_nest", "breeding nest"));
        RULES.add(new FixRule("^mnstr_seed.*", "monsterplant_seed", "monsterplant seed"));
        RULES.add(new FixRule("^monsterplant_seed.*", "monsterplant_seed", "monsterplant seed"));

        // ── Crackable ──
        RULES.add(new FixRule("^crackable_.*", "crackable", "crackable"));

        // ── FX Box ──
        RULES.add(new FixRule("^fxbox_.*", "fx_box", "FX box"));
        RULES.add(new FixRule("^fx_box_.*", "fx_box", "FX box"));

        // ── Effect Toggle ──
        RULES.add(new FixRule("^effect_toggle.*", "effect_toggle", "effect toggle"));

        // ── Effect Tile ──
        RULES.add(new FixRule("^effect_tile.*", "effect_tile", "effect tile"));

        // ── Gym Equipment ──
        RULES.add(new FixRule(".*_gym_.*", "gym_equipment", "gym equipment"));
        RULES.add(new FixRule("^gym_.*", "gym_equipment", "gym equipment"));

        // ── Rentable Space ──
        RULES.add(new FixRule("^rentable_space.*", "rentable_space", "rentable space"));

        // ── Mute/Build Area ──
        RULES.add(new FixRule("^mutearea.*", "mutearea", "mute area"));
        RULES.add(new FixRule("^buildarea.*", "buildarea", "build area"));

        // ── Random State ──
        RULES.add(new FixRule("^random_state.*", "random_state", "random state"));

        // ── Talking Furni ──
        RULES.add(new FixRule("^talking_furni.*", "talking_furni", "talking furniture"));

        // ── Highscore ──
        RULES.add(new FixRule("^wf_highscore.*", "wf_highscore", "wired highscore"));
        RULES.add(new FixRule("^highscore_.*", "wf_highscore", "highscore board"));

        // ── Football ──
        RULES.add(new FixRule("^footballgate.*", "football_gate", "football gate"));
        RULES.add(new FixRule("^football_goal_blue.*", "football_goal_blue", "football goal blue"));
        RULES.add(new FixRule("^football_goal_green.*", "football_goal_green", "football goal green"));
        RULES.add(new FixRule("^football_goal_red.*", "football_goal_red", "football goal red"));
        RULES.add(new FixRule("^football_goal_yellow.*", "football_goal_yellow", "football goal yellow"));
        RULES.add(new FixRule("^football_counter_blue.*", "football_counter_blue", "football counter blue"));
        RULES.add(new FixRule("^football_counter_green.*", "football_counter_green", "football counter green"));
        RULES.add(new FixRule("^football_counter_red.*", "football_counter_red", "football counter red"));
        RULES.add(new FixRule("^football_counter_yellow.*", "football_counter_yellow", "football counter yellow"));

        // ── Hand Items ──
        RULES.add(new FixRule("^handitem_.*", "handitem", "hand item"));
        RULES.add(new FixRule("^handitem_tile.*", "handitem_tile", "hand item tile"));

        // ── Information Terminal ──
        RULES.add(new FixRule("^info_terminal.*", "information_terminal", "information terminal"));
        RULES.add(new FixRule("^information_terminal.*", "information_terminal", "information terminal"));

        // ── Obstacle ──
        RULES.add(new FixRule("^obstacle_.*", "obstacle", "pet obstacle"));

        // ── Snowboard Slope ──
        RULES.add(new FixRule("^snowb_slope.*", "snowboard_slope", "snowboard slope"));

        // ── Switch ──
        RULES.add(new FixRule("^switch_.*", "switch", "switch"));

        // ── Club Items ──
        RULES.add(new FixRule("^club_gate.*", "club_gate", "habbo club gate"));
        RULES.add(new FixRule("^club_hopper.*", "club_hopper", "habbo club hopper"));
        RULES.add(new FixRule("^club_teleport.*", "club_teleporttile", "habbo club teleport"));

        // ── Effect Gate ──
        RULES.add(new FixRule("^effect_gate.*", "effect_gate", "effect gate"));

        // ── Viking Cotie ──
        RULES.add(new FixRule("^viking_cotie.*", "viking_cotie", "viking cotie"));

        // ── Trap ──
        RULES.add(new FixRule("^trap_.*", "trap", "trap"));

        // ── Black Hole ──
        RULES.add(new FixRule("^blackhole.*", "blackhole", "black hole"));

        // ── Room-o-matic ──
        RULES.add(new FixRule("^room_o_matic.*", "room_o_matic", "room-o-matic"));

        // ── Wired (catch-all for items that match wired prefixes) ──
        RULES.add(new FixRule("^wf_trg_.*", "default", "wired trigger (needs specific type)"));
        RULES.add(new FixRule("^wf_act_.*", "default", "wired effect (needs specific type)"));
        RULES.add(new FixRule("^wf_cnd_.*", "default", "wired condition (needs specific type)"));
        RULES.add(new FixRule("^wf_xtra_.*", "default", "wired extra (needs specific type)"));
        RULES.add(new FixRule("^wf_slc_.*", "default", "wired selector (needs specific type)"));

        // ── Effect Giver / Vending (effect-based) ──
        RULES.add(new FixRule("^effect_giver.*", "effect_giver", "effect giver"));
        RULES.add(new FixRule("^effect_vendingmachine.*", "effect_vendingmachine", "effect vending machine"));
    }

    // ══════════════════════════════════════════════════════════════════
    //  PUBLIC API
    // ══════════════════════════════════════════════════════════════════

    /**
     * Get all valid interaction type names from ItemManager.
     */
    public static Set<String> getValidInteractionTypes() {
        Set<String> valid = new HashSet<>();
        for (String name : Emulator.getGameEnvironment().getItemManager().getInteractionList()) {
            valid.add(name.toLowerCase());
        }
        return valid;
    }

    /**
     * Scan items_base for incorrect interaction types. Does NOT modify the DB.
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

                // Skip items that already have a valid non-default type
                if (!currentType.isEmpty() && !currentType.equals("default") && validTypes.contains(currentType)) {
                    continue;
                }

                String suggestedType = findCorrectType(itemName);

                if (suggestedType != null && !suggestedType.equals(currentType)) {
                    if (validTypes.contains(suggestedType)) {
                        fixes.add(new FixResult(id, itemName, currentType, suggestedType, getMatchedRule(itemName)));
                    } else {
                        warnings.add(String.format("[%d] %s: suggested '%s' but not registered in emulator", id, itemName, suggestedType));
                    }
                } else if (currentType.isEmpty()) {
                    totalInvalid++;
                    warnings.add(String.format("[%d] %s: empty interaction_type, no rule match", id, itemName));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error scanning items_base", e);
        }

        return new FixSummary(fixes, warnings, totalScanned, fixes.size(), totalInvalid);
    }

    /**
     * Scan and apply all fixes to the database.
     */
    public static FixSummary fix() {
        FixSummary scanResult = scan();

        if (scanResult.fixes.isEmpty()) {
            LOGGER.info("[FurniInteractionFixer] No fixes needed.");
            return scanResult;
        }

        int applied = 0;
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE items_base SET interaction_type = ? WHERE id = ?")) {

            for (FixResult fix : scanResult.fixes) {
                stmt.setString(1, fix.newType);
                stmt.setInt(2, fix.itemId);
                stmt.addBatch();
                applied++;
            }

            stmt.executeBatch();
            LOGGER.info("[FurniInteractionFixer] Applied {} fixes.", applied);
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error applying fixes", e);
        }

        return new FixSummary(scanResult.fixes, scanResult.warnings, scanResult.totalScanned, applied, scanResult.totalInvalid);
    }

    /**
     * Fix a single item by ID with a specific interaction type.
     */
    public static boolean fixSingle(int itemId, String newType) {
        Set<String> validTypes = getValidInteractionTypes();
        if (!validTypes.contains(newType.toLowerCase())) {
            LOGGER.warn("[FurniInteractionFixer] '{}' is not a registered interaction type.", newType);
            return false;
        }

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE items_base SET interaction_type = ? WHERE id = ?")) {
            stmt.setString(1, newType);
            stmt.setInt(2, itemId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                LOGGER.info("[FurniInteractionFixer] Fixed item {} -> '{}'", itemId, newType);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error fixing single item", e);
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
                            suggested != null ? getMatchedRule(set.getString("item_name")) : "no rule"
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error finding unregistered types", e);
        }

        return results;
    }

    /**
     * Fix all items that have unregistered interaction types.
     * Items with a matching rule get the suggested type; items without a rule get "default".
     *
     * @return summary with all changes applied
     */
    public static FixSummary fixUnregistered() {
        List<FixResult> unregistered = findUnregisteredTypes();

        if (unregistered.isEmpty()) {
            LOGGER.info("[FurniInteractionFixer] No unregistered interaction types found.");
            return new FixSummary(unregistered, Collections.emptyList(), 0, 0, 0);
        }

        Set<String> validTypes = getValidInteractionTypes();
        List<FixResult> applied = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE items_base SET interaction_type = ? WHERE id = ?")) {

            for (FixResult item : unregistered) {
                String targetType = item.newType;

                // Verify the target type is valid (it should be, but double-check)
                if (!validTypes.contains(targetType.toLowerCase())) {
                    targetType = "default";
                }

                stmt.setString(1, targetType);
                stmt.setInt(2, item.itemId);
                stmt.addBatch();
                applied.add(new FixResult(item.itemId, item.itemName, item.oldType, targetType, item.rule));
            }

            stmt.executeBatch();
            LOGGER.info("[FurniInteractionFixer] Fixed {} items with unregistered types.", applied.size());
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error fixing unregistered types", e);
        }

        return new FixSummary(applied, warnings, unregistered.size(), applied.size(), 0);
    }

    /**
     * Fix ALL issues: both default/empty items AND unregistered types.
     *
     * @return combined summary
     */
    public static FixSummary fixAll() {
        // 1. Fix default/empty items
        FixSummary defaultFixes = fix();

        // 2. Fix unregistered types
        FixSummary unregFixes = fixUnregistered();

        // Combine results
        List<FixResult> allFixes = new ArrayList<>(defaultFixes.fixes);
        allFixes.addAll(unregFixes.fixes);

        List<String> allWarnings = new ArrayList<>(defaultFixes.warnings);
        allWarnings.addAll(unregFixes.warnings);

        return new FixSummary(
                allFixes,
                allWarnings,
                defaultFixes.totalScanned,
                defaultFixes.totalFixed + unregFixes.totalFixed,
                defaultFixes.totalInvalid
        );
    }

    // ══════════════════════════════════════════════════════════════════
    //  INTERNAL MATCHING
    // ══════════════════════════════════════════════════════════════════

    /**
     * Find the correct interaction type for an item name.
     * Priority: exact map > regex rules.
     */
    static String findCorrectType(String itemName) {
        String lower = itemName.toLowerCase().trim();

        // 1. Exact match (highest priority)
        String exact = EXACT_MAP.get(lower);
        if (exact != null) return exact;

        // 2. Regex rules (first match wins)
        for (FixRule rule : RULES) {
            if (rule.namePattern.matcher(lower).matches()) {
                return rule.correctType;
            }
        }

        return null;
    }

    private static String getMatchedRule(String itemName) {
        String lower = itemName.toLowerCase().trim();

        if (EXACT_MAP.containsKey(lower)) {
            return "exact map";
        }

        for (FixRule rule : RULES) {
            if (rule.namePattern.matcher(lower).matches()) {
                return rule.description;
            }
        }
        return "unknown";
    }
}
