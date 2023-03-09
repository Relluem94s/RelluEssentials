/**
 * Author:  rellu
 * Created: 09.03.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`drops` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `MATERIAL` VARCHAR(94) NOT NULL,
  `MIN_INT` INT NOT NULL,
  `MAX_INT` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;