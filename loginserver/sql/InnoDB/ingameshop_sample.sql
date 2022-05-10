/*
Navicat MySQL Data Transfer

Source Server         : aion_extreme25
Source Server Version : 50141
Source Host           : localhost:3306
Source Database       : au_server_ls

Target Server Type    : MYSQL
Target Server Version : 50141
File Encoding         : 65001

Date: 2011-05-30 13:43:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `aionshop_categories` German Sample
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_categories`;
CREATE TABLE `aionshop_categories` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(255) NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_categories
-- ----------------------------
INSERT INTO `aionshop_categories` VALUES ('3', 'Charakteroptionen');
INSERT INTO `aionshop_categories` VALUES ('4', 'Rüstungsets');
INSERT INTO `aionshop_categories` VALUES ('5', 'Verstärkung');
INSERT INTO `aionshop_categories` VALUES ('6', 'Manasteine');
INSERT INTO `aionshop_categories` VALUES ('7', 'Flügel');
INSERT INTO `aionshop_categories` VALUES ('8', 'Verbrauchgüter');
INSERT INTO `aionshop_categories` VALUES ('9', 'Skillbücher');
INSERT INTO `aionshop_categories` VALUES ('10', 'Gottsteine');
INSERT INTO `aionshop_categories` VALUES ('11', 'Tränke');
INSERT INTO `aionshop_categories` VALUES ('12', 'Kostüme');
INSERT INTO `aionshop_categories` VALUES ('13', 'Hütte');
INSERT INTO `aionshop_categories` VALUES ('14', 'Accessoire');
INSERT INTO `aionshop_categories` VALUES ('15', 'Farben');
INSERT INTO `aionshop_categories` VALUES ('16', 'Miols');
INSERT INTO `aionshop_categories` VALUES ('17', 'Verwandlungen');
INSERT INTO `aionshop_categories` VALUES ('18', 'Sonstiges');
INSERT INTO `aionshop_categories` VALUES ('19', 'Angebote');
INSERT INTO `aionshop_categories` VALUES ('20', 'SuperDeal');

-- ----------------------------
-- Table structure for `aionshop_items`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_items`;
CREATE TABLE `aionshop_items` (
  `itemUniqueId` bigint(20) NOT NULL AUTO_INCREMENT,
  `itemTemplateId` bigint(20) NOT NULL,
  `itemCount` int(11) NOT NULL,
  `itemCategory` int(11) NOT NULL,
  `itemPrice` int(11) NOT NULL,
  `itemName` varchar(255) NOT NULL,
  `itemDesc` varchar(255) NOT NULL,
  `itemEyecatch` tinyint(1) NOT NULL DEFAULT '0',
  `itemClassRestrict` varchar(255) NOT NULL,
  `itemServerRestrict` varchar(255) NOT NULL,
  PRIMARY KEY (`itemUniqueId`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_items
-- ----------------------------
INSERT INTO `aionshop_items` VALUES ('1', '187000037', '1', '7', '2500', 'Glücksflügel', 'Glücksflügel', '1', '', '');
INSERT INTO `aionshop_items` VALUES ('2', '187000018', '1', '7', '2500', 'Sturm-Schwingen', 'Sturm-Schwingen', '1', '', '');
INSERT INTO `aionshop_items` VALUES ('3', '187000032', '1', '7', '2500', 'Flügel der Obscurati', 'Flügel der Obscurati', '0', '', '');
INSERT INTO `aionshop_items` VALUES ('4', '187000031', '1', '7', '2500', 'Flügel der Orichalcum-Loge', ' Flügel der Orichalcum-Loge', '0', '', '');

-- ----------------------------
-- Table structure for `aionshop_transactions`
-- ----------------------------
DROP TABLE IF EXISTS `aionshop_transactions`;
CREATE TABLE `aionshop_transactions` (
  `transaction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server_id` int(11) NOT NULL,
  `item_unique_id` int(11) NOT NULL,
  `buy_timestamp` bigint(20) NOT NULL,
  `player_id` bigint(20) NOT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aionshop_transactions
-- ----------------------------

