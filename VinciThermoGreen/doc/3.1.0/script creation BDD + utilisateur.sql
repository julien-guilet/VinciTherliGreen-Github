CREATE USER 'vinciThermo'@'localhost' IDENTIFIED BY 'vinciThermo';
grant all privileges on vincithermogreen . * to 'vinciThermo'@'localhost';


CREATE DATABASE vincithermogreen;
use vincithermogreen;
/*==============================================================*/
/* Table : Stade                                                */
/*==============================================================*/
create table Stade
(
   nomStade             varchar(254)  comment ''
);


drop table if exists Mesure;

/*==============================================================*/
/* Table : Mesure                                               */
/*==============================================================*/
create table Mesure
(
   zone                 int  comment '',
   temperature          float  comment '',
   horoDate             datetime  comment '',
   nomStade             varchar(254)  comment ''
);

/*==============================================================*/
/* Table : User                                                 */
/*==============================================================*/
create table User
(
   login                varchar(254)  comment '',
   password          	varchar(254)  comment '',
   nom            	varchar(254)  comment '',
   prenom            	varchar(254)  comment '',
   type			varchar(254)  comment ''
);


ALTER TABLE stade
ADD PRIMARY KEY (nomStade); 

ALTER TABLE mesure
ADD PRIMARY KEY (zone, horoDate,nomStade,temperature); 
   
alter table Mesure 
ADD CONSTRAINT FK_nom_stade
FOREIGN KEY (nomStade) REFERENCES Stade(nomstade); 

