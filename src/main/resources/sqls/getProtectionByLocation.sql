/**
 * Author:  rellu
 * Created: 29.01.2023
 */

SELECT 
    pr.*
FROM `rellu_essentials`.`location` lo
INNER JOIN `rellu_essentials`.`protections` pr on pr.location_fk = lo.id

WHERE lo.x = ?
    AND lo.y = ?
    AND lo.z = ?
    AND lo.location_type_fk = 5 
    AND lo.deletedby is null
    AND pr.deletedby is null;