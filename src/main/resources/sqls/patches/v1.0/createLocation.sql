/**
 * Author:  rellu
 * Created: 05.01.2021
 */

-- -----------------------------------------------------
-- Table `rellu_essentials`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`location` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `x` FLOAT NOT NULL,
  `y` FLOAT NOT NULL,
  `z` FLOAT NOT NULL,
  `yaw` FLOAT NOT NULL,
  `pitch` FLOAT NOT NULL,
  `world` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `location_type_fk` INT NOT NULL,
  `player_fk` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_location_type_1_idx` (`location_type_fk` ASC) VISIBLE,
  INDEX `fk_location_player_1_idx` (`player_fk` ASC) VISIBLE,
  CONSTRAINT `fk_location_type_1`
    FOREIGN KEY (`location_type_fk`)
    REFERENCES `rellu_essentials`.`location_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_location_player_1`
    FOREIGN KEY (`player_fk`)
    REFERENCES `rellu_essentials`.`player` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
