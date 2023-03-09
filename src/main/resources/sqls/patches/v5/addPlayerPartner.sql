/**
 * Author:  rellu
 * Created: 10.03.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`player_partner` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `first_partner_fk` INT NOT NULL,
  `second_partner_fk` INT NOT NULL,
  `shareProtections` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;