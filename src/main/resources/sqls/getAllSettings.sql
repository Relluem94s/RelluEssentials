/**
 * Author:  rellu
 * Created: 15.07.2026
 */

SELECT
    ID,
    CREATED,
    CREATEDBY,
    UPDATED,
    UPDATEDBY,
    DELETED,
    DELETEDBY,
    name
FROM
    rellu_essentials.setting
WHERE
    DELETEDBY IS NULL;