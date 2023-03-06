/**
 * Author:  rellu
 * Created: 19.02.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`world_group_inventory` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CREATED` DATETIME NOT NULL,
  `CREATEDBY` INT NOT NULL,
  `UPDATED` DATETIME NULL,
  `UPDATEDBY` INT NULL,
  `DELETED` DATETIME NULL,
  `DELETEDBY` INT NULL,
  `player_fk` INT NOT NULL,
  `world_group_fk` INT NOT NULL,
  `inventory` JSON NOT NULL,
  `health` DOUBLE NOT NULL,
  `food` INT NOT NULL,
  `totalExperience` INT NOT NULL,
  PRIMARY KEY (`ID`));