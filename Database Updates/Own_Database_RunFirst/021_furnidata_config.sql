-- 021_furnidata_config.sql
-- Seeds the furnidata feature config keys read at runtime by
-- FurnitureTextProvider / FurnidataReader / FurnidataWatcher and
-- FurniEditorImportTextEvent. Without these rows a fresh install logs
-- "Config key not found" for each (ConfigurationManager logs ERROR even
-- when a default is supplied) and the values are not editable from the DB.
--
-- Notes:
--  * *.enabled keys are read via Boolean.parseBoolean → use true/false (NOT 1/0).
--  * items.furnidata.path is intentionally empty: when blank the source is
--    derived from furni.editor.asset.base.path (seeded by 004_furni_editor.sql)
--    → <base>/furnidata (split-tier) or <base>/FurnitureData.json (single file).
--  * Editor write-path keys (items.furnidata.edit.*) are seeded by 020.

INSERT IGNORE INTO `emulator_settings` (`key`,`value`) VALUES
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
('furni.editor.import.url','https://www.habbo.it/gamedata/furnidata_json/1'),
('furni.editor.import.cache.ms','600000');
