/**
 * Author:  rellu
 * Created: 27.07.2026
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`custom_npc` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `uuid` VARCHAR(94) NOT NULL,
  `entity_uuid` VARCHAR(94) NULL,
  `profile_name` VARCHAR(94) NOT NULL,
  `world` VARCHAR(45) NOT NULL,
  `x` DOUBLE NOT NULL,
  `y` DOUBLE NOT NULL,
  `z` DOUBLE NOT NULL,
  `yaw` DOUBLE NULL,
  `pitch` DOUBLE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE)
ENGINE = InnoDB;