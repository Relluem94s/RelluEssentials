CREATE TABLE IF NOT EXISTS `rellu_essentials`.`custom_npc_dialogue` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `createdby` INT NOT NULL,
  `updated` DATETIME NULL,
  `updatedby` INT NULL,
  `deleted` DATETIME NULL,
  `deletedby` INT NULL,
  `listPosition` TINYINT(10) NOT NULL,
  `text` VARCHAR(256) NOT NULL,
  `custom_npc_fk` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_custom_npc_dialogue_1_idx` (`custom_npc_fk` ASC) VISIBLE)
ENGINE = InnoDB;