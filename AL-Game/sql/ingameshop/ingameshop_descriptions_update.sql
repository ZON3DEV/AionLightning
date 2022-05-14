ALTER TABLE `ingameshop`
MODIFY COLUMN `title_description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `gift`,
MODIFY COLUMN `description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `title_description`;
