/**
 * Author:  rellu
 * Created: 06.03.2023
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
    deletedby is null and location_type_fk = 3;