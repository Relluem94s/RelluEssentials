/**
 * Author:  rellu
 * Created: 29.01.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`permission` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `name` VARCHAR(45) NOT NULL,
  `path` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;