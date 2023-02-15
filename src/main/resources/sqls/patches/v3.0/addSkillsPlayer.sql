/**
 * Author:  rellu
 * Created: 03.02.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`skills_player` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `player_fk` INT NOT NULL,
  `skill_fk` INT NOT NULL,
  `skill_level` INT NOT NULL,
  `skill_xp` FLOAT NOT NULL,
  `block_counter_level` INT NULL,
  `block_counter_total` BIGINT(21) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;