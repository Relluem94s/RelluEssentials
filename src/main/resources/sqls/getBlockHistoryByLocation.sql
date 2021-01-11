/**
 * Author:  rellu
 * Created: 05.01.2021
 */

SELECT 
    bh.*, l.*
FROM 
    `rellu_essentials`.`block_history` bh
INNER JOIN 
    `rellu_essentials`.`location` l
ON 
    bh.location_fk = l.id
WHERE 
    bh.deletedby IS NULL
    AND l.x = ?
    AND l.y = ?
    AND l.z = ?
    AND l.location_type_fk = 4
;