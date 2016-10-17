#Author: Cheryl.lim.2015, Aloysiuslim.2015

DROP SCHEMA IF EXISTS bios_database;
CREATE SCHEMA IF NOT EXISTS bios_database;
USE bios_database;

CREATE TABLE IF NOT EXISTS bios_database.Course (
  course_id VARCHAR(10) NOT NULL,
  school VARCHAR(5) NOT NULL,
  title VARCHAR(100) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  exam_date DATE NOT NULL,
  exam_start TIME NOT NULL,
  exam_end TIME NOT NULL,
  PRIMARY KEY (`course_id`))
;

CREATE TABLE IF NOT EXISTS bios_database.Student (
  user_id VARCHAR(128) NOT NULL,
  password VARCHAR(128) NOT NULL,
  name VARCHAR(100) NOT NULL,
  school VARCHAR(5) NOT NULL,
  edollar DECIMAL(6,2) NOT NULL,
  PRIMARY KEY (`user_id`))
;

CREATE TABLE IF NOT EXISTS bios_database.Section (
  course_id VARCHAR(10) NOT NULL,
  section_id VARCHAR(3) NOT NULL,
  day INT NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  instructor VARCHAR(100) NOT NULL,
  venue VARCHAR(100) NOT NULL,
  size INT(2) NOT NULL,
  PRIMARY KEY (`section_id`,`course_id`),
  CONSTRAINT section_fk
    FOREIGN KEY (`course_id`)
    REFERENCES bios_database.Course (`course_id`)
 )
;

CREATE TABLE IF NOT EXISTS bios_database.Bid (
  user_id VARCHAR(128) NOT NULL,
  amount DECIMAL(6,2) NOT NULL,
  course_id VARCHAR(10) NOT NULL,
  section_id VARCHAR(3) NOT NULL,
  status VARCHAR(15) NOT NULL,
  PRIMARY KEY (`user_id`, course_id, `section_id`),
  CONSTRAINT bid_fk1
    FOREIGN KEY (`user_id`)
    REFERENCES bios_database.Student (`user_id`),
  CONSTRAINT bid_fk2
    FOREIGN KEY (`course_id`)
    REFERENCES bios_database.Course (`course_id`),
  CONSTRAINT bid_fk3
    FOREIGN KEY (`section_id`)
    REFERENCES bios_database.Section (`section_id`)
 )
;

CREATE TABLE IF NOT EXISTS bios_database.course_completed (
  user_id VARCHAR(128) NOT NULL,
  course_id VARCHAR(10) NOT NULL,
  PRIMARY KEY (`user_id`, `course_id`),
  CONSTRAINT course_completed_fk1
    FOREIGN KEY (`course_id`)
    REFERENCES bios_database.Course (`course_id`),
  CONSTRAINT course_completed_fk2
    FOREIGN KEY (`user_id`)
    REFERENCES bios_database.Student (`user_id`)
 )
;

CREATE TABLE IF NOT EXISTS bios_database.Prerequisite (
  course_id VARCHAR(10) NOT NULL,
  prerequisite VARCHAR(10) NOT NULL,
  PRIMARY KEY (`course_id`, `prerequisite`),
  CONSTRAINT prerequisite_fk1
    FOREIGN KEY (`course_id`)
    REFERENCES bios_database.Course (`course_id`),
  CONSTRAINT prerequisite_fk2
    FOREIGN KEY (`prerequisite`)
    REFERENCES bios_database.Course (`course_id`)
 )
;


CREATE TABLE IF NOT EXISTS bios_database.round (
  round_num VARCHAR(2) NOT NULL,
  round_status VARCHAR(10) NOT NULL,
  PRIMARY KEY (`round_num`))
;

CREATE TABLE IF NOT EXISTS bios_database.section_student (
    user_id VARCHAR(128) NOT NULL,
    course_id VARCHAR(10) NOT NULL,
    section_id VARCHAR(3) NOT NULL,
    amount DECIMAL(6,2) NOT NULL,
    status VARCHAR(15) NOT NULL,
    CONSTRAINT secStud_PK PRIMARY KEY(user_id,course_id, section_id),
    CONSTRAINT secStud_FK1 FOREIGN KEY (course_id,section_id) REFERENCES Section(course_id,section_id),
    CONSTRAINT secStud_FK2 FOREIGN KEY (user_id) REFERENCES Student(user_id)
)
;

CREATE TABLE IF NOT EXISTS bios_database.Admin (
  user_id VARCHAR(128) NOT NULL,
  password VARCHAR(128) NOT NULL,
  PRIMARY KEY (user_id)
)
;

CREATE TABLE IF NOT EXISTS bios_database.section_minimal_price (
  course_id VARCHAR(10) NOT NULL,
  section_id VARCHAR(3) NOT NULL,
  minimum_bid DECIMAL(6,2) NOT NULL,
  PRIMARY KEY(course_id, section_id),
  CONSTRAINT bidpricing_fk1 FOREIGN KEY (course_id, section_id) REFERENCES Section(course_id, section_id)
)
;

INSERT INTO `admin` (`user_id`, `password`) VALUES ('admin', '1234');
INSERT INTO `round` (`round_num`, `round_status`) VALUES ('0', 'inactive'); 

