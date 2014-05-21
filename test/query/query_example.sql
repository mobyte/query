DROP DATABASE query_example;
CREATE DATABASE query_example;
\c query_example;

DROP TABLE IF EXISTS "clients";
CREATE TABLE "clients" (
  "id" int PRIMARY KEY,
  "name" text
);

INSERT INTO "clients" VALUES
 (1,'user1'),
 (2,'client2'),
 (3,'guest3'),
 (4,'hacker4');

DROP TABLE IF EXISTS "emails";
CREATE TABLE  "emails" (
  "id" int PRIMARY KEY,
  "client" int,
  "value" text
);

INSERT INTO "emails" VALUES
 (1,1,'user1@host1'),
 (2,1,'user1@localhost'),
 (3,2,'client2@somewere.com'),
 (4,3,'guest3@yh.com'),
 (5,3,'guest3@gm.com'),
 (6,3,'guest3@ms.com'),
 (7,4,'spammer@hacker.org');

DROP TABLE IF EXISTS "phones";
CREATE TABLE  "phones" (
  "id" int PRIMARY KEY,
  "client" int,
  "value" text
);

INSERT INTO "phones" VALUES  
 (1,1,'+1111111'),
 (2,2,'+2222222'),
 (3,2,'+2220000'),
 (4,2,'+2221111'),
 (5,3,'+3333333'),
 (6,3,'+3330000');
