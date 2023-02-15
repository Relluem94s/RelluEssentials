/**
 * Author:  rellu
 * Created: 03.02.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`skills` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(94) NOT NULL,
  `displayname` VARCHAR(45) NOT NULL,
  `max_level` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;