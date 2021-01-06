/**
 * Author:  rellu
 * Created: 05.01.2021
 */

SELECT * from `rellu_essentials`.`player` p where p.id = ? and p.deletedby is null;