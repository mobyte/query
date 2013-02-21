-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.12-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema query_example
--

CREATE DATABASE IF NOT EXISTS query_example;
USE query_example;

--
-- Definition of table `query_example`.`clients`
--

DROP TABLE IF EXISTS `query_example`.`clients`;
CREATE TABLE  `query_example`.`clients` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_example`.`clients`
--

/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
LOCK TABLES `clients` WRITE;
INSERT INTO `query_example`.`clients` VALUES  (1,'user1'),
 (2,'client2'),
 (3,'guest3'),
 (4,'hacker4');
UNLOCK TABLES;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;


--
-- Definition of table `query_example`.`emails`
--

DROP TABLE IF EXISTS `query_example`.`emails`;
CREATE TABLE  `query_example`.`emails` (
  `id` int(11) unsigned NOT NULL,
  `client` int(11) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_example`.`emails`
--

/*!40000 ALTER TABLE `emails` DISABLE KEYS */;
LOCK TABLES `emails` WRITE;
INSERT INTO `query_example`.`emails` VALUES  (1,1,'user1@host1'),
 (2,1,'user1@localhost'),
 (3,2,'client2@somewere.com'),
 (4,3,'guest3@yh.com'),
 (5,3,'guest3@gm.com'),
 (6,3,'guest3@ms.com'),
 (7,4,'spammer@hacker.org');
UNLOCK TABLES;
/*!40000 ALTER TABLE `emails` ENABLE KEYS */;


--
-- Definition of table `query_example`.`phones`
--

DROP TABLE IF EXISTS `query_example`.`phones`;
CREATE TABLE  `query_example`.`phones` (
  `id` int(11) unsigned NOT NULL,
  `client` int(11) DEFAULT NULL,
  `value` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_example`.`phones`
--

/*!40000 ALTER TABLE `phones` DISABLE KEYS */;
LOCK TABLES `phones` WRITE;
INSERT INTO `query_example`.`phones` VALUES  (1,1,'+1111111'),
 (2,2,'+2222222'),
 (3,2,'+2220000'),
 (4,2,'+2221111'),
 (5,3,'+3333333'),
 (6,3,'+3330000');
UNLOCK TABLES;
/*!40000 ALTER TABLE `phones` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
