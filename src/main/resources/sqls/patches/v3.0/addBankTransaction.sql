/**
 * Author:  rellu
 * Created: 15.01.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`bank_transaction` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `bank_account_fk` INT(21) NOT NULL,
  `value` BIGINT(21) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_bank_account_transaction_1_idx` (`bank_account_fk` ASC) VISIBLE)
ENGINE = InnoDB;
