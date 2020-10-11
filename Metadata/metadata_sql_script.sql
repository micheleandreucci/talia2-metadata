drop database if exists Talia2;
CREATE DATABASE Talia2 CHARACTER SET utf8
  COLLATE utf8_general_ci;
use Talia2;

create table Partners (
  partner_id int not null auto_increment primary key,
  partner_name varchar(250) UNIQUE,
  partner_nature varchar(50),
  partner_country varchar(30),
  partner_postalCode varchar(30),
  partner_area varchar(100),
  nuts3 varchar(150),
  partner_city varchar(50), /*non vengono presi dal crawler*/
  partner_address varchar (100) /*non vengono presi dal crawler*/
);

create table Stakeholders (
  stakeholder_id int not null auto_increment primary key,
  organization_name varchar (100) UNIQUE,
  stakeholder_nature varchar(50),
  stakeholder_country varchar(30),
  stakeholder_postalCode varchar(10),
  stakeholder_area varchar(100),
  nuts3 varchar(150),
  stakeholder_city varchar(50),
  stakeholder_address varchar (100),
  stakeholder_web_address varchar(30),
  stakeholder_email varchar(30)
);

create table Communities (
  community_name varchar(50) primary key
);

create table StakeholderKeywords (
  stakeholder_id int not null,
  foreign key (stakeholder_id) references Stakeholders(stakeholder_id),
  related_keyword varchar(20),
  primary key(stakeholder_id, related_keyword)
);

create table Projects (
  project_id int not null auto_increment primary key,
  project_axis int,
  project_objective int,
  project_acronym varchar(30) UNIQUE,
  project_label varchar(300),
  operation_summary varchar(2000),
  /*lead partner?*/
  call_for_proposals varchar(15),
  start_date date,
  end_date date,
  project_type varchar(40),
  erdf double(14,2),
  ipa_funds double(14,2),
  project_amount double(14,2),
  cofinancing_rate double(14,2),
  project_status varchar(20),
  project_deliverables_url varchar(100),
  project_community varchar(50),
  foreign key (project_community) references Communities(community_name)
);

create table Deliverables (
  deliverable_id int not null auto_increment primary key,
  deliverable_url varchar(300),
  deliverable_title varchar (200) UNIQUE,
  deliverable_date date,
  deliverable_description varchar(400),
  deliverable_type varchar(30),
  deliverable_budget double(14,2), /*chiamarlo amount?*/
  deliverable_project_id int not null,
  deliverable_author_id int not null,  
  foreign key (deliverable_project_id) references Projects(project_id),
  foreign key (deliverable_author_id) references Partners(partner_id)
);

create table DeliverableKeywords (
  deliverable_id int not null,
  foreign key (deliverable_id) references Deliverables(deliverable_id),
  related_keyword varchar(60),
  primary key(deliverable_id, related_keyword)
);

create table DeliverableTargets (
  deliverable_id int not null,
  foreign key (deliverable_id) references Deliverables(deliverable_id),
  deliverable_target varchar(50),
  primary key(deliverable_id, deliverable_target)
);

create table ProjectPartners (
  project_id int not null,
  project_partner_id int not null,
  is_leadPartner boolean,
  partner_erdf double(14,2),
  partner_erdfContribution double(14,2),
  partner_ipaFunds double(14,2),
  partner_ipaContribution double(14,2),
  partner_amount double(14,2),
  foreign key (project_partner_id) references Partners(partner_id),
  foreign key (project_id) references Projects(project_id),
  primary key(project_id, project_partner_id)
);

create table ProjectStakeholders (
  project_id int not null,
  foreign key (project_id) references Projects(project_id),
  project_stakeholder_id int not null,
  foreign key(project_stakeholder_id) references Stakeholders(stakeholder_id),
  primary key(project_id, project_stakeholder_id)
);


/*QUERY
select p.project_acronym,part.partner_name from partners part inner join projectpartners pp inner join projects p on part.partner_id=pp.project_partner_id AND p.project_id=pp.project_id;

select nuts3 from partners group by nuts3;*/