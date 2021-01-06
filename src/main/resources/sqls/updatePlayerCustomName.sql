/**
 * Author:  rellu
 * Created: 05.01.2021
 */

UPDATE `rellu_essentials`.`player` 
SET 
    UPDATED = now(), 
    UPDATEDBY = ?,
    CUSTOMNAME = ?
WHERE ID = ?;