/**
 * Author:  rellu
 * Created: 16.08.2026
 */

ALTER TABLE `rellu_essentials`.`setting_player`
DROP COLUMN `value`,
RENAME COLUMN `value_new` TO `value`;