/**
 * Author:  rellu
 * Created: 31.01.2023
 */

SELECT 
    pr.*
FROM `rellu_essentials`.`protections` pr

WHERE pr.deletedby is null;