DROP DATABASE IF EXISTS ihatovgram;
CREATE DATABASE ihatovgram;

GRANT SELECT, UPDATE, INSERT ON ihatovgram.* TO 'ipuweb'@'localhost' IDENTIFIED BY 'ihatovgram';
FLUSH PRIVILEGES;

use ihatovgram;

DROP table IF EXISTS user;
CREATE TABLE user(
 id serial,
 fb_token varchar(50) NOT NULL,
 user_name varchar(50),
 create_date datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
 update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);

DROP table IF EXISTS image;
CREATE TABLE image(
 id serial,
 user_id bigint(20) unsigned NOT NULL,
 comment text,
 create_date datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
 update_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
);
