/**
 * Author:  rellu
 * Created: 10.07.2026
 */

DELETE FROM `rellu_essentials`.`location`
WHERE
    `deletedby` IS NOT NULL
    AND `deleted` < NOW() - INTERVAL 1 MONTH
    AND `id` NOT IN (
        SELECT `location_fk`
        FROM `rellu_essentials`.`protections`
        WHERE `location_fk` IS NOT NULL
    );