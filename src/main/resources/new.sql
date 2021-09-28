drop TABLE if EXISTS `article`;
CREATE TABLE `article`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `title`        varchar(200)     DEFAULT NULL,
    `title_pic`     varchar(55)      DEFAULT NULL,
    `slug`         varchar(200)     DEFAULT NULL,
    `created`      datetime         DEFAULT NOW(),
    `updated`      datetime         DEFAULT NOW(),
    `content`      text COMMENT '内容文字',
    `author_id`     bigint unsigned  DEFAULT '0',
    `type`         varchar(16)      DEFAULT 'post',
    `status`       varchar(16)      DEFAULT 'publish',
    `hits`         int(10) unsigned DEFAULT '0',
    `comments_num`  int(10) unsigned DEFAULT '0',
    `allow_comment` tinyint(1)       DEFAULT '1',
    `allow_ping`    tinyint(1)       DEFAULT '1',
    `allow_feed`    tinyint(1)       DEFAULT '1',
    `order_weight`  int(10)          DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `slug` (`slug`) USING BTREE,
    KEY `created` (`created`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 164
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT;


drop TABLE if EXISTS `category`;
CREATE TABLE `category`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `cate_name` varchar(64) not null,
    PRIMARY KEY (`id`) USING BTREE
);

drop TABLE if EXISTS `tag`;
CREATE TABLE `tag`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `tag_name` varchar(64) not null,
    PRIMARY KEY (`id`) USING BTREE
);

drop TABLE if EXISTS `article_category`;
CREATE TABLE `article_category`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `article_id` bigint not null,
    `category_id` bigint not null,
    PRIMARY KEY (`id`) USING BTREE
);

drop TABLE if EXISTS `article_tag`;
CREATE TABLE `article_tag`
(
    `id`           bigint unsigned NOT NULL AUTO_INCREMENT,
    `article_id` bigint not null,
    `tag_id` bigint not null,
    PRIMARY KEY (`id`) USING BTREE
);


