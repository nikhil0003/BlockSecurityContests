use securitycontest;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user,role,user_role,contest,contestant,judge,ledger,sponser,wallet,grade;


CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `enabled` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_role` (
  `user_role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`user_role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `contest` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `sponserAmount` bigint DEFAULT NULL,
    `closed` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `contestant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_area` varchar(255) DEFAULT NULL,
  `grade` bigint DEFAULT NULL,
  `contest_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkxe255ggrm2s4gubvextgon86` (`contest_id`),
  KEY `FKeg4du20g71lh58sco0b8hu9a8` (`user_id`),
  CONSTRAINT `FKeg4du20g71lh58sco0b8hu9a8` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkxe255ggrm2s4gubvextgon86` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `judge` (
  `id` bigint NOT NULL  AUTO_INCREMENT,
  `contest_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc0mk955eu1k7k2lh2igoeece4` (`contest_id`),
  KEY `FKna7n0dt1pt693iv9twjvjwmj0` (`user_id`),
  CONSTRAINT `FKc0mk955eu1k7k2lh2igoeece4` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `FKna7n0dt1pt693iv9twjvjwmj0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ledger` (
  `id` bigint NOT NULL  AUTO_INCREMENT,
  `amount` bigint DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `address_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4tbbbc95t6vm5kxsggg155h6j` (`address_id`),
  CONSTRAINT `FK4tbbbc95t6vm5kxsggg155h6j` FOREIGN KEY (`address_id`) REFERENCES `wallet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `sponser` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contest_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK56qnv2gd6ka0033ct0c2hxp18` (`contest_id`),
  KEY `FK7457jlpv2tyrgmjdn65cdn2lv` (`user_id`),
  CONSTRAINT `FK56qnv2gd6ka0033ct0c2hxp18` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `FK7457jlpv2tyrgmjdn65cdn2lv` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `balance` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3y9jbtvtnd4rm8xk3iu4wi6up` (`address`),
  KEY `FKbs4ogwiknsup4rpw8d47qw9dx` (`user_id`),
  CONSTRAINT `FKbs4ogwiknsup4rpw8d47qw9dx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `grade` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contestant_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `contest_id` bigint DEFAULT NULL,
  `judge_id` bigint DEFAULT NULL,
  `gradeValue` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `contestant_id` (`contestant_id`),
  KEY `user_id` (`user_id`),
  KEY `contest_id` (`contest_id`),
  KEY `judge_id` (`judge_id`),
  CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`contestant_id`) REFERENCES `contestant` (`id`),
  CONSTRAINT `grade_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `grade_ibfk_3` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`id`),
  CONSTRAINT `grade_ibfk_4` FOREIGN KEY (`judge_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `judge_review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `judge_user_id` bigint DEFAULT NULL,
  `sponser_user_id` bigint DEFAULT NULL,
  `review` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `judge_user_id` (`judge_user_id`),
  KEY `sponser_user_id` (`sponser_user_id`),
  CONSTRAINT `judge_review_ibfk_1` FOREIGN KEY (`judge_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `judge_review_ibfk_2` FOREIGN KEY (`sponser_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
