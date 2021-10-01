ALTER TABLE `rellu_essentials`.`player`
ADD CONSTRAINT `fk_player_group_1` FOREIGN KEY (group_fk) REFERENCES `rellu_essentials`.`group` (`id`);