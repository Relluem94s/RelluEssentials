/**
 * Author:  rellu
 * Created: 09.07.2026
 */

INSERT INTO `plugin_informations` (`CREATED`, `CREATEDBY`, `tab_header`, `tab_footer`, `motd_message`, `motd_players`, `db_version`) SELECT now(), 1, tab_header, tab_footer, motd_message, motd_players, 10 FROM `plugin_informations` where deletedby is null;
