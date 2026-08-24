/**
 * Author:  rellu
 * Created: 15.07.2026
 */

ALTER TABLE `rellu_essentials`.`world_group_setting`
DROP COLUMN `value`,
RENAME COLUMN `value_new` TO `value`;