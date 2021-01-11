/**
 * Author:  rellu
 * Created: 05.01.2021
 */


-- -----------------------------------------------------
-- Table `rellu_essentials`.`block_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`block_history` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CREATED` DATETIME NOT NULL,
  `CREATEDBY` INT NOT NULL,
  `DELETED` DATETIME NULL,
  `DELETEDBY` INT NULL,
  `location_fk` INT NOT NULL,
  `material` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_block_history_1_idx` (`location_fk` ASC),
  CONSTRAINT `fk_block_history_1`
    FOREIGN KEY (`location_fk`)
    REFERENCES `rellu_essentials`.`location` (`id`)
);
