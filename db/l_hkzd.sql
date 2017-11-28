# Host: 127.0.0.1  (Version 5.7.17-log)
# Date: 2017-11-06 09:38:35
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
  `yt` varchar(50) DEFAULT NULL,
  `create_time` varchar(50) DEFAULT NULL,
  `isfk` varchar(10) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `bz` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`zdid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
