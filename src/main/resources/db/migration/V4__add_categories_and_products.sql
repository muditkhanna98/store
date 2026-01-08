CREATE TABLE `store`.`categories`
(
    `id`   TINYINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `store`.`products`
(
    `id`          BIGINT         NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255)   NULL,
    `price`       DECIMAL(10, 2) NULL,
    `category_id` TINYINT        NULL,
    PRIMARY KEY (`id`),
    INDEX `category_id__fk_idx` (`category_id` ASC) VISIBLE,
    CONSTRAINT `category_id__fk`
        FOREIGN KEY (`category_id`)
            REFERENCES `store`.`categories` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

