/**
 * Author:  rellu
 * Created: 05.01.2021
 */

-- -----------------------------------------------------
-- Table `rellu_essentials`.`location_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`location_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `location_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
