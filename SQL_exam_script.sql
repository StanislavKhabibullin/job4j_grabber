-- Database: company

-- DROP DATABASE company;

CREATE DATABASE company
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE company
    IS 'SQL-exam';


CREATE TABLE company(
	id integer NOT NULL,
	name character varying,
	CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);


INSERT INTO company(id, name) VALUES
	(1, 'Compik'),
	(2, 'Trest'),
	(3, 'Trestik'),
	(4, 'TikTok'),
	(5, 'PikPok'),
	(6, 'Shinok');

INSERT INTO person VALUES
	(7, 'Alex', 2),
	(8, 'Skin', 2),
	(9, 'Tresto', 2),
	(10, 'Vik', 2),
	(11, 'Shmik', 2),
	(12, 'Terik', 2),
	(13, 'Alex3', 3),
	(14, 'Skin3', 3),
	(15, 'Tresto3', 3),
	(16, 'Vik3', 3),
	(17, 'Shmik3', 3),
	(18, 'Terik3', 3),
	(19, 'Alex4', 4),
	(20, 'Skin4', 4),
	(21, 'Tresto4', 4),
	(22, 'Vik4', 4),
	(23, 'Shmik4', 4),
	(24, 'Terik4', 4),
	(25, 'Alex5', 5),
	(26, 'Skin5', 5),
	(27, 'Tresto5', 5),
	(28, 'Vik5', 5),
	(29, 'Shmik5', 5),
	(30, 'Terik5', 5),
	(31, 'Alex6', 6),
	(32, 'Skin6', 6),
	(33, 'Tresto6', 6),
	(34, 'Vik6', 6),
	(35, 'Shmik6', 6),
	(36, 'Terik6', 6),
	(37, 'BigBos', 6);

select s.name as Имя, ss.name as Название_компании
				from person as s join company as ss 
				on s.company_id=ss.id where ss.id!=5;

drop table tCountPersons;
select max(PersonCount) as ResultMax
into tCountPersons
from(
select 
ss.name as Comp, 
COUNT(s.name) as PersonCount
from company as ss 
left join person as s on s.company_id=ss.id
GROUP BY(ss.name)
) as tempQ

;
		select 
			ss.name as Comp, 
			COUNT(s.name) as PersonCount
		from company as ss 
		left join person as s 
		on s.company_id=ss.id
		GROUP BY(ss.name) 
	

HAVING (count(s.name) = (select ResultMax from tCountPersons))