/**
 * Author:  rellu
 * Created: 19.02.2023
 */


INSERT INTO `rellu_essentials`.`world` (`ID`, `CREATED`, `CREATEDBY`, `NAME`, `world_group_fk`, `build_group_fk`) VALUES
(1, now(), 1, 'lobby', 1, 268435456),
(2, now(), 1, 'world', 2, 1),
(3, now(), 1, 'world_nether', 2, 1),
(4, now(), 1, 'world_the_end', 2, 1);