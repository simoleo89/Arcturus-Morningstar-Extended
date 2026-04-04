-- FurniInteractionFixer Plugin: permission and text keys
-- Run this SQL ONCE before using the plugin.

-- Command trigger word
INSERT INTO `emulator_texts` (`key`, `value`) VALUES
  ('commands.keys.cmd_fix_furni_interactions', 'fixfurni')
ON DUPLICATE KEY UPDATE `value` = VALUES(`value`);

-- Permission for highest rank (adjust rank_id as needed)
INSERT IGNORE INTO `permissions` (`id`, `rank_id`, `permission`, `setting`)
SELECT IFNULL(MAX(`id`), 0) + 1, (SELECT MAX(`id`) FROM `ranks`), 'cmd_fix_furni_interactions', '1'
FROM `permissions`;
