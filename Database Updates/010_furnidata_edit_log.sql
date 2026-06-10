-- 020_furnidata_edit_log.sql
-- Audit trail for furnidata name/description edits made through the furni editor,
-- plus config keys for the editor write path. NOTE: *.enabled keys elsewhere are
-- read via Boolean.parseBoolean (true/false), but these two are numeric.
CREATE TABLE IF NOT EXISTS `furnidata_edit_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `classname` varchar(255) NOT NULL,
  `action` enum('edit','revert') NOT NULL DEFAULT 'edit',
  `old_name` varchar(256) NOT NULL DEFAULT '',
  `new_name` varchar(256) NOT NULL DEFAULT '',
  `old_description` varchar(256) NOT NULL DEFAULT '',
  `new_description` varchar(256) NOT NULL DEFAULT '',
  `timestamp` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `idx_classname` (`classname`),
  INDEX `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO `emulator_settings` (`key`,`value`) VALUES
('items.furnidata.edit.backup.keep','10'),
('items.furnidata.edit.ratelimit.ms','2000'),
-- Server-authoritative furni names (source of truth = furnidata JSON)
('items.furnidata.names.enabled','true'),
('items.furnidata.path',''),
('items.furnidata.max.bytes','67108864'),
-- Live-reload watcher
('items.furnidata.watch.enabled','true'),
('items.furnidata.watch.debounce.ms','750'),
('items.furnidata.watch.min.interval.ms','5000'),
('items.furnidata.delta.cap','500'),
-- Furni editor: import official names/descriptions from Habbo
('furni.editor.import.url','https://www.habbo.com/gamedata/furnidata_json/1'),
('furni.editor.import.cache.ms','600000');

START TRANSACTION;
DROP TEMPORARY TABLE IF EXISTS cleanup_furnidata_settings;
CREATE TEMPORARY TABLE cleanup_furnidata_settings (
    `key` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL PRIMARY KEY
);
INSERT INTO cleanup_furnidata_settings (`key`) VALUES
    ('items.furnidata.names.enabled'),
    ('items.furnidata.path'),
    ('items.furnidata.max.bytes'),
    ('items.furnidata.watch.enabled'),
    ('items.furnidata.watch.debounce.ms'),
    ('items.furnidata.watch.min.interval.ms'),
    ('items.furnidata.delta.cap'),
    ('furni.editor.import.url'),
    ('furni.editor.import.cache.ms');

-- Preview rows that will be removed.
SELECT es.`key`, es.`value`
FROM emulator_settings es
JOIN cleanup_furnidata_settings cfs ON cfs.`key` = es.`key`
ORDER BY es.`key`;

DELETE es
FROM emulator_settings es
JOIN cleanup_furnidata_settings cfs ON cfs.`key` = es.`key`;

-- Preview remaining matching rows inside the transaction.
SELECT COUNT(*) AS remaining_furnidata_settings
FROM emulator_settings es
JOIN cleanup_furnidata_settings cfs ON cfs.`key` = es.`key`;

-- Safe default. Change to COMMIT after reviewing the preview.
ROLLBACK;
