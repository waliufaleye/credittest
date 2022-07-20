CREATE DATABASE `creditdb` /*!40100 COLLATE 'utf8mb4_0900_ai_ci' */;

CREATE TABLE `creditdb`.`credit_detail` (
	`id` BIGINT(19) NOT NULL,
	`amount_value` DECIMAL(19,2) NULL DEFAULT NULL,
	`branch_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`currency_code` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`customer_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`loan_account_scheme` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`loan_period_days` BIGINT(19) NULL DEFAULT NULL,
	`loan_period_months` BIGINT(19) NULL DEFAULT NULL,
	`no_of_installmental` BIGINT(19) NULL DEFAULT NULL,
	`re_pmt_method` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`status` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `creditdb`.`generator_cd_table` (
	`ID` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`next_val` BIGINT(19) NULL DEFAULT NULL,
	PRIMARY KEY (`ID`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `creditdb`.`hibernate_sequence` (
	`next_val` BIGINT(19) NULL DEFAULT NULL
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `creditdb`.`role` (
	`id` BIGINT(19) NOT NULL,
	`role_description` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`status` VARCHAR(1) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UK_hlxtstdpw16vslauurcnn5rw6` (`role_description`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `creditdb`.`privilege` (
	`id` BIGINT(19) NOT NULL,
	`name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UK_h7iwbdg4ev8mgvmij76881tx8` (`name`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `creditdb`.`roles_privileges` (
	`role_id` BIGINT(19) NOT NULL,
	`privilege_id` BIGINT(19) NOT NULL,
	INDEX `FK5yjwxw2gvfyu76j3rgqwo685u` (`privilege_id`) USING BTREE,
	INDEX `FK9h2vewsqh8luhfq71xokh4who` (`role_id`) USING BTREE,
	CONSTRAINT `FK5yjwxw2gvfyu76j3rgqwo685u` FOREIGN KEY (`privilege_id`) REFERENCES `creditdb`.`privilege` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK9h2vewsqh8luhfq71xokh4who` FOREIGN KEY (`role_id`) REFERENCES `creditdb`.`role` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

CREATE TABLE `creditdb`.`user` (
	`id` BIGINT(19) NOT NULL,
	`channel` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`del_flg` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`expiration` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`password` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`status` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`user_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`userrole_id` BIGINT(19) NOT NULL,
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `UK_lqjrcobrh9jc8wpcar64q1bfh` (`user_name`) USING BTREE,
	INDEX `FKsyty2g87s893yhuwjcj6fcyo4` (`userrole_id`) USING BTREE,
	CONSTRAINT `FKsyty2g87s893yhuwjcj6fcyo4` FOREIGN KEY (`userrole_id`) REFERENCES `creditdb`.`role` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;


CREATE TABLE `creditdb`.`apiuser_gen` (
	`next_val` BIGINT(19) NULL DEFAULT NULL
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;

INSERT INTO `creditdb`.`role` (`id`, `role_description`, `status`) VALUES (1, 'LOAN_PROCESSOR', 'A');

INSERT INTO `creditdb`.`privilege` (`id`, `name`) VALUES (1, 'createLoan');
INSERT INTO `creditdb`.`privilege` (`id`, `name`) VALUES (3, 'deleteLoan');
INSERT INTO `creditdb`.`privilege` (`id`, `name`) VALUES (5, 'getLoanDetailsById');
INSERT INTO `creditdb`.`privilege` (`id`, `name`) VALUES (4, 'getLoans');
INSERT INTO `creditdb`.`privilege` (`id`, `name`) VALUES (2, 'updateLoan');


INSERT INTO `creditdb`.`roles_privileges` (`role_id`, `privilege_id`) VALUES (1, 1);
INSERT INTO `creditdb`.`roles_privileges` (`role_id`, `privilege_id`) VALUES (1, 2);
INSERT INTO `creditdb`.`roles_privileges` (`role_id`, `privilege_id`) VALUES (1, 3);
INSERT INTO `creditdb`.`roles_privileges` (`role_id`, `privilege_id`) VALUES (1, 4);
INSERT INTO `creditdb`.`roles_privileges` (`role_id`, `privilege_id`) VALUES (1, 5);

INSERT INTO `creditdb`.`user` (`id`, `channel`, `del_flg`, `expiration`, `password`, `status`, `user_name`, `userrole_id`) VALUES (1, 'WEB', 'N', NULL, NULL, 'I', 'loantester', 1);


