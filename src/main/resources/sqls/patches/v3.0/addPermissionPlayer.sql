/**
 * Author:  rellu
 * Created: 29.01.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`permission_player` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `player_fk` INT NOT NULL,
  `permission_fk` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;