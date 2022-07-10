DROP SCHEMA IF EXISTS `Task_Manger`;

CREATE SCHEMA `Task_Manger`;

use `Task_Manger`;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `user_id` int(11) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `completed` TINYINT DEFAULT NULL,
  CONSTRAINT `FK_DETAIL` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

