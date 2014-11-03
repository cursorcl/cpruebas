-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.25a


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema cpruebas
--

CREATE DATABASE IF NOT EXISTS cpruebas;
USE cpruebas;

--
-- Definition of table `alumno`
--

DROP TABLE IF EXISTS `alumno`;
CREATE TABLE `alumno` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PATERNO` varchar(255) DEFAULT NULL,
  `MATERNO` varchar(255) DEFAULT NULL,
  `RUT` varchar(255) DEFAULT NULL,
  `DIRECCION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `CURSO_ID` bigint(20) DEFAULT NULL,
  `COLEGIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ALUMNO_CURSO_ID` (`CURSO_ID`),
  KEY `FK_ALUMNO_COLEGIO_ID` (`COLEGIO_ID`),
  CONSTRAINT `FK_ALUMNO_COLEGIO_ID` FOREIGN KEY (`COLEGIO_ID`) REFERENCES `colegio` (`ID`),
  CONSTRAINT `FK_ALUMNO_CURSO_ID` FOREIGN KEY (`CURSO_ID`) REFERENCES `curso` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=204 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `alumno`
--

/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (1,'Osorio','Verdugo','10613781-1','Su casa','Eliecer',1,1),
 (2,'Farías','Zavala','12623503-8','Su Casa','Susan',2,1),
 (3,'FARIAS','ZAVALA','12623502-K','Cerro Almud','URSULA',4,1),
 (112,'PINTO','MIRANDA','22019890-1',' ','CATALINA R.',1,1),
 (113,'ALARCON','VALLEJOS','22339962-2',' ','BASTIAN',2,1),
 (114,'ORELLANA ','MIRANDA','22015000-3',' ','YARITZA MILLARAY',2,1),
 (115,'TRONCOSO','  ','22243892-6',' ','BENJAMIN EDUARDO',2,1),
 (116,'MOLINA','MIRANDA','21818189-9',' ','GISELA ANTONELLA',3,1),
 (117,'LANDEROS ','CHAMORRO','21653596-0',' ','CRISTIAN E.',4,1),
 (118,'MORAGA ','SALGADO','21749583-0',' ','MARÍA JOSÉ',4,1),
 (119,'OSES ','MUÑOZ','21283988-4',' ','JOSÉ IGNACIO',4,1),
 (120,'PLAZA ','SILVA','20498256-2',' ','LUIS A.',5,1),
 (121,'MORAGA',' SALAZAR','20877094-2',' ','PAOLA J',6,1),
 (122,'VALLEJOS ','ANRÍQUEZ','20953235-2',' ','MIREYA E',6,1),
 (123,'GALDAMES ',' ','22363872-4',' ','LUCAS PATRICIO',8,2),
 (124,'LUNA ','ORELLANA','21735888-4',' ','CATALINA MINELVA',12,2);
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (125,'PINTO',' RÍOS','21578183-1',' ','SCARLETH ALEJANDRA',13,2),
 (126,'GALDAMES',' PEREIRA','21309451-3',' ','DEBORA VICTORIA',14,2),
 (127,'LARA',' SUAZO','22633410-6',' ','LUCAS BORYS',17,3),
 (128,'MUÑOZ',' ARAVENA','22305789-6',' ','CLAUDIA PAMELA',18,3),
 (129,'ANDRADE',' VEGA','21778764-5',' ','ELÍAS JACOB',20,3),
 (130,'OSORES',' LARA','21443744-9',' ','NADIA ESTER',21,3),
 (131,'OSORES',' LARA','21377568-5',' ','RODRIGO ALEXIS',21,3),
 (132,'VELOZO ','GARCÍA','21554906-2',' ','BELÉN ALEJANDRA',21,3),
 (133,'LARA ','SUAZO','21154084-2',' ','VIVIANA BELÉN',22,3),
 (134,'SUAZO ','LANDERO','21145523-3',' ','JENNIFER YASMÍN',22,3),
 (135,'ESPINOZA',' CONCHA','22406318-0',' ','BENJAMIN ALONSO',25,4),
 (136,'PEREIRA ','PEREZ','22472574-4',' ','MARCELO ANDRES',25,4),
 (137,'MUÑOZ ','PARRA','22013494-6',' ','CATALINA DEL CARMEN',26,4),
 (138,'GUTIERREZ',' VALDEBENITO','21927510-2',' ','KATHERINE ALEJANDRA',27,4),
 (139,'CAMPOS ','BRAVO','21731848-3',' ','PRISCILA ANGELINE',27,4);
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (140,'MORAGA',' MUENA','21771948-8',' ','MARTIN ALBERTO',28,4),
 (141,'CONCHA ','QUINTANA','20274113-4',' ','VICTOR MANUEL',30,4),
 (142,'AMIGO',' PALMA','22431434-5',' ','MARCELO JULIAN',33,5),
 (143,'DE LA FUENTE',' TAPIA','22448542-5',' ','JAVIERA IGNACIA',33,5),
 (144,'LUNA',' SOTO ','22254206-5',' ','ANIBAL ABRAHAM',33,5),
 (145,'MORA',' SOTO ','22447634-5',' ','ELIA DEL CARMEN',33,5),
 (146,'ARAVENA',' CHAMORRO','22288695-3',' ','MARCO ANDRES',34,5),
 (147,'ARAVENA ','SALGADO','22351722-7',' ','MARIA JOSE',34,5),
 (148,'MANRIQUEZ',' CHAMORRO','22276072-0',' ','FLORENCIA VALENTINA',34,5),
 (149,'SANTIBAÑEZ',' ARCE','22250863-0',' ','IGNACIO MAXIMILIANO',34,5),
 (150,'PEREZ ','SOTO','21905853-2',' ','FRANCISCO GUSTAVO',35,5),
 (151,'RODRIGUEZ',' ESCALONA','21859404-2',' ','BASTIAN EXEQUIEL',35,5),
 (152,'TORRES ','COLOMA','22059759-8',' ','JAVIERA PAZ',35,5),
 (153,'VALENZUELA',' DÍAZ ','21712230-9',' ','ARTURO ANTONIO',35,5),
 (154,'COLOMA',' FRITZ','21410581-0',' ','CESAR ANTONIO',37,5);
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (155,'ESCALONA',' SOTO','20273764-1',' ','MAXIMILIANO ANDRES',38,5),
 (156,'VALENZUELA',' REBOLLEDO','21237922-0',' ','CARLALI DEL CARMEN',38,5),
 (157,'DE LA FUENTE',' TAPIA','22448542-5',' ',' JAVIERA IGNACIA',233,5),
 (158,'FERNANDEZ',' FERNANDEZ','22676342-2',' ','DANIEL MATIAS',39,16),
 (159,'NUÑEZ',' GUERRA','22611659-1',' ','ELIZABETH CONSTANZA',39,16),
 (160,'NUÑEZ ',' GUERRA','21998816-8',' ','NIBIA ALEJANDRA',40,16),
 (161,'LATORRE',' ORTIZ','20569933-3',' ','BASTIAN ANDRES',41,17),
 (162,'SALGADO ','PÉREZ','20570084-6',' ','DAMIAN PATRICIO',41,17),
 (163,'TORRES',' NAVARRETE','20090501-6',' ','FERNANDO ANTONIO',41,17),
 (164,'ANDRADES',' FRIZ','22018219-3',' ','CARLA ANDREA',42,17),
 (165,'LOPEZ',' LOPEZ ','22248087-6',' ','YERKO ANTONIO',42,17),
 (166,'PEREZ ','DIAZ','22140531-5',' ','HUGO ANTONIO',42,17),
 (167,'FUENTES',' SALGADO','22053619-K',' ','FERNANDA GRACIELA',43,17),
 (168,'LANDEROS ','CANCINO','21881724-6',' ','TRINIDAD DE LOS ANGELES',43,17);
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (169,'MUÑOZ',' ARAVENA','21920512-0',' ','JAIRO DAMIÁN',43,17),
 (170,'MUÑOZ ','HERNÁNDEZ','22076289-0',' ','CAROLINA NICOLE',43,17),
 (171,'PARRA ','ARAVENA','21934073-7',' ','BENJAMIN ALEXANDRO',43,17),
 (172,'VÁSQUEZ ','PEÑA','22116537-3',' ','CATALINA POLET',43,17),
 (173,'CONTRERAS ',' AGURTO','21708369-1',' ','SEBASTIAN IGNACIO',44,17),
 (174,'FUENTES  ','SALGADO ','21629013-K',' ','MARTINA FLORENCIA',44,17),
 (175,'GUEVARA',' VÁSQUEZ','21795935-7',' ','FLORDI NALLELY',44,17),
 (176,'MEZA',' HENRÍQUEZ','21073695-K',' ','BRAYAN ANDRES',44,17),
 (177,'OBREGÓN',' MORAGA','21193762-9',' ','ANDREA ALEJANDRA',44,17),
 (178,'RETAMAL',' TORRES','21776546-3',' ','BENJAMIN NICOLAS',44,17),
 (179,'ROMERO',' ESPINOZA','21858874-3',' ','MARCELO ENOC',44,17),
 (180,'ROMERO',' FUENTES','21652651-1',' ','VALESCA ALEJANDRA',44,17),
 (181,'LANDEROS ','CANCINO','21038382-4',' ','YAMILET DEL CARMEN',45,17),
 (182,'RETAMAL ','TORRES','21178430-K',' ','ROCIO BELEN',45,17);
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (183,'ROMERO ','ESPINOZA','21247915-2',' ','MARCO ESTEBAN',45,17),
 (184,'ANDRADES ','FRITZ','20767286-6',' ','JUAN PABLO',46,17),
 (185,'GUEVARA ','VÁSQUEZ','21146963-3',' ','SIGRID SHIRLEY',46,17),
 (186,'HERNÁNDEZ ','SÁNCHEZ','20077003-9',' ','KRISHNA LISSETH',46,17),
 (187,'LÓPEZ ','APARICIO','21124454-2',' ','VICTOR ANDRES',46,17),
 (188,'GUEVARA ','VÁSQUEZ','20570497-3',' ','CLAUDIO GEORGE',47,17),
 (189,'PACHECO ',' VALENZUELA','21024480-8',' ','NICOLAS HERNAN',47,17),
 (190,'LATORRE ','ORTIZ','20569933-3',' ','BASTIAN ANDRES',48,17),
 (191,'SALGADO',' PÉREZ','20570084-6',' ','DAMIAN PATRICIO',48,17),
 (192,'TORRES ','NAVARRETE','20090501-6',' ','FERNANDO ANTONIO',48,17),
 (193,'AMAYA ','LABRA','22528889-5',' ','JOSUE JACOB',49,18),
 (194,'VELOSO','LABRA','22485215-0',' ','MIREYA A.',49,18),
 (195,'AMAYA ','LABRA','21865721-4',' ','MOISES ABRAHAM',50,18),
 (196,'GALLARDO',' MIRANDA','21185208-9',' ','MARIA JOSE',51,18),
 (197,'LEAL ','MIRANDA','21130572-K',' ','ERNESTO DANIEL',52,18);
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (198,'MARILEO',' MUÑOZ','21198558-5',' ','JUAN PABLO',52,18),
 (199,'Osorio','Espinoza','21652190-0','Su casa','Eliecer Antonio',234,19),
 (200,'Osorio','Espinoza','22148346-4','Su casa','Evelyn Elisa',234,19),
 (201,'Osorio','Verdugo','10613781-1','Su casa','Eliecer Enrique',234,19),
 (202,'Osorio','Espinoza','21021606-5','Su casa','Viviana Belén',234,19),
 (203,'Farías','Zavala','12623503-8','Su casa','Susan Angela',234,19);
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;


--
-- Definition of table `asignatura`
--

DROP TABLE IF EXISTS `asignatura`;
CREATE TABLE `asignatura` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `asignatura`
--

/*!40000 ALTER TABLE `asignatura` DISABLE KEYS */;
INSERT INTO `asignatura` (`ID`,`NAME`) VALUES 
 (1,'Lenguaje modificado'),
 (2,'Matemáticas'),
 (3,'Sociedad'),
 (4,'Naturaleza'),
 (5,'Francés'),
 (7,'Inglés'),
 (9,'Chino Mandarin'),
 (13,'Quimica'),
 (14,'Castellano'),
 (15,'NAME'),
 (16,'Asignatura A1'),
 (17,'Asignatura A2'),
 (18,'Asignatura A3'),
 (19,'Asignatura A4'),
 (20,'Asignatura A5'),
 (21,'Asignatura A6'),
 (22,'Asignatura A7'),
 (23,'Asignatura A8'),
 (24,'Asignatura A9'),
 (25,'Asignatura A10'),
 (26,'Asignatura A11'),
 (27,'Asignatura A12'),
 (28,'Nombre'),
 (29,'Matematicas'),
 (30,'Nombre'),
 (31,'Matematicas');
/*!40000 ALTER TABLE `asignatura` ENABLE KEYS */;


--
-- Definition of table `ciclo`
--

DROP TABLE IF EXISTS `ciclo`;
CREATE TABLE `ciclo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ciclo`
--

/*!40000 ALTER TABLE `ciclo` DISABLE KEYS */;
INSERT INTO `ciclo` (`ID`,`NAME`) VALUES 
 (1,'Ciclo 1'),
 (2,'Ciclo 2'),
 (3,'Ciclo 3'),
 (4,'Ciclo 4'),
 (5,'NAME'),
 (6,'Ciclo C'),
 (7,'Ciclo D'),
 (8,'Ciclo E');
/*!40000 ALTER TABLE `ciclo` ENABLE KEYS */;


--
-- Definition of table `colegio`
--

DROP TABLE IF EXISTS `colegio`;
CREATE TABLE `colegio` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DIRECCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `colegio`
--

/*!40000 ALTER TABLE `colegio` DISABLE KEYS */;
INSERT INTO `colegio` (`ID`,`NAME`,`DIRECCION`) VALUES 
 (1,'ESCUELA CHORRILLOS',''),
 (2,'ESCUELA GUSTAVO ILUFÍ',''),
 (3,'ESCUELA LA CAPILLA DE PILÉN',''),
 (4,'ESCUELA MARÍA RUIZ MARTÍNEZ',''),
 (5,'ESCUELA MIXTA ATENEA',''),
 (16,'ESCUELA PADRE HURTADO',''),
 (17,'ESCUELA PURISIMA CONCEPCION DE POCILLAS',''),
 (18,'ESCUELA RINCÓN DE PILÉN',''),
 (19,'Colegio 1','Colegio 1');
/*!40000 ALTER TABLE `colegio` ENABLE KEYS */;


--
-- Definition of table `curso`
--

DROP TABLE IF EXISTS `curso`;
CREATE TABLE `curso` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `TIPOCURSO_ID` bigint(20) DEFAULT NULL,
  `CICLO_ID` bigint(20) DEFAULT NULL,
  `COLEGIO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CURSO_COLEGIO_ID` (`COLEGIO_ID`),
  KEY `FK_CURSO_CICLO_ID` (`CICLO_ID`),
  KEY `FK_CURSO_TIPOCURSO_ID` (`TIPOCURSO_ID`),
  CONSTRAINT `FK_CURSO_CICLO_ID` FOREIGN KEY (`CICLO_ID`) REFERENCES `ciclo` (`ID`),
  CONSTRAINT `FK_CURSO_COLEGIO_ID` FOREIGN KEY (`COLEGIO_ID`) REFERENCES `colegio` (`ID`),
  CONSTRAINT `FK_CURSO_TIPOCURSO_ID` FOREIGN KEY (`TIPOCURSO_ID`) REFERENCES `tipocurso` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `curso`
--

/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` (`ID`,`NAME`,`TIPOCURSO_ID`,`CICLO_ID`,`COLEGIO_ID`) VALUES 
 (1,'Primero',1,1,1),
 (2,'Segundo',2,1,1),
 (3,'Tercero',3,1,1),
 (4,'Cuarto',4,1,1),
 (5,'Quinto',5,2,1),
 (6,'Sexto',6,2,1),
 (7,'Primero',1,1,2),
 (8,'Segundo',2,1,2),
 (11,'Tercero',3,1,2),
 (12,'Cuarto',4,1,2),
 (13,'Quinto',5,2,2),
 (14,'Sesto',6,2,2),
 (15,'Sétimo',7,2,2),
 (16,'Octavo',8,2,2),
 (17,'Primero',1,1,3),
 (18,'Segundo',2,1,3),
 (19,'Tercero',3,1,3),
 (20,'Cuarto',4,1,3),
 (21,'Quinto',5,2,3),
 (22,'Sexto',6,2,3),
 (23,'Séptimo',7,2,3),
 (24,'Octavo',8,2,3),
 (25,'Primero',1,1,4),
 (26,'Segundo',2,1,4),
 (27,'Tercero',3,1,4),
 (28,'Cuarto',4,1,4),
 (29,'Quinto',5,2,4),
 (30,'Sexto',6,2,4),
 (31,'Séptimo',7,2,4),
 (32,'Octavo',8,2,4),
 (33,'Primero',1,1,5),
 (34,'Segundo',2,1,5),
 (35,'Tercero',3,1,5),
 (36,'Cuarto',4,1,5),
 (37,'Quinto',5,2,5),
 (38,'Sexto',6,2,5),
 (39,'Primero',1,1,16),
 (40,'Tercero',3,1,16),
 (41,'Primero',1,1,17),
 (42,'Segundo',2,1,17),
 (43,'Tercero',3,1,17),
 (44,'Cuarto',4,1,17);
INSERT INTO `curso` (`ID`,`NAME`,`TIPOCURSO_ID`,`CICLO_ID`,`COLEGIO_ID`) VALUES 
 (45,'Quinto',5,2,17),
 (46,'Sexto',6,2,17),
 (47,'Séptimo',7,2,17),
 (48,'Octavo',8,2,17),
 (49,'Primero',1,1,18),
 (50,'Cuarto',4,1,18),
 (51,'Quinto',5,2,18),
 (52,'Sexto',6,2,18),
 (233,'Séptimo',7,2,5),
 (234,'Primero A',1,1,19);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;


--
-- Definition of table `curso_alumno`
--

DROP TABLE IF EXISTS `curso_alumno`;
CREATE TABLE `curso_alumno` (
  `curso_ID` bigint(20) NOT NULL,
  `alumnos_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`curso_ID`,`alumnos_ID`),
  KEY `FK_CURSO_ALUMNO_alumnos_ID` (`alumnos_ID`),
  CONSTRAINT `FK_CURSO_ALUMNO_alumnos_ID` FOREIGN KEY (`alumnos_ID`) REFERENCES `alumno` (`ID`),
  CONSTRAINT `FK_CURSO_ALUMNO_curso_ID` FOREIGN KEY (`curso_ID`) REFERENCES `curso` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `curso_alumno`
--

/*!40000 ALTER TABLE `curso_alumno` DISABLE KEYS */;
/*!40000 ALTER TABLE `curso_alumno` ENABLE KEYS */;


--
-- Definition of table `ejetematico`
--

DROP TABLE IF EXISTS `ejetematico`;
CREATE TABLE `ejetematico` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `ASIGNATURA_ID` bigint(20) DEFAULT NULL,
  `TIPOPRUEBA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_EJETEMATICO_ASIGNATURA_ID` (`ASIGNATURA_ID`),
  KEY `FK_EJETEMATICO_TIPOPRUEBA_ID` (`TIPOPRUEBA_ID`),
  CONSTRAINT `FK_EJETEMATICO_ASIGNATURA_ID` FOREIGN KEY (`ASIGNATURA_ID`) REFERENCES `asignatura` (`ID`),
  CONSTRAINT `FK_EJETEMATICO_TIPOPRUEBA_ID` FOREIGN KEY (`TIPOPRUEBA_ID`) REFERENCES `tipoprueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ejetematico`
--

/*!40000 ALTER TABLE `ejetematico` DISABLE KEYS */;
INSERT INTO `ejetematico` (`ID`,`NAME`,`ASIGNATURA_ID`,`TIPOPRUEBA_ID`) VALUES 
 (1,'EJE 1',16,1),
 (2,'EJE 2',1,1),
 (3,'EJE 3',2,1),
 (4,'EJE 4',2,1),
 (5,'Comprensión A1',16,1);
/*!40000 ALTER TABLE `ejetematico` ENABLE KEYS */;


--
-- Definition of table `evaluacionejetematico`
--

DROP TABLE IF EXISTS `evaluacionejetematico`;
CREATE TABLE `evaluacionejetematico` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `NRORANGOMIN` float DEFAULT NULL,
  `NRORANGOMAX` float DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `evaluacionejetematico`
--

/*!40000 ALTER TABLE `evaluacionejetematico` DISABLE KEYS */;
INSERT INTO `evaluacionejetematico` (`ID`,`NAME`,`NRORANGOMIN`,`NRORANGOMAX`) VALUES 
 (1,'Adecuado',75,100),
 (2,'Elemental',50,74),
 (3,'Insuficiente',1,49);
/*!40000 ALTER TABLE `evaluacionejetematico` ENABLE KEYS */;


--
-- Definition of table `evaluacionprueba`
--

DROP TABLE IF EXISTS `evaluacionprueba`;
CREATE TABLE `evaluacionprueba` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  `FECHA` bigint(20) DEFAULT NULL,
  `CURSO_ID` bigint(20) DEFAULT NULL,
  `PROFESOR_ID` bigint(20) DEFAULT NULL,
  `COLEGIO_ID` bigint(20) DEFAULT NULL,
  `PRUEBA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_EVALUACIONPRUEBA_COLEGIO_ID` (`COLEGIO_ID`),
  KEY `FK_EVALUACIONPRUEBA_PRUEBA_ID` (`PRUEBA_ID`),
  KEY `FK_EVALUACIONPRUEBA_CURSO_ID` (`CURSO_ID`),
  KEY `FK_EVALUACIONPRUEBA_PROFESOR_ID` (`PROFESOR_ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_COLEGIO_ID` FOREIGN KEY (`COLEGIO_ID`) REFERENCES `colegio` (`ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_CURSO_ID` FOREIGN KEY (`CURSO_ID`) REFERENCES `curso` (`ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_PROFESOR_ID` FOREIGN KEY (`PROFESOR_ID`) REFERENCES `profesor` (`ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_PRUEBA_ID` FOREIGN KEY (`PRUEBA_ID`) REFERENCES `prueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `evaluacionprueba`
--

/*!40000 ALTER TABLE `evaluacionprueba` DISABLE KEYS */;
INSERT INTO `evaluacionprueba` (`ID`,`NAME`,`FECHA`,`CURSO_ID`,`PROFESOR_ID`,`COLEGIO_ID`,`PRUEBA_ID`) VALUES 
 (3,'Asignatura A1 ESCUELA CHORRILLOS Cuarto 2014-10-31',16374,4,2,1,20);
/*!40000 ALTER TABLE `evaluacionprueba` ENABLE KEYS */;


--
-- Definition of table `evaluacionprueba_pruebarendida`
--

DROP TABLE IF EXISTS `evaluacionprueba_pruebarendida`;
CREATE TABLE `evaluacionprueba_pruebarendida` (
  `evaluacionprueba_ID` bigint(20) NOT NULL,
  `pruebasRendidas_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`evaluacionprueba_ID`,`pruebasRendidas_ID`),
  KEY `EVALUACIONPRUEBA_PRUEBARENDIDA_pruebasRendidas_ID` (`pruebasRendidas_ID`),
  CONSTRAINT `EVALUACIONPRUEBA_PRUEBARENDIDA_evaluacionprueba_ID` FOREIGN KEY (`evaluacionprueba_ID`) REFERENCES `evaluacionprueba` (`ID`),
  CONSTRAINT `EVALUACIONPRUEBA_PRUEBARENDIDA_pruebasRendidas_ID` FOREIGN KEY (`pruebasRendidas_ID`) REFERENCES `pruebarendida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `evaluacionprueba_pruebarendida`
--

/*!40000 ALTER TABLE `evaluacionprueba_pruebarendida` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacionprueba_pruebarendida` ENABLE KEYS */;


--
-- Definition of table `formas`
--

DROP TABLE IF EXISTS `formas`;
CREATE TABLE `formas` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FORMA` int(11) DEFAULT NULL,
  `ORDEN` varchar(255) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `PRUEBA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_FORMAS_PRUEBA_ID` (`PRUEBA_ID`),
  CONSTRAINT `FK_FORMAS_PRUEBA_ID` FOREIGN KEY (`PRUEBA_ID`) REFERENCES `prueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formas`
--

/*!40000 ALTER TABLE `formas` DISABLE KEYS */;
INSERT INTO `formas` (`ID`,`FORMA`,`ORDEN`,`NAME`,`PRUEBA_ID`) VALUES 
 (24,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20','Forma 1',20),
 (30,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35','Forma 1',22),
 (31,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45','Forma 1',23),
 (32,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30','Forma 1',24),
 (33,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30','Forma 1',25);
/*!40000 ALTER TABLE `formas` ENABLE KEYS */;


--
-- Definition of table `habilidad`
--

DROP TABLE IF EXISTS `habilidad`;
CREATE TABLE `habilidad` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `habilidad`
--

/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` (`ID`,`NAME`,`DESCRIPCION`) VALUES 
 (1,'Habilidad 1','Habilidad 1'),
 (2,'Habilidad 2','Habilidad 2'),
 (3,'Habilidad 3','Habilidad 3'),
 (4,'Habilidad 4','Habilidad 4'),
 (9,'Habilidad 5','Habilidad 5'),
 (10,'Habilidad 6','Habilidad 6');
/*!40000 ALTER TABLE `habilidad` ENABLE KEYS */;


--
-- Definition of table `nivelevaluacion`
--

DROP TABLE IF EXISTS `nivelevaluacion`;
CREATE TABLE `nivelevaluacion` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `NRORANGOS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `nivelevaluacion`
--

/*!40000 ALTER TABLE `nivelevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion` (`ID`,`NAME`,`NRORANGOS`) VALUES 
 (1,'Bajo/Medio Bajo/Medio Alto/Alto',4);
/*!40000 ALTER TABLE `nivelevaluacion` ENABLE KEYS */;


--
-- Definition of table `nivelevaluacion_rangoevaluacion`
--

DROP TABLE IF EXISTS `nivelevaluacion_rangoevaluacion`;
CREATE TABLE `nivelevaluacion_rangoevaluacion` (
  `nivelevaluacion_ID` bigint(20) NOT NULL,
  `rangos_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`nivelevaluacion_ID`,`rangos_ID`),
  KEY `FK_NIVELEVALUACION_RANGOEVALUACION_rangos_ID` (`rangos_ID`),
  CONSTRAINT `FK_NIVELEVALUACION_RANGOEVALUACION_rangos_ID` FOREIGN KEY (`rangos_ID`) REFERENCES `rangoevaluacion` (`ID`),
  CONSTRAINT `NIVELEVALUACION_RANGOEVALUACION_nivelevaluacion_ID` FOREIGN KEY (`nivelevaluacion_ID`) REFERENCES `nivelevaluacion` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `nivelevaluacion_rangoevaluacion`
--

/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion_rangoevaluacion` (`nivelevaluacion_ID`,`rangos_ID`) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,4);
/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` ENABLE KEYS */;


--
-- Definition of table `profesor`
--

DROP TABLE IF EXISTS `profesor`;
CREATE TABLE `profesor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `PATERNO` varchar(255) DEFAULT NULL,
  `RUT` varchar(255) DEFAULT NULL,
  `MATERNO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `profesor`
--

/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` (`ID`,`NAME`,`PATERNO`,`RUT`,`MATERNO`) VALUES 
 (1,'Eliecer','Osorio','10613781-1','Verdugo'),
 (2,'Susan','Farías','12623503-8','Zavala'),
 (3,'Mauricio','Salgado','9410614-1','Caro'),
 (4,'Ursula','Farías','12623502-k','Zavala');
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;


--
-- Definition of table `prueba`
--

DROP TABLE IF EXISTS `prueba`;
CREATE TABLE `prueba` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EXIGENCIA` int(11) DEFAULT NULL,
  `ALTERNATIVAS` int(11) DEFAULT NULL,
  `FECHA` bigint(20) DEFAULT NULL,
  `PUNTAJEBASE` int(11) DEFAULT NULL,
  `NROFORMAS` int(11) DEFAULT NULL,
  `RESPONSES` varchar(255) DEFAULT NULL,
  `NROPREGUNTAS` int(11) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `ASIGNATURA_ID` bigint(20) DEFAULT NULL,
  `CURSO_ID` bigint(20) DEFAULT NULL,
  `TIPOPRUEBA_ID` bigint(20) DEFAULT NULL,
  `PROFESOR_ID` bigint(20) DEFAULT NULL,
  `NIVELEVALUACION_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRUEBA_ASIGNATURA_ID` (`ASIGNATURA_ID`),
  KEY `FK_PRUEBA_NIVELEVALUACION_ID` (`NIVELEVALUACION_ID`),
  KEY `FK_PRUEBA_TIPOPRUEBA_ID` (`TIPOPRUEBA_ID`),
  KEY `FK_PRUEBA_PROFESOR_ID` (`PROFESOR_ID`),
  KEY `FK_PRUEBA_CURSO_ID` (`CURSO_ID`),
  CONSTRAINT `FK_PRUEBA_ASIGNATURA_ID` FOREIGN KEY (`ASIGNATURA_ID`) REFERENCES `asignatura` (`ID`),
  CONSTRAINT `FK_PRUEBA_CURSO_ID` FOREIGN KEY (`CURSO_ID`) REFERENCES `tipocurso` (`ID`),
  CONSTRAINT `FK_PRUEBA_NIVELEVALUACION_ID` FOREIGN KEY (`NIVELEVALUACION_ID`) REFERENCES `nivelevaluacion` (`ID`),
  CONSTRAINT `FK_PRUEBA_PROFESOR_ID` FOREIGN KEY (`PROFESOR_ID`) REFERENCES `profesor` (`ID`),
  CONSTRAINT `FK_PRUEBA_TIPOPRUEBA_ID` FOREIGN KEY (`TIPOPRUEBA_ID`) REFERENCES `tipoprueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prueba`
--

/*!40000 ALTER TABLE `prueba` DISABLE KEYS */;
INSERT INTO `prueba` (`ID`,`EXIGENCIA`,`ALTERNATIVAS`,`FECHA`,`PUNTAJEBASE`,`NROFORMAS`,`RESPONSES`,`NROPREGUNTAS`,`NAME`,`ASIGNATURA_ID`,`CURSO_ID`,`TIPOPRUEBA_ID`,`PROFESOR_ID`,`NIVELEVALUACION_ID`) VALUES 
 (20,60,5,16374,1,1,'AAAAAAAAAAAAAAAAAAAA',20,'Prueba 3',16,4,1,2,1),
 (22,60,5,16376,1,1,'          VVVVVVVVVVAAAAAAAAAA     ',35,'Prueba 2',16,4,1,1,1),
 (23,60,4,16376,1,1,'ACBBVBC ABAVAAVVV  ACBVVAVAA V ACBAABCVF ABCF',45,'Prueba TETS',16,4,1,1,1),
 (24,60,5,16376,1,1,' BVBBBVB  BVBBBBBBVVCCFCCBVVBB',30,'Prueba REAL',1,1,1,1,1),
 (25,60,5,16377,1,1,'AAAAAAAAAAAAAABBBBBBBBBBBBBBBB',30,'Prueba Modi',16,4,1,1,1);
/*!40000 ALTER TABLE `prueba` ENABLE KEYS */;


--
-- Definition of table `pruebarendida`
--

DROP TABLE IF EXISTS `pruebarendida`;
CREATE TABLE `pruebarendida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OMITIDAS` int(11) DEFAULT NULL,
  `NOTA` float DEFAULT NULL,
  `RESPUESTAS` varchar(255) DEFAULT NULL,
  `FORMA` int(11) DEFAULT NULL,
  `MALAS` int(11) DEFAULT NULL,
  `BUENAS` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RANGO_ID` bigint(20) DEFAULT NULL,
  `ALUMNO_ID` bigint(20) DEFAULT NULL,
  `EVALUACIONPRUEBA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRUEBARENDIDA_ALUMNO_ID` (`ALUMNO_ID`),
  KEY `FK_PRUEBARENDIDA_EVALUACIONPRUEBA_ID` (`EVALUACIONPRUEBA_ID`),
  KEY `FK_PRUEBARENDIDA_RANGO_ID` (`RANGO_ID`),
  CONSTRAINT `FK_PRUEBARENDIDA_ALUMNO_ID` FOREIGN KEY (`ALUMNO_ID`) REFERENCES `alumno` (`ID`),
  CONSTRAINT `FK_PRUEBARENDIDA_EVALUACIONPRUEBA_ID` FOREIGN KEY (`EVALUACIONPRUEBA_ID`) REFERENCES `evaluacionprueba` (`ID`),
  CONSTRAINT `FK_PRUEBARENDIDA_RANGO_ID` FOREIGN KEY (`RANGO_ID`) REFERENCES `rangoevaluacion` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `pruebarendida`
--

/*!40000 ALTER TABLE `pruebarendida` DISABLE KEYS */;
INSERT INTO `pruebarendida` (`ID`,`OMITIDAS`,`NOTA`,`RESPUESTAS`,`FORMA`,`MALAS`,`BUENAS`,`NAME`,`RANGO_ID`,`ALUMNO_ID`,`EVALUACIONPRUEBA_ID`) VALUES 
 (7,0,1,'BBBBBBBBBBBBBBBBBBBB',1,20,0,NULL,1,117,3),
 (8,10,3.5,'OOOOOOOOOOAAAAAAAAAA',1,0,10,NULL,2,3,3),
 (9,0,7,'AAAAAAAAAAAAAAAAAAAA',1,0,20,NULL,4,119,3),
 (10,0,5.125,'AAAAAAAAAAAAAAABBBBB',1,5,15,NULL,3,118,3);
/*!40000 ALTER TABLE `pruebarendida` ENABLE KEYS */;


--
-- Definition of table `rangoevaluacion`
--

DROP TABLE IF EXISTS `rangoevaluacion`;
CREATE TABLE `rangoevaluacion` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ABREVIACION` varchar(255) DEFAULT NULL,
  `MINIMO` float DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `MAXIMO` float DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rangoevaluacion`
--

/*!40000 ALTER TABLE `rangoevaluacion` DISABLE KEYS */;
INSERT INTO `rangoevaluacion` (`ID`,`ABREVIACION`,`MINIMO`,`NAME`,`MAXIMO`) VALUES 
 (1,'B',0,'Bajo',39),
 (2,'MB',40,'Medio Bajo',65),
 (3,'MA',66,'Medio Alto',79),
 (4,'A',80,'Alto',100);
/*!40000 ALTER TABLE `rangoevaluacion` ENABLE KEYS */;


--
-- Definition of table `respuestasesperadasprueba`
--

DROP TABLE IF EXISTS `respuestasesperadasprueba`;
CREATE TABLE `respuestasesperadasprueba` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NUMERO` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `VERDADEROFALSO` tinyint(1) DEFAULT '0',
  `RESPUESTA` varchar(255) DEFAULT NULL,
  `MENTAL` tinyint(1) DEFAULT '0',
  `PRUEBA_ID` bigint(20) DEFAULT NULL,
  `HABILIDAD_ID` bigint(20) DEFAULT NULL,
  `EJETEMATICO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_RESPUESTASESPERADASPRUEBA_HABILIDAD_ID` (`HABILIDAD_ID`),
  KEY `FK_RESPUESTASESPERADASPRUEBA_EJETEMATICO_ID` (`EJETEMATICO_ID`),
  KEY `FK_RESPUESTASESPERADASPRUEBA_PRUEBA_ID` (`PRUEBA_ID`),
  CONSTRAINT `FK_RESPUESTASESPERADASPRUEBA_EJETEMATICO_ID` FOREIGN KEY (`EJETEMATICO_ID`) REFERENCES `ejetematico` (`ID`),
  CONSTRAINT `FK_RESPUESTASESPERADASPRUEBA_HABILIDAD_ID` FOREIGN KEY (`HABILIDAD_ID`) REFERENCES `habilidad` (`ID`),
  CONSTRAINT `FK_RESPUESTASESPERADASPRUEBA_PRUEBA_ID` FOREIGN KEY (`PRUEBA_ID`) REFERENCES `prueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=971 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `respuestasesperadasprueba`
--

/*!40000 ALTER TABLE `respuestasesperadasprueba` DISABLE KEYS */;
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (606,3,'3',0,'A',0,20,1,1),
 (607,18,'18',0,'A',0,20,2,5),
 (608,11,'11',0,'A',0,20,2,5),
 (609,14,'14',0,'A',0,20,2,5),
 (610,13,'13',0,'A',0,20,2,5),
 (611,19,'19',0,'A',0,20,2,5),
 (612,5,'5',0,'A',0,20,1,1),
 (613,17,'17',0,'A',0,20,2,5),
 (614,8,'8',0,'A',0,20,1,1),
 (615,15,'15',0,'A',0,20,2,5),
 (616,1,'1',0,'A',0,20,1,1),
 (617,6,'6',0,'A',0,20,1,1),
 (618,20,'20',0,'A',0,20,2,5),
 (619,10,'10',0,'A',0,20,1,1),
 (620,9,'9',0,'A',0,20,1,1),
 (621,16,'16',0,'A',0,20,2,5),
 (622,4,'4',0,'A',0,20,1,1),
 (623,7,'7',0,'A',0,20,1,1),
 (624,12,'12',0,'A',0,20,2,5),
 (625,2,'2',0,'A',0,20,1,1),
 (801,29,'29',0,'A',0,22,9,1),
 (802,22,'22',0,'A',0,22,9,1),
 (803,28,'28',0,'A',0,22,9,1),
 (804,2,'2',0,' ',1,22,9,1),
 (805,10,'10',0,' ',1,22,9,1),
 (806,18,'18',1,'V',0,22,9,1),
 (807,21,'21',0,'A',0,22,9,1),
 (808,17,'17',1,'V',0,22,9,1),
 (809,34,'34',0,' ',1,22,9,1);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (810,13,'13',1,'V',0,22,9,1),
 (811,35,'35',0,' ',1,22,9,1),
 (812,9,'9',0,' ',1,22,9,1),
 (813,31,'31',0,' ',1,22,9,1),
 (814,5,'5',0,' ',1,22,9,1),
 (815,4,'4',0,' ',1,22,9,1),
 (816,33,'33',0,' ',1,22,9,1),
 (817,30,'30',0,'A',0,22,9,1),
 (818,11,'11',1,'V',0,22,9,1),
 (819,24,'24',0,'A',0,22,9,1),
 (820,1,'1',0,' ',1,22,9,1),
 (821,3,'3',0,' ',1,22,9,1),
 (822,15,'15',1,'V',0,22,9,1),
 (823,12,'12',1,'V',0,22,9,1),
 (824,7,'7',0,' ',1,22,9,1),
 (825,19,'19',1,'V',0,22,9,1),
 (826,14,'14',1,'V',0,22,9,1),
 (827,20,'20',1,'V',0,22,9,1),
 (828,16,'16',1,'V',0,22,9,1),
 (829,8,'8',0,' ',1,22,9,1),
 (830,25,'25',0,'A',0,22,9,1),
 (831,23,'23',0,'A',0,22,9,1),
 (832,27,'27',0,'A',0,22,9,1),
 (833,32,'32',0,' ',1,22,9,1),
 (834,6,'6',0,' ',1,22,9,1),
 (835,26,'26',0,'A',0,22,9,1),
 (836,3,'3',0,'B',0,23,1,1),
 (837,18,'18',0,' ',1,23,2,5),
 (838,6,'6',0,'B',0,23,1,1);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (839,45,'45',1,'F',0,23,4,5),
 (840,41,'41',0,' ',1,23,4,5),
 (841,30,'30',1,'V',0,23,3,1),
 (842,7,'7',0,'C',0,23,1,1),
 (843,32,'32',0,'A',0,23,3,1),
 (844,19,'19',0,' ',1,23,2,5),
 (845,12,'12',1,'V',0,23,1,1),
 (846,15,'15',1,'V',0,23,2,5),
 (847,8,'8',0,' ',1,23,1,1),
 (848,37,'37',0,'B',0,23,4,5),
 (849,44,'44',0,'C',0,23,4,5),
 (850,2,'2',0,'C',0,23,1,1),
 (851,10,'10',0,'B',0,23,1,1),
 (852,25,'25',0,'A',0,23,2,5),
 (853,20,'20',0,'A',0,23,2,5),
 (854,29,'29',0,' ',1,23,3,1),
 (855,43,'43',0,'B',0,23,4,5),
 (856,5,'5',1,'V',0,23,1,1),
 (857,39,'39',1,'V',0,23,4,5),
 (858,1,'1',0,'A',0,23,1,1),
 (859,26,'26',1,'V',0,23,2,5),
 (860,33,'33',0,'C',0,23,3,1),
 (861,28,'28',0,'A',0,23,3,1),
 (862,9,'9',0,'A',0,23,1,1),
 (863,36,'36',0,'A',0,23,4,5),
 (864,13,'13',0,'A',0,23,2,5),
 (865,38,'38',0,'C',0,23,4,5),
 (866,35,'35',0,'A',0,23,4,5),
 (867,17,'17',1,'V',0,23,2,5);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (868,22,'22',0,'B',0,23,2,5),
 (869,42,'42',0,'A',0,23,4,5),
 (870,27,'27',0,'A',0,23,3,1),
 (871,23,'23',1,'V',0,23,2,5),
 (872,24,'24',1,'V',0,23,2,5),
 (873,16,'16',1,'V',0,23,2,5),
 (874,14,'14',0,'A',0,23,2,5),
 (875,21,'21',0,'C',0,23,2,5),
 (876,31,'31',0,' ',1,23,3,1),
 (877,34,'34',0,'B',0,23,3,1),
 (878,40,'40',1,'F',0,23,4,5),
 (879,4,'4',0,'B',0,23,1,1),
 (880,11,'11',0,'A',0,23,1,1),
 (881,20,'20',1,'V',0,24,2,2),
 (882,30,'30',0,'B',0,24,2,2),
 (883,9,'9',0,' ',1,24,1,2),
 (884,4,'4',0,'B',0,24,1,2),
 (885,14,'14',0,'B',0,24,1,2),
 (886,24,'24',0,'C',0,24,2,2),
 (887,13,'13',0,'B',0,24,1,2),
 (888,11,'11',0,'B',0,24,1,2),
 (889,17,'17',0,'B',0,24,1,2),
 (890,18,'18',0,'B',0,24,1,2),
 (891,23,'23',1,'F',0,24,2,2),
 (892,15,'15',0,'B',0,24,1,2),
 (893,12,'12',1,'V',0,24,1,2),
 (894,1,'1',0,' ',1,24,1,2),
 (895,29,'29',0,'B',0,24,2,2),
 (896,27,'27',1,'V',0,24,2,2);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (897,26,'26',0,'B',0,24,2,2),
 (898,10,'10',0,' ',1,24,1,2),
 (899,28,'28',1,'V',0,24,2,2),
 (900,6,'6',0,'B',0,24,1,2),
 (901,21,'21',0,'C',0,24,2,2),
 (902,25,'25',0,'C',0,24,2,2),
 (903,5,'5',0,'B',0,24,1,2),
 (904,7,'7',1,'V',0,24,1,2),
 (905,22,'22',0,'C',0,24,2,2),
 (906,19,'19',1,'V',0,24,2,2),
 (907,16,'16',0,'B',0,24,1,2),
 (908,2,'2',0,'B',0,24,1,2),
 (909,8,'8',0,'B',0,24,1,2),
 (910,3,'3',1,'V',0,24,1,2),
 (941,8,'8',0,'A',0,25,1,1),
 (942,21,'21',0,'B',0,25,1,1),
 (943,23,'23',0,'B',0,25,1,1),
 (944,9,'9',0,'A',0,25,1,1),
 (945,18,'18',0,'B',0,25,1,1),
 (946,22,'22',0,'B',0,25,1,1),
 (947,5,'5',0,'A',0,25,1,1),
 (948,12,'12',0,'A',0,25,1,1),
 (949,2,'2',0,'A',0,25,1,1),
 (950,25,'25',0,'B',0,25,1,1),
 (951,13,'13',0,'A',0,25,1,1),
 (952,29,'29',0,'B',0,25,1,1),
 (953,20,'20',0,'B',0,25,1,1),
 (954,28,'28',0,'B',0,25,1,1),
 (955,11,'11',0,'A',0,25,1,1);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (956,27,'27',0,'B',0,25,1,1),
 (957,24,'24',0,'B',0,25,1,1),
 (958,7,'7',0,'A',0,25,1,1),
 (959,16,'16',0,'B',0,25,1,1),
 (960,3,'3',0,'A',0,25,1,1),
 (961,26,'26',0,'B',0,25,1,1),
 (962,10,'10',0,'A',0,25,1,1),
 (963,0,'0',0,'A',0,25,1,1),
 (964,4,'4',0,'A',0,25,1,1),
 (965,15,'15',0,'B',0,25,1,1),
 (966,1,'1',0,'A',0,25,1,1),
 (967,17,'17',0,'B',0,25,1,1),
 (968,14,'14',0,'B',0,25,1,1),
 (969,6,'6',0,'A',0,25,1,1),
 (970,19,'19',0,'B',0,25,1,1);
/*!40000 ALTER TABLE `respuestasesperadasprueba` ENABLE KEYS */;


--
-- Definition of table `tipocurso`
--

DROP TABLE IF EXISTS `tipocurso`;
CREATE TABLE `tipocurso` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tipocurso`
--

/*!40000 ALTER TABLE `tipocurso` DISABLE KEYS */;
INSERT INTO `tipocurso` (`ID`,`NAME`) VALUES 
 (1,'Primero Básico'),
 (2,'Segundo Básico'),
 (3,'Tercero Básico'),
 (4,'Cuarto Básico'),
 (5,'Quinto Básico'),
 (6,'Sexto Básico'),
 (7,'Séptimo Básico'),
 (8,'Octavo Básico');
/*!40000 ALTER TABLE `tipocurso` ENABLE KEYS */;


--
-- Definition of table `tipoprueba`
--

DROP TABLE IF EXISTS `tipoprueba`;
CREATE TABLE `tipoprueba` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tipoprueba`
--

/*!40000 ALTER TABLE `tipoprueba` DISABLE KEYS */;
INSERT INTO `tipoprueba` (`ID`,`NAME`) VALUES 
 (1,'General'),
 (2,'Simce');
/*!40000 ALTER TABLE `tipoprueba` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
