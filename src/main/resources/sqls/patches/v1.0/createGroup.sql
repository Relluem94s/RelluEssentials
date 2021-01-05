/**
 * Author:  rellu
 * Created: 05.01.2021
 */

-- -----------------------------------------------------
-- Table `rellu_essentials`.`group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rellu_essentials`.`group` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `prefix` VARCHAR(2) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
