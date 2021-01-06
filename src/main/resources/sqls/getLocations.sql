/**
 * Author:  rellu
 * Created: 05.01.2021
 */

SELECT l.*, lt.location_type from `rellu_essentials`.`location` l
INNER JOIN `rellu_essentials`.`location_type` lt on l.location_type_fk = lt.id;