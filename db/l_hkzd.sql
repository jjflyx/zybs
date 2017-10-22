# Host: localhost  (Version 5.5.56)
# Date: 2017-10-22 22:56:39
# Generator: MySQL-Front 5.4  (Build 4.153) - http://www.mysqlfront.de/

/*!40101 SET NAMES utf8 */;

#
# Structure for table "l_hkzd"
#

DROP TABLE IF EXISTS `l_hkzd`;
CREATE TABLE `l_hkzd` (
  `zdid` int(5) NOT NULL DEFAULT '0',
  `actor` varchar(100) DEFAULT NULL,
  `zdmc` varchar(100) DEFAULT NULL,
  `fkrq` varchar(50) DEFAULT NULL,
  `sfjk` int(11) DEFAULT NULL,
  `gmtj` varchar(50) DEFAULT NULL,
  `yt` varchar(255) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `isfk` varchar(10) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`zdid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "l_hkzd"
#

INSERT INTO `l_hkzd` VALUES (1710200001,'jjf','dfa ','2017-10-20',123,'0001','213',NULL,NULL,'10002027'),(1710220001,'jjf','123','2017-10-22',12,'0003',NULL,'2017-10-22 22:49:52',NULL,'10002027'),(1710220002,'jjf','123213','2017-10-22',213,'0002',NULL,'2017-10-22 22:51:20',NULL,'10002027'),(1710220003,'jjf','123','2017-10-22',123,'0003',NULL,'2017-10-22 22:52:29','0001','10002027');
