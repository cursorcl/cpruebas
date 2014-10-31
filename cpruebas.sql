-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.39


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

CREATE DATABASE /*!32312 IF NOT EXISTS*/ cpruebas;
USE cpruebas;

--
-- Table structure for table `cpruebas`.`alumno`
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
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`alumno`
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
 (198,'MARILEO',' MUÑOZ','21198558-5',' ','JUAN PABLO',52,18);
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`asignatura`
--

DROP TABLE IF EXISTS `asignatura`;
CREATE TABLE `asignatura` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`asignatura`
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
-- Table structure for table `cpruebas`.`ciclo`
--

DROP TABLE IF EXISTS `ciclo`;
CREATE TABLE `ciclo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`ciclo`
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
-- Table structure for table `cpruebas`.`colegio`
--

DROP TABLE IF EXISTS `colegio`;
CREATE TABLE `colegio` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DIRECCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`colegio`
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
 (18,'ESCUELA RINCÓN DE PILÉN','');
/*!40000 ALTER TABLE `colegio` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`curso`
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
) ENGINE=InnoDB AUTO_INCREMENT=234 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`curso`
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
 (233,'Séptimo',7,2,5);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`curso_alumno`
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
-- Dumping data for table `cpruebas`.`curso_alumno`
--

/*!40000 ALTER TABLE `curso_alumno` DISABLE KEYS */;
/*!40000 ALTER TABLE `curso_alumno` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`ejetematico`
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
-- Dumping data for table `cpruebas`.`ejetematico`
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
-- Table structure for table `cpruebas`.`evaluacionejetematico`
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
-- Dumping data for table `cpruebas`.`evaluacionejetematico`
--

/*!40000 ALTER TABLE `evaluacionejetematico` DISABLE KEYS */;
INSERT INTO `evaluacionejetematico` (`ID`,`NAME`,`NRORANGOMIN`,`NRORANGOMAX`) VALUES 
 (1,'Adecuado',75,100),
 (2,'Elemental',50,74),
 (3,'Insuficiente',1,49);
/*!40000 ALTER TABLE `evaluacionejetematico` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`evaluacionprueba`
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
-- Dumping data for table `cpruebas`.`evaluacionprueba`
--

/*!40000 ALTER TABLE `evaluacionprueba` DISABLE KEYS */;
INSERT INTO `evaluacionprueba` (`ID`,`NAME`,`FECHA`,`CURSO_ID`,`PROFESOR_ID`,`COLEGIO_ID`,`PRUEBA_ID`) VALUES 
 (3,'Asignatura A1 ESCUELA CHORRILLOS Cuarto 2014-10-31',16374,4,2,1,20);
/*!40000 ALTER TABLE `evaluacionprueba` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`evaluacionprueba_pruebarendida`
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
-- Dumping data for table `cpruebas`.`evaluacionprueba_pruebarendida`
--

/*!40000 ALTER TABLE `evaluacionprueba_pruebarendida` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacionprueba_pruebarendida` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`formas`
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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`formas`
--

/*!40000 ALTER TABLE `formas` DISABLE KEYS */;
INSERT INTO `formas` (`ID`,`FORMA`,`ORDEN`,`NAME`,`PRUEBA_ID`) VALUES 
 (24,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20','Forma 1',20);
/*!40000 ALTER TABLE `formas` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`habilidad`
--

DROP TABLE IF EXISTS `habilidad`;
CREATE TABLE `habilidad` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`habilidad`
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
-- Table structure for table `cpruebas`.`nivelevaluacion`
--

DROP TABLE IF EXISTS `nivelevaluacion`;
CREATE TABLE `nivelevaluacion` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `NRORANGOS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`nivelevaluacion`
--

/*!40000 ALTER TABLE `nivelevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion` (`ID`,`NAME`,`NRORANGOS`) VALUES 
 (1,'Bajo/Medio Bajo/Medio Alto/Alto',4);
/*!40000 ALTER TABLE `nivelevaluacion` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`nivelevaluacion_rangoevaluacion`
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
-- Dumping data for table `cpruebas`.`nivelevaluacion_rangoevaluacion`
--

/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion_rangoevaluacion` (`nivelevaluacion_ID`,`rangos_ID`) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,4);
/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`profesor`
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
-- Dumping data for table `cpruebas`.`profesor`
--

/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` (`ID`,`NAME`,`PATERNO`,`RUT`,`MATERNO`) VALUES 
 (1,'Eliecer','Osorio','10613781-1','Verdugo'),
 (2,'Susan','Farías','12623503-8','Zavala'),
 (3,'Mauricio','Salgado','9410614-1','Caro'),
 (4,'Ursula','Farías','12623502-k','Zavala');
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`prueba`
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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`prueba`
--

/*!40000 ALTER TABLE `prueba` DISABLE KEYS */;
INSERT INTO `prueba` (`ID`,`EXIGENCIA`,`ALTERNATIVAS`,`FECHA`,`PUNTAJEBASE`,`NROFORMAS`,`RESPONSES`,`NROPREGUNTAS`,`NAME`,`ASIGNATURA_ID`,`CURSO_ID`,`TIPOPRUEBA_ID`,`PROFESOR_ID`,`NIVELEVALUACION_ID`) VALUES 
 (20,60,5,16374,1,1,'AAAAAAAAAAAAAAAAAAAA',20,'Prueba 3',16,4,1,2,1);
/*!40000 ALTER TABLE `prueba` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`pruebarendida`
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
-- Dumping data for table `cpruebas`.`pruebarendida`
--

/*!40000 ALTER TABLE `pruebarendida` DISABLE KEYS */;
INSERT INTO `pruebarendida` (`ID`,`OMITIDAS`,`NOTA`,`RESPUESTAS`,`FORMA`,`MALAS`,`BUENAS`,`NAME`,`RANGO_ID`,`ALUMNO_ID`,`EVALUACIONPRUEBA_ID`) VALUES 
 (7,0,1,'BBBBBBBBBBBBBBBBBBBB',1,20,0,NULL,1,117,3),
 (8,10,3.5,'OOOOOOOOOOAAAAAAAAAA',1,0,10,NULL,2,3,3),
 (9,0,7,'AAAAAAAAAAAAAAAAAAAA',1,0,20,NULL,4,119,3),
 (10,0,5.125,'AAAAAAAAAAAAAAABBBBB',1,5,15,NULL,3,118,3);
/*!40000 ALTER TABLE `pruebarendida` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`rangoevaluacion`
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
-- Dumping data for table `cpruebas`.`rangoevaluacion`
--

/*!40000 ALTER TABLE `rangoevaluacion` DISABLE KEYS */;
INSERT INTO `rangoevaluacion` (`ID`,`ABREVIACION`,`MINIMO`,`NAME`,`MAXIMO`) VALUES 
 (1,'B',0,'Bajo',39),
 (2,'MB',40,'Medio Bajo',65),
 (3,'MA',66,'Medio Alto',79),
 (4,'A',80,'Alto',100);
/*!40000 ALTER TABLE `rangoevaluacion` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`respuestasesperadasprueba`
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
) ENGINE=InnoDB AUTO_INCREMENT=626 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`respuestasesperadasprueba`
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
 (625,2,'2',0,'A',0,20,1,1);
/*!40000 ALTER TABLE `respuestasesperadasprueba` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`tipocurso`
--

DROP TABLE IF EXISTS `tipocurso`;
CREATE TABLE `tipocurso` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`tipocurso`
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
-- Table structure for table `cpruebas`.`tipoprueba`
--

DROP TABLE IF EXISTS `tipoprueba`;
CREATE TABLE `tipoprueba` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`tipoprueba`
--

/*!40000 ALTER TABLE `tipoprueba` DISABLE KEYS */;
INSERT INTO `tipoprueba` (`ID`,`NAME`) VALUES 
 (1,'General Modificado'),
 (2,'Simce'),
 (6,'Prueba 1'),
 (7,'Prueba 2'),
 (8,'Prueba 3');
/*!40000 ALTER TABLE `tipoprueba` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
