/**
 * Author:  rellu
 * Created: 02.10.2021
 */

INSERT INTO `plugin_informations` (`CREATED`, `CREATEDBY`, `tab_header`, `tab_footer`, `motd_message`, `motd_players`, `db_version`) SELECT now(), 1, tab_header, tab_footer, motd_message, motd_players, 4 FROM `plugin_informations` where deletedby is null;
