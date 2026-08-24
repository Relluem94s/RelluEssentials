/**
 * Author:  rellu
 * Created: 15.07.2026
 */

UPDATE `rellu_essentials`.`world_group_setting`
SET `value_new` = CASE JSON_UNQUOTE(`value`)
    WHEN 'true' THEN 1
    WHEN 'false' THEN 0
    ELSE CAST(JSON_UNQUOTE(`value`) AS UNSIGNED)
END;