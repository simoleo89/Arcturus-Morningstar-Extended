package com.arcturus.plugin.furnifix;

import com.eu.habbo.Emulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Core engine that scans items_base for furniture with incorrect or missing
 * interaction_type values and fixes them using 3 layers:
 *   1. Exact name map (highest priority)
 *   2. Regex pattern rules
 *   3. DB auto-learning from existing valid items (lowest priority)
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
        public final Map<String, Integer> fixCountByType;

        public FixSummary(List<FixResult> fixes, List<String> warnings, int totalScanned, int totalFixed, int totalInvalid) {
            this.fixes = Collections.unmodifiableList(fixes);
            this.warnings = Collections.unmodifiableList(warnings);
            this.totalScanned = totalScanned;
            this.totalFixed = totalFixed;
            this.totalInvalid = totalInvalid;

            // Build grouped stats
            Map<String, Integer> counts = new TreeMap<>();
            for (FixResult f : fixes) {
                counts.merge(f.newType, 1, Integer::sum);
            }
            this.fixCountByType = Collections.unmodifiableMap(counts);
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

    // ── Exact name map (highest priority) ─────────────────────────────
    private static final Map<String, String> EXACT_MAP = new LinkedHashMap<>();

    // ── Regex rules (second priority) ─────────────────────────────────
    private static final List<FixRule> RULES = new ArrayList<>();

    // ── DB learned prefixes (third priority, populated at runtime) ─────
    private static Map<String, String> learnedPrefixes = null;

    static {
        // ══════════════════════════════════════════════════════════════
        //  EXACT NAME MAPPINGS
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

        // ── Tag / Run / Rollerskate ──
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

        // ── Snowstorm ──
        EXACT_MAP.put("snowstorm_tree", "snowstorm_tree");
        EXACT_MAP.put("snowstorm_machine", "snowstorm_machine");
        EXACT_MAP.put("snowstorm_pile", "snowstorm_pile");

        // ══════════════════════════════════════════════════════════════
        //  REGEX RULES  (ordered by priority)
        // ══════════════════════════════════════════════════════════════

        // ── Wired: use the item_name itself as interaction_type ──
        // Wired items have item_name == interaction_type (e.g., wf_trg_walks_on_furni)
        // These are handled specially in findCorrectType() via wired self-match

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
        RULES.add(new FixRule("^(gold|silver|bronze)_trophy.*", "trophy", "metal trophy"));

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
        RULES.add(new FixRule("^love_?lock.*", "love_lock", "love lock"));

        // ── Guild Furni ──
        RULES.add(new FixRule("^guild_gate$", "guild_gate", "guild gate"));
        RULES.add(new FixRule("^guild_forum$", "guild_furni", "guild forum"));
        RULES.add(new FixRule("^gld_.*", "guild_furni", "guild furni"));
        RULES.add(new FixRule("^guild_.*", "guild_furni", "guild furni"));

        // ── Clothing ──
        RULES.add(new FixRule("^clothing_.*", "clothing", "clothing item"));

        // ── Gift ──
        RULES.add(new FixRule("^present_(gen|wrap).*", "gift", "gift wrap"));
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
        RULES.add(new FixRule("^(song_disk|musicdisc|disk)_.*", "musicdisc", "music disc"));

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
        RULES.add(new FixRule("^(nest|petnest)_.*", "nest", "pet nest"));
        RULES.add(new FixRule("^petfood\\d+$", "pet_food", "pet food"));
        RULES.add(new FixRule("^pet_food.*", "pet_food", "pet food"));
        RULES.add(new FixRule("^(petdrink|pet_waterbowl).*", "pet_drink", "pet drink"));
        RULES.add(new FixRule("^(pettoy|pet_toy).*", "pet_toy", "pet toy"));
        RULES.add(new FixRule("^pet_tree.*", "pet_tree", "pet tree"));
        RULES.add(new FixRule("^pet_trampoline.*", "pet_trampoline", "pet trampoline"));
        RULES.add(new FixRule("^(breeding|pet_breeding)_.*", "breeding_nest", "breeding nest"));
        RULES.add(new FixRule("^(mnstr_seed|monsterplant_seed).*", "monsterplant_seed", "monsterplant seed"));

        // ── Crackable ──
        RULES.add(new FixRule("^crackable_.*", "crackable", "crackable"));

        // ── FX Box ──
        RULES.add(new FixRule("^(fxbox|fx_box)_.*", "fx_box", "FX box"));

        // ── Effect Items ──
        RULES.add(new FixRule("^effect_toggle.*", "effect_toggle", "effect toggle"));
        RULES.add(new FixRule("^effect_tile.*", "effect_tile", "effect tile"));
        RULES.add(new FixRule("^effect_gate.*", "effect_gate", "effect gate"));
        RULES.add(new FixRule("^effect_giver.*", "effect_giver", "effect giver"));
        RULES.add(new FixRule("^effect_vendingmachine.*", "effect_vendingmachine", "effect vending machine"));

        // ── Gym Equipment ──
        RULES.add(new FixRule("(^|.+_)gym(_|$).*", "gym_equipment", "gym equipment"));

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
        RULES.add(new FixRule("^(wf_highscore|highscore_).*", "wf_highscore", "wired highscore"));

        // ── Football ──
        RULES.add(new FixRule("^footballgate.*", "football_gate", "football gate"));
        RULES.add(new FixRule("^football_goal_(blue|green|red|yellow).*", "football_goal_$1", "football goal"));
        RULES.add(new FixRule("^football_counter_(blue|green|red|yellow).*", "football_counter_$1", "football counter"));

        // ── Hand Items ──
        RULES.add(new FixRule("^handitem_tile.*", "handitem_tile", "hand item tile"));
        RULES.add(new FixRule("^handitem_.*", "handitem", "hand item"));

        // ── Information Terminal ──
        RULES.add(new FixRule("^(info|information)_terminal.*", "information_terminal", "information terminal"));

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

        // ── Misc ──
        RULES.add(new FixRule("^viking_cotie.*", "viking_cotie", "viking cotie"));
        RULES.add(new FixRule("^trap_.*", "trap", "trap"));
        RULES.add(new FixRule("^blackhole.*", "blackhole", "black hole"));
        RULES.add(new FixRule("^room_o_matic.*", "room_o_matic", "room-o-matic"));
        RULES.add(new FixRule("^color_?plate.*", "colorplate", "color plate"));
        RULES.add(new FixRule("^tile_fxprovider.*", "tile_fxprovider_nfs", "tile FX provider"));
        RULES.add(new FixRule("^tile_walkmagic.*", "tile_walkmagic", "tile walk magic"));
    }

    // ══════════════════════════════════════════════════════════════════
    //  DB AUTO-LEARNING
    // ══════════════════════════════════════════════════════════════════

    /**
     * Learn item_name prefixes from items that already have valid interaction types.
     * E.g., if "roller_blue"=roller, "roller_red"=roller exist, learns "roller_" -> roller.
     */
    public static Map<String, String> learnFromDatabase() {
        Set<String> validTypes = getValidInteractionTypes();
        // prefix -> type counts
        Map<String, Map<String, Integer>> prefixVotes = new HashMap<>();

        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery(
                     "SELECT item_name, interaction_type FROM items_base " +
                     "WHERE interaction_type != '' AND interaction_type != 'default' " +
                     "ORDER BY item_name")) {

            while (set.next()) {
                String name = set.getString("item_name").toLowerCase().trim();
                String type = set.getString("interaction_type").toLowerCase().trim();

                if (!validTypes.contains(type)) continue;

                // Extract prefix (everything before last _ or digits)
                String prefix = extractPrefix(name);
                if (prefix != null && prefix.length() >= 3) {
                    prefixVotes.computeIfAbsent(prefix, k -> new HashMap<>())
                              .merge(type, 1, Integer::sum);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error learning from database", e);
        }

        // Build prefix map: only keep prefixes where one type has >= 2 votes and > 80% majority
        Map<String, String> learned = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : prefixVotes.entrySet()) {
            Map<String, Integer> votes = entry.getValue();
            int total = votes.values().stream().mapToInt(Integer::intValue).sum();

            if (total < 2) continue;

            votes.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .ifPresent(best -> {
                        double ratio = (double) best.getValue() / total;
                        if (ratio >= 0.8 && !best.getKey().equals("default")) {
                            learned.put(entry.getKey(), best.getKey());
                        }
                    });
        }

        LOGGER.info("[FurniInteractionFixer] Learned {} prefixes from database.", learned.size());
        return learned;
    }

    /**
     * Extract the meaningful prefix from an item name.
     * "roller_blue" -> "roller_", "edice_hc2" -> "edice_"
     */
    private static String extractPrefix(String name) {
        int lastUnderscore = name.lastIndexOf('_');
        if (lastUnderscore > 0) {
            return name.substring(0, lastUnderscore + 1);
        }
        // Try stripping trailing digits: "petfood10" -> "petfood"
        String stripped = name.replaceAll("\\d+$", "");
        if (!stripped.equals(name) && stripped.length() >= 3) {
            return stripped;
        }
        return null;
    }

    /**
     * Refresh learned prefixes from DB.
     */
    public static void refreshLearning() {
        learnedPrefixes = learnFromDatabase();
    }

    // ══════════════════════════════════════════════════════════════════
    //  PUBLIC API
    // ══════════════════════════════════════════════════════════════════

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
        ensureLearned();
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

                if (!currentType.isEmpty() && !currentType.equals("default") && validTypes.contains(currentType)) {
                    continue;
                }

                String[] result = findCorrectTypeWithRule(itemName, validTypes);

                if (result != null && !result[0].equals(currentType)) {
                    fixes.add(new FixResult(id, itemName, currentType, result[0], result[1]));
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
     * Scan and apply fixes to the database.
     */
    public static FixSummary fix() {
        FixSummary scanResult = scan();

        if (scanResult.fixes.isEmpty()) {
            LOGGER.info("[FurniInteractionFixer] No fixes needed.");
            return scanResult;
        }

        int applied = applyFixes(scanResult.fixes);
        return new FixSummary(scanResult.fixes, scanResult.warnings, scanResult.totalScanned, applied, scanResult.totalInvalid);
    }

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

    public static List<FixResult> findUnregisteredTypes() {
        ensureLearned();
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
                    String itemName = set.getString("item_name");
                    String[] result = findCorrectTypeWithRule(itemName, validTypes);
                    results.add(new FixResult(
                            set.getInt("id"),
                            itemName,
                            currentType,
                            result != null ? result[0] : "default",
                            result != null ? result[1] : "no rule"
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error finding unregistered types", e);
        }

        return results;
    }

    public static FixSummary fixUnregistered() {
        List<FixResult> unregistered = findUnregisteredTypes();

        if (unregistered.isEmpty()) {
            LOGGER.info("[FurniInteractionFixer] No unregistered interaction types found.");
            return new FixSummary(unregistered, Collections.emptyList(), 0, 0, 0);
        }

        Set<String> validTypes = getValidInteractionTypes();
        List<FixResult> toApply = new ArrayList<>();

        for (FixResult item : unregistered) {
            String targetType = validTypes.contains(item.newType.toLowerCase()) ? item.newType : "default";
            toApply.add(new FixResult(item.itemId, item.itemName, item.oldType, targetType, item.rule));
        }

        int applied = applyFixes(toApply);
        return new FixSummary(toApply, Collections.emptyList(), unregistered.size(), applied, 0);
    }

    public static FixSummary fixAll() {
        FixSummary defaultFixes = fix();
        FixSummary unregFixes = fixUnregistered();

        List<FixResult> allFixes = new ArrayList<>(defaultFixes.fixes);
        allFixes.addAll(unregFixes.fixes);

        List<String> allWarnings = new ArrayList<>(defaultFixes.warnings);
        allWarnings.addAll(unregFixes.warnings);

        return new FixSummary(allFixes, allWarnings,
                defaultFixes.totalScanned,
                defaultFixes.totalFixed + unregFixes.totalFixed,
                defaultFixes.totalInvalid);
    }

    /**
     * Get stats: count of items per interaction_type in the DB.
     */
    public static Map<String, Integer> getTypeStats() {
        Map<String, Integer> stats = new TreeMap<>();
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet set = stmt.executeQuery(
                     "SELECT interaction_type, COUNT(*) as cnt FROM items_base " +
                     "GROUP BY interaction_type ORDER BY cnt DESC")) {
            while (set.next()) {
                stats.put(set.getString("interaction_type"), set.getInt("cnt"));
            }
        } catch (SQLException e) {
            LOGGER.error("[FurniInteractionFixer] Error getting type stats", e);
        }
        return stats;
    }

    // ══════════════════════════════════════════════════════════════════
    //  INTERNAL
    // ══════════════════════════════════════════════════════════════════

    private static void ensureLearned() {
        if (learnedPrefixes == null) {
            refreshLearning();
        }
    }

    /**
     * Find correct type with 4 layers:
     *   1. Exact name map
     *   2. Wired self-match (item_name IS a registered interaction type)
     *   3. Regex rules
     *   4. DB learned prefixes
     *
     * @return [type, ruleDescription] or null
     */
    private static String[] findCorrectTypeWithRule(String itemName, Set<String> validTypes) {
        String lower = itemName.toLowerCase().trim();

        // 1. Exact map
        String exact = EXACT_MAP.get(lower);
        if (exact != null && validTypes.contains(exact)) {
            return new String[]{exact, "exact map"};
        }

        // 2. Wired self-match: if the item_name itself is a valid registered type, use it
        //    This handles all wf_trg_*, wf_act_*, wf_cnd_*, wf_xtra_*, wf_slc_* items
        if (validTypes.contains(lower)) {
            return new String[]{lower, "self-match (name = registered type)"};
        }

        // 3. Regex rules
        for (FixRule rule : RULES) {
            if (rule.namePattern.matcher(lower).matches()) {
                if (validTypes.contains(rule.correctType)) {
                    return new String[]{rule.correctType, rule.description};
                }
            }
        }

        // 4. DB learned prefixes
        if (learnedPrefixes != null) {
            String prefix = extractPrefix(lower);
            if (prefix != null) {
                String learned = learnedPrefixes.get(prefix);
                if (learned != null && validTypes.contains(learned)) {
                    return new String[]{learned, "DB learned (" + prefix + "* -> " + learned + ")"};
                }
            }
        }

        return null;
    }

    // Keep legacy method for backward compatibility
    static String findCorrectType(String itemName) {
        String[] result = findCorrectTypeWithRule(itemName, getValidInteractionTypes());
        return result != null ? result[0] : null;
    }

    private static int applyFixes(List<FixResult> fixes) {
        if (fixes.isEmpty()) return 0;

        int applied = 0;
        try (Connection connection = Emulator.getDatabase().getDataSource().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "UPDATE items_base SET interaction_type = ? WHERE id = ?")) {

            for (FixResult fix : fixes) {
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
        return applied;
    }
}
