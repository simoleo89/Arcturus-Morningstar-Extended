-- Fix NULL room paint columns
--
-- Some legacy/imported rooms have NULL in paper_wall / paper_floor / paper_landscape.
-- The server compares these with .equals("0.0") on room entry, which throws a
-- NullPointerException (RoomManager.openRoom) and prevents the room from loading.
-- This normalizes existing NULL values and re-enforces the NOT NULL DEFAULT '0.0'
-- constraint so it cannot happen again.

UPDATE `rooms` SET `paper_wall`      = '0.0' WHERE `paper_wall`      IS NULL;
UPDATE `rooms` SET `paper_floor`     = '0.0' WHERE `paper_floor`     IS NULL;
UPDATE `rooms` SET `paper_landscape` = '0.0' WHERE `paper_landscape` IS NULL;

ALTER TABLE `rooms` MODIFY COLUMN `paper_wall`      VARCHAR(50) NOT NULL DEFAULT '0.0';
ALTER TABLE `rooms` MODIFY COLUMN `paper_floor`     VARCHAR(50) NOT NULL DEFAULT '0.0';
ALTER TABLE `rooms` MODIFY COLUMN `paper_landscape` VARCHAR(50) NOT NULL DEFAULT '0.0';
