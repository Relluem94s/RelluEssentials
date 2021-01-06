/**
 * Author:  rellu
 * Created: 05.01.2021
 */

-- -----------------------------------------------------
-- Table `rellu_essentials`.`plugin_informations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`plugin_informations` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CREATED` DATETIME NOT NULL,
  `CREATEDBY` INT NOT NULL,
  `UPDATED` DATETIME NULL,
  `UPDATEDBY` INT NULL,
  `DELETED` DATETIME NULL,
  `DELETEDBY` INT NULL,
  `tab_header` VARCHAR(255) NOT NULL,
  `tab_footer` VARCHAR(255) NOT NULL,
  `motd_message` VARCHAR(255) NOT NULL,
  `motd_players` INT NOT NULL,
  `db_version` INT NOT NULL,
  PRIMARY KEY (`ID`));
