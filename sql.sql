CREATE TABLE `account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `is_ad_permission` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0, 1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `withdrew_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_email_unique` (`email`),
  UNIQUE KEY `account_phone_unique` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `background` (
  `id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `breed` (
  `id` bigint NOT NULL,
  `species_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `default_image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `breed_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_url` varchar(255) NOT NULL,
  `tag` varchar(255) NOT NULL,
  `breed_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `breed_image_breed_FK` (`breed_id`),
  CONSTRAINT `breed_image_breed_FK` FOREIGN KEY (`breed_id`) REFERENCES `breed` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `diary` (
  `id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  `content` text NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `deleted_dt` datetime DEFAULT NULL,
  `account_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `diary_account_FK` (`account_id`),
  KEY `diary_profile_FK` (`profile_id`),
  CONSTRAINT `diary_account_FK` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `diary_profile_FK` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `diary_comment` (
  `id` bigint NOT NULL,
  `diary_id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  `content` text NOT NULL,
  `created_dt` datetime NOT NULL,
  `updated_dt` datetime NOT NULL,
  `deleted_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `diary_media` (
  `id` bigint NOT NULL,
  `diary_id` bigint NOT NULL,
  `url` text NOT NULL,
  `media_order` int NOT NULL,
  `type` char(1) NOT NULL,
  `created_dt` datetime NOT NULL,
  `updated_dt` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `diary_reply` (
  `id` bigint NOT NULL,
  `diary_comment_id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  `content` text NOT NULL,
  `created_dt` datetime NOT NULL,
  `updated_dt` datetime NOT NULL,
  `deleted_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `diary_like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `diary_id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `diary_like_diary_FK` (`diary_id`),
  KEY `diary_like_profile_FK` (`profile_id`),
  CONSTRAINT `diary_like_diary_FK` FOREIGN KEY (`diary_id`) REFERENCES `diary` (`id`),
  CONSTRAINT `diary_like_profile_FK` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `birthday` date NOT NULL,
  `gender` tinyint NOT NULL DEFAULT '0',
  `breed_id` bigint NOT NULL,
  `background_id` bigint NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pet_to_do_relationship` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pet_id` bigint DEFAULT NULL,
  `to_do_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pet_to_do_relationship_pet_FK` (`pet_id`),
  KEY `pet_to_do_relationship_to_do_FK` (`to_do_id`),
  CONSTRAINT `pet_to_do_relationship_pet_FK` FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`),
  CONSTRAINT `pet_to_do_relationship_to_do_FK` FOREIGN KEY (`to_do_id`) REFERENCES `to_do` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `profile` (
  `id` bigint NOT NULL,
  `account_id` bigint NOT NULL,
  `name` varchar(50) NOT NULL,
  `image_url` text,
  `push_token` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `push` (
  `id` bigint NOT NULL,
  `profile_id` bigint NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `Field` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `species` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `species_unique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `to_do` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_profile_id` bigint DEFAULT NULL,
  `date` date NOT NULL,
  `time` datetime DEFAULT NULL,
  `is_all_day` tinyint(1) NOT NULL DEFAULT '0',
  `content` varchar(255) DEFAULT NULL,
  `tag` varchar(50) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `color` varchar(20) NOT NULL,
  `is_using_alarm` tinyint(1) NOT NULL DEFAULT '0',
  `unit` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'null, time, day, week',
  `interval_num` bigint DEFAULT NULL,
  `week_days` int NOT NULL DEFAULT '1',
  `account_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `to_do_account_FK` (`account_id`),
  KEY `to_do_profile_FK` (`create_profile_id`),
  CONSTRAINT `to_do_account_FK` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `to_do_profile_FK` FOREIGN KEY (`create_profile_id`) REFERENCES `profile` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `to_do_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `to_do_id` bigint NOT NULL,
  `time` time NOT NULL,
  `complete_profile_id` bigint DEFAULT NULL,
  `complete_time` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `to_do_instance_profile_FK` (`complete_profile_id`),
  KEY `to_do_instance_to_do_FK` (`to_do_id`),
  CONSTRAINT `to_do_instance_profile_FK` FOREIGN KEY (`complete_profile_id`) REFERENCES `profile` (`id`),
  CONSTRAINT `to_do_instance_to_do_FK` FOREIGN KEY (`to_do_id`) REFERENCES `to_do` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `to_do_week_day` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `to_do_id` bigint NOT NULL,
  `week_day` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `to_do_week_day_to_do_FK` (`to_do_id`),
  CONSTRAINT `to_do_week_day_to_do_FK` FOREIGN KEY (`to_do_id`) REFERENCES `to_do` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;