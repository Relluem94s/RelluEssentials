/**
 * Author:  rellu
 * Created: 16.08.2026
 */

UPDATE `rellu_essentials`.`setting_player`
SET `value_new` = CASE JSON_UNQUOTE(`value`)
    WHEN 'true' THEN 1
    WHEN 'false' THEN 0
    ELSE CAST(JSON_UNQUOTE(`value`) AS UNSIGNED)
END;