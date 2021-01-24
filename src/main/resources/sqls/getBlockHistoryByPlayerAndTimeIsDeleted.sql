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
    AND bh.created between  
        DATE_SUB(
            DATE_SUB(
                DATE_SUB(
                    DATE_SUB(
                        DATE_SUB(
                            NOW(),
                            INTERVAL ? MINUTE
                        ),
                        INTERVAL ? HOUR
                    ),
                    INTERVAL ? DAY
                ) ,
                INTERVAL ? MONTH
            ),
            INTERVAL ? YEAR
        ) 
        and NOW()