use vincithermogreen;
insert into  user values
("jguilet", "$2a$10$vFUdAMCBgMmDhzSDrvwPzOiRiFxdkVIKPAO4yrw8WoRoGGCZtNCpC", "guilet", "julien", "admin", '+33673313263'),
("mguessaz", "$2a$10$vFUdAMCBgMmDhzSDrvwPzOiRiFxdkVIKPAO4yrw8WoRoGGCZtNCpC", "guessaz", "manu", "admin", '+33673313263'),
("sgrousset", "$2a$10$vFUdAMCBgMmDhzSDrvwPzOiRiFxdkVIKPAO4yrw8WoRoGGCZtNCpC", "grousset", "sylvain", "membre" ,'+33673313263'),
("mwacquiez", "$2a$10$vFUdAMCBgMmDhzSDrvwPzOiRiFxdkVIKPAO4yrw8WoRoGGCZtNCpC", "wacquiez", "max", "membre", '+33673313263'),
("amassa", "$2a$10$vFUdAMCBgMmDhzSDrvwPzOiRiFxdkVIKPAO4yrw8WoRoGGCZtNCpC", "massa", "alexis", "membre", '+33673313263');

INSERT INTO stade VALUES
 ('Stade de Lyon', 50, 80, 'jguilet',"2020-09-2 15:00:00" ),
 ('Stade de Paris', 50, 80, 'jguilet',"2020-04-10 14:10:00" ),
 ('Stade de Marseille', 50, 80, 'jguilet',"2020-01-10 4:00:00" );

INSERT INTO mesure (zone,temperature, horoDate, nomStade)VALUES 

(1,72,"2020-09-2 15:00:00","Stade de Lyon"),
(1,73,"2020-10-2 15:00:00","Stade de Lyon"),
(2,75,"2020-10-15 15:30:00",'Stade de Lyon'),
(3,76,"2020-11-18 18:00:00",'Stade de Lyon'),

(1,68,"2020-04-10 14:10:00",'Stade de Paris'),
(2,67,"2020-05-20 18:00:00",'Stade de Paris'),
(3,68.5,"2020-06-22 05:00:00",'Stade de Paris'),

(1,69,"2020-01-10 4:00:00",'Stade de Marseille'),
(2,70,"2020-10-12 8:00:00",'Stade de Marseille'),
(3,72,"2020-11-12 9:00:00",'Stade de Marseille');


