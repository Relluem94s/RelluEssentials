/**
 * Author:  rellu
 * Created: 05.01.2021
 */

-- -----------------------------------------------------
-- Table `rellu_essentials`.`player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`player` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `uuid` VARCHAR(45) NOT NULL,
  `group_fk` INT(21) NOT NULL,
  `afk` TINYINT NULL DEFAULT 0,
  `fly` TINYINT NULL DEFAULT 0,
  `customname` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_player_group_1_idx` (`group_fk` ASC) VISIBLE,
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC) VISIBLE,
  CONSTRAINT `fk_player_group_1`
    FOREIGN KEY (`group_fk`)
    REFERENCES `rellu_essentials`.`group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;