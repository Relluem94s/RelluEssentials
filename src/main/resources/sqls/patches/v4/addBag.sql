/**
 * Author:  rellu
 * Created: 07.02.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`bag` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `player_fk` INT(21) NOT NULL,
  `bag_type_fk` INT NOT NULL,
  `slot_1_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_2_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_3_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_4_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_5_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_6_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_7_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_8_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_9_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_10_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_11_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_12_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_13_value` BIGINT(21) NOT NULL DEFAULT 0,
  `slot_14_value` BIGINT(21) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;