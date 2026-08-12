/**
 * Author:  rellu
 * Created: 11.08.2026
 */

 SELECT `id`
 FROM `rellu_essentials`.`protections`
 WHERE `deleted` IS NOT NULL
     AND `deleted` < NOW() - INTERVAL 1 MONTH
     AND `location_fk` IN (
         SELECT `id`
         FROM `rellu_essentials`.`location`
         WHERE `deletedby` IS NOT NULL
     );