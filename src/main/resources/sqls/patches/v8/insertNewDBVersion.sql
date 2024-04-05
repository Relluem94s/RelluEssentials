/**
 * Author:  rellu
 * Created: 19.02.2023
 */

INSERT INTO `plugin_informations` (`CREATED`, `CREATEDBY`, `tab_header`, `tab_footer`, `motd_message`, `motd_players`, `db_version`) SELECT now(), 1, tab_header, tab_footer, motd_message, motd_players, 8 FROM `plugin_informations` where deletedby is null;
