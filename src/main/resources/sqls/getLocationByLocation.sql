/**
 * Author:  rellu
 * Created: 05.01.2021
 */

SELECT 
    l.*
FROM `rellu_essentials`.`location` l

WHERE l.x = ?
    AND l.y = ?
    AND l.z = ?
    AND l.location_type_fk = 4 ;