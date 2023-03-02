/**
 * Author:  rellu
 * Created: 02.10.2021
 */

ALTER TABLE `rellu_essentials`.`player`
ADD CONSTRAINT `fk_player_group_1` FOREIGN KEY (group_fk) REFERENCES `rellu_essentials`.`group` (`id`);