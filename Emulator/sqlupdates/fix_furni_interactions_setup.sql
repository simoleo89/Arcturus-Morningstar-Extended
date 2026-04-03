-- Fix Furni Interactions Command: permission and text keys

-- Command text key (command trigger word)
INSERT INTO `emulator_texts` (`key`, `value`) VALUES
  ('commands.keys.cmd_fix_furni_interactions', 'fixfurni')
ON DUPLICATE KEY UPDATE `value` = VALUES(`value`);

-- Success message
INSERT INTO `emulator_texts` (`key`, `value`) VALUES
  ('commands.succes.cmd_fix_furni_interactions', 'Furniture interaction types have been fixed.')
ON DUPLICATE KEY UPDATE `value` = VALUES(`value`);

-- Description
INSERT INTO `emulator_texts` (`key`, `value`) VALUES
  ('commands.description.cmd_fix_furni_interactions', ':fixfurni <scan|fix|unregistered> - Scan and fix furniture interaction types')
ON DUPLICATE KEY UPDATE `value` = VALUES(`value`);

-- Permission (assign to highest rank - adjust rank_id as needed)
INSERT INTO `permissions` (`id`, `rank_id`, `permission`, `setting`)
SELECT IFNULL(MAX(`id`), 0) + 1, (SELECT MAX(`id`) FROM `ranks`), 'cmd_fix_furni_interactions', '1'
FROM `permissions`
ON DUPLICATE KEY UPDATE `setting` = VALUES(`setting`);
