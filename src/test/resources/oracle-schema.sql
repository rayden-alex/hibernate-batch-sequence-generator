DROP SEQUENCE IF EXISTS SEQ_CHILD_ID;
DROP SEQUENCE IF EXISTS SEQ_PARENT_ID;
DROP TABLE IF EXISTS CHILD_ENTITY;
DROP TABLE IF EXISTS PARENT_ENTITY;

CREATE TABLE PARENT_ENTITY (
  PARENT_ID BIGINT NOT NULL,
  CONSTRAINT PARENT_ID_PK
  PRIMARY KEY (PARENT_ID)
);

CREATE TABLE CHILD_ENTITY (
  CHILD_ID BIGINT NOT NULL,
  PARENT_ID BIGINT NOT NULL,
  CONSTRAINT CHILD_ID_PK
  PRIMARY KEY (CHILD_ID),
  CONSTRAINT PARENT_ID_FK
  FOREIGN KEY (PARENT_ID) REFERENCES PARENT_ENTITY (PARENT_ID)
);

CREATE SEQUENCE SEQ_PARENT_ID
START WITH 1
MINVALUE 1
CACHE 50;

CREATE SEQUENCE SEQ_CHILD_ID
START WITH 1
MINVALUE 1
CACHE 50;

