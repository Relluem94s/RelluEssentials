/**
 * Author:  rellu
 * Created: 22.01.2023
 */

SELECT * FROM `rellu_essentials`.`bank_transaction` bt where bt.bank_account_fk = ? limit 10; 