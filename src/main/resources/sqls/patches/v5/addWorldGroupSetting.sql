/**
 * Author:  rellu
 * Created: 19.02.2023
 */


CREATE TABLE IF NOT EXISTS `rellu_essentials`.`world_group_setting` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CREATED` DATETIME NOT NULL,
  `CREATEDBY` INT NOT NULL,
  `UPDATED` DATETIME NULL,
  `UPDATEDBY` INT NULL,
  `DELETED` DATETIME NULL,
  `DELETEDBY` INT NULL,
  `setting_fk` INT NOT NULL,
  `world_group_fk` INT NOT NULL,
  `value` JSON NOT NULL,
  PRIMARY KEY (`ID`));