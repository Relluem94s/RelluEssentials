/**
 * Author:  rellu
 * Created: 15.01.2023
 */

 CREATE TABLE IF NOT EXISTS `rellu_essentials`.`bank_account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `player_fk` INT(21) NOT NULL,
  `value` FLOAT(21) NOT NULL,
  `bank_tier_fk` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bank_account_tier_1_idx` (`bank_tier_fk` ASC) VISIBLE,
  INDEX `fk_bank_account_player_1_idx` (`player_fk` ASC) VISIBLE)
ENGINE = InnoDB;