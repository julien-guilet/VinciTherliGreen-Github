CREATE USER 'vinciThermo'@'localhost' IDENTIFIED BY 'vinciThermo';
grant all privileges on vincithermogreen . * to 'vinciThermo'@'localhost';


CREATE DATABASE vincithermogreen;
use vincithermogreen;
/*==============================================================*/
/* Table : Stade                                                */
/*==============================================================*/
create table Stade
(
   nomStade             varchar(254)  comment '' primary key,
   temp_min 		int	comment'',
   temp_max		int 	comment'',
   gerant	varchar(254) comment'',
   dateTemp	datetime comment ''
);
drop table if exists Mesure;

/*==============================================================*/
/* Table : Mesure                                               */
/*==============================================================*/
create table Mesure
(
   idMesure int auto_increment primary key,
   zone int  comment '',
   temperature float  comment '',
   horoDate datetime  comment '',
   nomStade varchar(254)  comment ''
);

   


/*==============================================================*/
/* Table : User                                                 */
/*==============================================================*/
create table User
(
   login                varchar(254)  comment '' primary key,
   password          	varchar(254)  comment '',
   nom            	varchar(254)  comment '',
   prenom            	varchar(254)  comment '',
   type			varchar(254)  comment '',
   tel	varchar(12) comment''
);

drop table if exists alerte;
/*==============================================================*/
/* Table : Alerte                                               */
/*==============================================================*/

create table Alerte
(
   idAlert int auto_increment primary key,
   libelle_aler			varchar(254)  comment ''
);

alter table alerte 
add constraint fk_nom_stade_alerte
foreign key(idAlert) references mesure(idMesure);

alter table stade 
add constraint fk_nom_stade_gerant
foreign key(gerant) references user(login);

alter table Mesure 
ADD CONSTRAINT FK_nom_stade
FOREIGN KEY (nomStade) REFERENCES Stade(nomstade); 

