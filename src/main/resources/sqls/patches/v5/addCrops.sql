/**
 * Author:  rellu
 * Created: 09.03.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`crops` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `PLANT` VARCHAR(94) NOT NULL,
  `SEED` VARCHAR(94) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;