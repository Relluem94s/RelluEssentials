/**
 * Author:  rellu
 * Created: 05.01.2021
 */

SELECT 
    l.*, 
    lt.location_type 
FROM 
    `rellu_essentials`.`location` l
INNER JOIN 
    `rellu_essentials`.`location_type` lt 
ON 
    l.location_type_fk = lt.id
WHERE 
    deletedby is null;