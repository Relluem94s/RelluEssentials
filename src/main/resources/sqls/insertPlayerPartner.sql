/**
 * Author:  rellu
 * Created: 13.03.2023
 */
 
INSERT INTO `rellu_essentials`.`player_partner` 
(`created`,`createdby`,`first_partner_fk`,`second_partner_fk`,`shareProtections`) 
VALUES (now(),?,?,?,?);
