/**
 * Author:  rellu
 * Created: 12.02.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`npc` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `name` VARCHAR(94) NULL,
  `profession` VARCHAR(94) NULL,
  `type` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_1_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_2_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_3_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_4_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_5_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_6_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_7_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_8_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_9_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_10_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_11_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_12_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_13_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_14_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_15_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_16_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_17_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_18_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_19_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_20_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_21_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_22_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_23_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_24_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_25_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_26_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_27_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  `slot_28_name` VARCHAR(94) NOT NULL DEFAULT 'AIR',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;