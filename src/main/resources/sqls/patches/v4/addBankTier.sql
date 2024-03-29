/**
 * Author:  rellu
 * Created: 15.01.2023
 */

CREATE TABLE IF NOT EXISTS `rellu_essentials`.`bank_tier` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(94) NOT NULL,
  `limit` BIGINT(21) NOT NULL,
  `interest` FLOAT NOT NULL,
  `cost` INT(21) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;