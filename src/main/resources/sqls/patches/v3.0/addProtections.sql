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
  `location_fk` INT NOT NULL,
  `material_name` VARCHAR(255) NOT NULL,
  `flags` JSON NULL,
  `rights` JSON NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;