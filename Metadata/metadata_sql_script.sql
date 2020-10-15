drop database if exists Talia2;
CREATE DATABASE Talia2;
use Talia2;

DROP USER 'root'@'localhost';
CREATE USER 'root' @'localhost' IDENTIFIED BY 'root';
GRANT SELECT,INSERT ON Talia2.* TO root@localhost;

create table Communities (
  collection varchar(50) primary key
);

create table Projects (
  project_id int not null auto_increment primary key,
  project_axis int,
  project_objective int,
  project_acronym varchar(30) UNIQUE,
  project_label varchar(300),
  operation_summary varchar(2000),
  call_for_proposals varchar(20),
  start_date date,
  end_date date,
  project_type varchar(50),
  erdf double(14,2),
  ipa_funds double(14,2),
  project_amount double(14,2),
  cofinancing_rate double(14,2),
  project_status varchar(20),
  project_deliverables_url varchar(300),
  project_community varchar(50),
  foreign key (project_community) references Communities(collection)
);

create table Partners (
  partner_id int not null auto_increment primary key,
  ISLP boolean,
  partner_name varchar(250) UNIQUE,
  partner_nature varchar(50),
  partner_country varchar(30),
  partner_postalCode varchar(30),
  partner_area varchar(100),
  nuts3 varchar(150),
  erdf double,
  erdfContribution double,
  ipa double,
  ipaContribution double,
  amount double
);

create table ProjectPartners (
  project_id int not null,
  project_partner_id int not null,
  foreign key (project_partner_id) references Partners(partner_id),
  foreign key (project_id) references Projects(project_id),
  primary key(project_id, project_partner_id)
);

create table Deliverables (
  deliverable_id int not null auto_increment primary key,
  deliverable_url varchar(300),
  deliverable_title varchar (200) UNIQUE,
  deliverable_date date,
  deliverable_description varchar(2000),
  deliverable_type varchar(150),
  deliverable_project_id int not null,
  deliverable_author_id int not null
  /*foreign key (deliverable_project_id) references Projects(project_id)
  foreign key (deliverable_author_id) references Partners(partner_id)*/
);

create table DeliverableKeywords (
  deliverable_id int not null primary key,
  foreign key (deliverable_id) references Deliverables(deliverable_id),
  related_keyword varchar(200) unique
);

create table DeliverableTargets (
  deliverable_id int not null primary key,
  foreign key (deliverable_id) references Deliverables(deliverable_id),
  deliverable_target varchar(200) unique
);
