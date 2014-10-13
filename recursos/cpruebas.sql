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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`alumno`
--

/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
INSERT INTO `alumno` (`ID`,`PATERNO`,`MATERNO`,`RUT`,`DIRECCION`,`NAME`,`CURSO_ID`,`COLEGIO_ID`) VALUES 
 (1,'Osorio','Verdugo','10613781-1','Su casa','Eliecer',1,1),
 (2,'Farías','Zavala','12623503-8','Su Casa','Susan',1,1);
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`asignatura`
--

DROP TABLE IF EXISTS `asignatura`;
CREATE TABLE `asignatura` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`asignatura`
--

/*!40000 ALTER TABLE `asignatura` DISABLE KEYS */;
INSERT INTO `asignatura` (`ID`,`NAME`) VALUES 
 (1,'Lenguaje'),
 (2,'Matemáticas'),
 (3,'Sociedad'),
 (4,'Naturaleza');
/*!40000 ALTER TABLE `asignatura` ENABLE KEYS */;


--
-- Table structure for table `cpruebas`.`ciclo`
--

DROP TABLE IF EXISTS `ciclo`;
CREATE TABLE `ciclo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`ciclo`
--

/*!40000 ALTER TABLE `ciclo` DISABLE KEYS */;
INSERT INTO `ciclo` (`ID`,`NAME`) VALUES 
 (1,'Ciclo 1'),
 (2,'Ciclo 2'),
 (3,'Ciclo 3'),
 (4,'Ciclo 4');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`colegio`
--

/*!40000 ALTER TABLE `colegio` DISABLE KEYS */;
INSERT INTO `colegio` (`ID`,`NAME`,`DIRECCION`) VALUES 
 (1,'Colegio 1','Dirección 1'),
 (2,'Colegio 2','Dirección 2'),
 (3,'Colegio 3','Dirección 3');
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`curso`
--

/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` (`ID`,`NAME`,`TIPOCURSO_ID`,`CICLO_ID`,`COLEGIO_ID`) VALUES 
 (1,'Primero A',1,1,1),
 (2,'Primero B',1,1,1),
 (3,'Segundo A',2,1,1),
 (4,'Segundo B',2,1,1);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;


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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`ejetematico`
--

/*!40000 ALTER TABLE `ejetematico` DISABLE KEYS */;
INSERT INTO `ejetematico` (`ID`,`NAME`,`ASIGNATURA_ID`,`TIPOPRUEBA_ID`) VALUES 
 (1,'EJE 1',1,1),
 (2,'EJE 2',1,1),
 (3,'EJE 3',2,1),
 (4,'EJE 4',2,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`evaluacionprueba`
--

/*!40000 ALTER TABLE `evaluacionprueba` DISABLE KEYS */;
INSERT INTO `evaluacionprueba` (`ID`,`NAME`,`FECHA`,`CURSO_ID`,`PROFESOR_ID`,`COLEGIO_ID`,`PRUEBA_ID`) VALUES 
 (2,'Lenguaje-Colegio 1-Primero A-2014-10-04',16347,1,1,1,1);
/*!40000 ALTER TABLE `evaluacionprueba` ENABLE KEYS */;


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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`formas`
--

/*!40000 ALTER TABLE `formas` DISABLE KEYS */;
INSERT INTO `formas` (`ID`,`FORMA`,`ORDEN`,`NAME`,`PRUEBA_ID`) VALUES 
 (1,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30','Forma 1',1),
 (2,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60','Forma 1',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`habilidad`
--

/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` (`ID`,`NAME`,`DESCRIPCION`) VALUES 
 (1,'Habilidad 1','Habilidad 1'),
 (2,'Habilidad 2','Habilidad 2'),
 (3,'Habilidad 3','Habilidad 3');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`profesor`
--

/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` (`ID`,`NAME`,`PATERNO`,`RUT`,`MATERNO`) VALUES 
 (1,'Eliecer','Osorio','10613781-1','Verdugo'),
 (2,'Susan','Farías','12623503-8','Zavala');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`prueba`
--

/*!40000 ALTER TABLE `prueba` DISABLE KEYS */;
INSERT INTO `prueba` (`ID`,`EXIGENCIA`,`ALTERNATIVAS`,`FECHA`,`PUNTAJEBASE`,`NROFORMAS`,`RESPONSES`,`NROPREGUNTAS`,`NAME`,`ASIGNATURA_ID`,`CURSO_ID`,`TIPOPRUEBA_ID`,`PROFESOR_ID`,`NIVELEVALUACION_ID`) VALUES 
 (1,60,5,16347,1,1,'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',30,'Lenguaje Vocabulario',1,1,1,1,1),
 (2,60,5,16347,1,1,'BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBV',60,'Lengauje Abecedario',1,2,1,1,1);
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
  CONSTRAINT `FK_PRUEBARENDIDA_RANGO_ID` FOREIGN KEY (`RANGO_ID`) REFERENCES `rangoevaluacion` (`ID`),
  CONSTRAINT `FK_PRUEBARENDIDA_ALUMNO_ID` FOREIGN KEY (`ALUMNO_ID`) REFERENCES `alumno` (`ID`),
  CONSTRAINT `FK_PRUEBARENDIDA_EVALUACIONPRUEBA_ID` FOREIGN KEY (`EVALUACIONPRUEBA_ID`) REFERENCES `evaluacionprueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`pruebarendida`
--

/*!40000 ALTER TABLE `pruebarendida` DISABLE KEYS */;
INSERT INTO `pruebarendida` (`ID`,`OMITIDAS`,`NOTA`,`RESPUESTAS`,`FORMA`,`MALAS`,`BUENAS`,`NAME`,`RANGO_ID`,`ALUMNO_ID`,`EVALUACIONPRUEBA_ID`) VALUES 
 (1,0,2,'AAAAAABBBBBBBBBBBBBBBBBBBBBBBB',1,24,6,NULL,1,1,2),
 (2,0,7,'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',1,0,30,NULL,4,2,2);
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
 (1,'B',0,'Bajo',25),
 (2,'MB',25,'Medio Bajo',50),
 (3,'MA',50,'Medio Alto',75),
 (4,'A',75,'Alto',100);
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
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`respuestasesperadasprueba`
--

/*!40000 ALTER TABLE `respuestasesperadasprueba` DISABLE KEYS */;
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (1,20,'20',0,'A',0,1,2,2),
 (2,10,'10',0,'A',0,1,1,1),
 (3,13,'13',0,'A',0,1,1,1),
 (4,6,'6',0,'A',0,1,1,1),
 (5,3,'3',0,'A',0,1,3,1),
 (6,8,'8',0,'A',0,1,1,1),
 (7,22,'22',0,'A',0,1,2,2),
 (8,2,'2',0,'A',0,1,3,1),
 (9,9,'9',0,'A',0,1,1,1),
 (10,16,'16',0,'A',0,1,3,1),
 (11,12,'12',0,'A',0,1,1,1),
 (12,27,'27',0,'A',0,1,2,2),
 (13,1,'1',0,'A',0,1,3,1),
 (14,29,'29',0,'A',0,1,2,2),
 (15,18,'18',0,'A',0,1,2,2),
 (16,17,'17',0,'A',0,1,3,1),
 (17,5,'5',0,'A',0,1,1,1),
 (18,24,'24',0,'A',0,1,2,2),
 (19,11,'11',0,'A',0,1,1,1),
 (20,19,'19',0,'A',0,1,2,2),
 (21,23,'23',0,'A',0,1,2,2),
 (22,28,'28',0,'A',0,1,2,2),
 (23,30,'30',0,'A',0,1,2,2),
 (24,15,'15',0,'A',0,1,1,1),
 (25,26,'26',0,'A',0,1,2,2),
 (26,7,'7',0,'A',0,1,1,1),
 (27,25,'25',0,'A',0,1,2,2),
 (28,14,'14',0,'A',0,1,1,1),
 (29,21,'21',0,'A',0,1,2,2),
 (30,4,'4',0,'A',0,1,3,1),
 (31,39,'39',0,'B',0,2,1,2),
 (32,19,'19',0,'B',0,2,1,1);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (33,9,'9',0,'B',0,2,1,1),
 (34,15,'15',0,'B',0,2,1,1),
 (35,16,'16',0,'B',0,2,1,1),
 (36,41,'41',0,'B',0,2,1,2),
 (37,29,'29',0,'B',0,2,1,1),
 (38,57,'57',0,'B',0,2,1,2),
 (39,23,'23',0,'B',0,2,1,1),
 (40,27,'27',0,'B',0,2,1,1),
 (41,51,'51',0,'B',0,2,1,2),
 (42,34,'34',0,'B',0,2,1,1),
 (43,37,'37',0,'B',0,2,1,1),
 (44,43,'43',0,'B',0,2,1,2),
 (45,48,'48',0,'B',0,2,1,2),
 (46,5,'5',0,'B',0,2,1,1),
 (47,60,'60',1,'V',0,2,3,2),
 (48,12,'12',0,'B',0,2,1,1),
 (49,40,'40',0,'B',0,2,1,2),
 (50,8,'8',0,'B',0,2,1,1),
 (51,52,'52',0,'B',0,2,1,2),
 (52,53,'53',0,'B',0,2,1,2),
 (53,54,'54',0,'B',0,2,1,2),
 (54,38,'38',0,'B',0,2,1,1),
 (55,58,'58',0,'B',0,2,1,2),
 (56,18,'18',0,'B',0,2,1,1),
 (57,42,'42',0,'B',0,2,1,2),
 (58,28,'28',0,'B',0,2,1,1),
 (59,47,'47',0,'B',0,2,1,2),
 (60,3,'3',0,'B',0,2,1,1),
 (61,10,'10',0,'B',0,2,1,1),
 (62,14,'14',0,'B',0,2,1,1),
 (63,33,'33',0,'B',0,2,1,1);
INSERT INTO `respuestasesperadasprueba` (`ID`,`NUMERO`,`NAME`,`VERDADEROFALSO`,`RESPUESTA`,`MENTAL`,`PRUEBA_ID`,`HABILIDAD_ID`,`EJETEMATICO_ID`) VALUES 
 (64,32,'32',0,'B',0,2,1,1),
 (65,17,'17',0,'B',0,2,1,1),
 (66,36,'36',0,'B',0,2,1,1),
 (67,55,'55',0,'B',0,2,1,2),
 (68,1,'1',0,'B',0,2,1,1),
 (69,30,'30',0,'B',0,2,1,1),
 (70,22,'22',0,'B',0,2,1,1),
 (71,56,'56',0,'B',0,2,1,2),
 (72,35,'35',0,'B',0,2,1,1),
 (73,46,'46',0,'B',0,2,1,2),
 (74,21,'21',0,'B',0,2,1,1),
 (75,11,'11',0,'B',0,2,1,1),
 (76,7,'7',0,'B',0,2,1,1),
 (77,2,'2',0,'B',0,2,1,1),
 (78,25,'25',0,'B',0,2,1,1),
 (79,4,'4',0,'B',0,2,1,1),
 (80,20,'20',0,'B',0,2,1,1),
 (81,50,'50',0,'B',0,2,1,2),
 (82,44,'44',0,'B',0,2,1,2),
 (83,13,'13',0,'B',0,2,1,1),
 (84,6,'6',0,'B',0,2,1,1),
 (85,49,'49',0,'B',0,2,1,2),
 (86,59,'59',0,'B',0,2,1,2),
 (87,31,'31',0,'B',0,2,1,1),
 (88,26,'26',0,'B',0,2,1,1),
 (89,24,'24',0,'B',0,2,1,1),
 (90,45,'45',0,'B',0,2,1,2);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cpruebas`.`tipoprueba`
--

/*!40000 ALTER TABLE `tipoprueba` DISABLE KEYS */;
INSERT INTO `tipoprueba` (`ID`,`NAME`) VALUES 
 (1,'General');
/*!40000 ALTER TABLE `tipoprueba` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
