/**
 * Author:  rellu
 * Created: 05.01.2021
 */

SELECT * from `rellu_essentials`.`player_partner` p where (p.first_partner_fk = ? or  p.second_partner_fk = ?)  and p.deletedby is null;