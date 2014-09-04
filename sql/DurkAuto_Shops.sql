-- lose the key detection
SET FOREIGN_KEY_CHECKS=0;

-- Creating a database
DROP DATABASE IF EXISTS `durkauto_shops`;
CREATE DATABASE `durkauto_shops`;

-- Use new database
USE `durkauto_shops`;

-- Create a user table
DROP TABLE IF EXISTS `durkauto_user`;
CREATE TABLE `durkauto_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(64) NOT NULL,
  `create_time` datetime NOT NULL,
  `email` varchar(128) DEFAULT NULL,
  `phone` varchar(32) DEFAULT NULL,
  `realname` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Establish the root user
insert into `durkauto_user` values(null, 'root', password('root'), now(), 'durkauto@sina.com', '10000000000', '系统管理员');

-- Create a appointment service type table
DROP TABLE IF EXISTS `durkauto_appointment_type`;
CREATE TABLE `durkauto_appointment_type` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`type_name` varchar(32) NOT NULL,
	`parking` int(4) NOT NULL,
	`business_date` int(3),
	`business_hours_start` time,
	`business_hours_end` time,
	`service_time` int(4) NOT NULL,
	`min_time` int(4) NOT NULL, 
	`reminder_time` int(4) NOT NULL, 
	`Wait_time` int(4) NOT NULL,
	`time_scale` int(4) NOT NULL,
	`time_basis` varchar(10) NOT NULL,
	`exclusive` boolean NOT NULL,
	`enable` boolean not null,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create a appointment table
DROP TABLE IF EXISTS `durkauto_appointment_service`;
CREATE TABLE `durkauto_appointment_service` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`service_type` bigint(20) not null,
	`appoint_date` date not null,
	`appoint_time` time not null,
	`arrival_time` time,
	`reminder_time` time not null,
	`timeout_time` time not null,
	`service_state` int(1) not null,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;