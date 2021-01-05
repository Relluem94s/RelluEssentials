/**
 * Author:  rellu
 * Created: 05.01.2021
 */


-- -----------------------------------------------------
-- Table `rellu_essentials`.`block_history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`block_history` (
  `ID` INT NOT NULL,
  `CREATED` DATETIME NOT NULL,
  `CREATEDBY` INT NOT NULL,
  `UPDATED` DATETIME NULL,
  `UPDATEDBY` INT NULL,
  `DELETED` DATETIME NULL,
  `DELETEDBY` INT NULL,
  `location_fk` INT NOT NULL,
  `player_fk` INT NOT NULL,
  `material` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`ID`, `location_fk`, `player_fk`, `material`),
  INDEX `fk_block_history_1_idx` (`location_fk` ASC) VISIBLE,
  INDEX `fk_block_history_2_idx` (`player_fk` ASC) VISIBLE,
  CONSTRAINT `fk_block_history_1`
    FOREIGN KEY (`location_fk`)
    REFERENCES `rellu_essentials`.`location` (`id`),
  CONSTRAINT `fk_block_history_2`
    FOREIGN KEY (`player_fk`)
    REFERENCES `rellu_essentials`.`player` (`id`)
);
