/**
 * Author:  rellu
 * Created: 07.02.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`bag_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `displayname` VARCHAR(94) NOT NULL,
  `name` VARCHAR(94) NOT NULL,
  `cost` INT NOT NULL,
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
  PRIMARY KEY (`id`))
ENGINE = InnoDB;