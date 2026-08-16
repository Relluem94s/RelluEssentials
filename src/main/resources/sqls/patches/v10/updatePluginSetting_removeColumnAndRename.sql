/**
 * Author:  rellu
 * Created: 16.08.2026
 */

ALTER TABLE `rellu_essentials`.`plugin_setting`
DROP COLUMN `value`,
RENAME COLUMN `value_new` TO `value`;