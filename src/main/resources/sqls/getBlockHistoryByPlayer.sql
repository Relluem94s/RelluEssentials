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
    bh.createdby = ?
    AND bh.deletedby IS NULL
ORDER BY bh.id DESC
;