CREATE TABLE `store`.`tags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `store`.`user_tags` (
  `user_id` BIGINT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `tag_id`),
  INDEX `tag__fk_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `id__fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `store`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `tag__fk`
    FOREIGN KEY (`tag_id`)
    REFERENCES `store`.`tags` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);


