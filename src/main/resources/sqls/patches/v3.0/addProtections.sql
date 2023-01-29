/**
 * Author:  rellu
 * Created: 29.01.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`protections` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `player_fk` INT(21) NOT NULL,
  `location_fk` INT NOT NULL,
  `material_name` VARCHAR(255) NOT NULL,
  `flags` JSON NULL,
  `rights` JSON NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_player_protection_1_idx` (`player_fk` ASC) VISIBLE,
  INDEX `fk_protections_Location_1_idx` (`location_fk` ASC) VISIBLE)
ENGINE = InnoDB;