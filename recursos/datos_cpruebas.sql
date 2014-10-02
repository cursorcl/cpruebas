CREATE DATABASE  IF NOT EXISTS `cpruebas` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cpruebas`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: cpruebas
-- ------------------------------------------------------
-- Server version	5.5.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alumno`
--

DROP TABLE IF EXISTS `alumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumno`
--

LOCK TABLES `alumno` WRITE;
/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
INSERT INTO `alumno` VALUES (1,'Osorio','Verdugo','10613781-1','Mi casa','Eliecer Enrique',1,1),(2,'Farías','Zavala','12623503-8','Su Casa','Susan Angela',1,1);
/*!40000 ALTER TABLE `alumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asignatura`
--

DROP TABLE IF EXISTS `asignatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asignatura` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignatura`
--

LOCK TABLES `asignatura` WRITE;
/*!40000 ALTER TABLE `asignatura` DISABLE KEYS */;
INSERT INTO `asignatura` VALUES (1,'Lenguaje y Comunicación'),(2,'Matemáticas'),(3,'Sociedad y Cultura'),(4,'Naturaleza');
/*!40000 ALTER TABLE `asignatura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ciclo`
--

DROP TABLE IF EXISTS `ciclo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ciclo` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ciclo`
--

LOCK TABLES `ciclo` WRITE;
/*!40000 ALTER TABLE `ciclo` DISABLE KEYS */;
INSERT INTO `ciclo` VALUES (1,'Ciclo 1'),(2,'Ciclo 2'),(3,'Ciclo 3');
/*!40000 ALTER TABLE `ciclo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colegio`
--

DROP TABLE IF EXISTS `colegio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colegio` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `image` blob,
  `DIRECCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colegio`
--

LOCK TABLES `colegio` WRITE;
/*!40000 ALTER TABLE `colegio` DISABLE KEYS */;
INSERT INTO `colegio` VALUES (1,'Colegio 1','�PNG\r\n\Z\n\0\0\0\rIHDR\0\0\0\0\0\0\0\0\0�=M�\0\0�IDATx�c` ����9Vhi)mbf����d��Uliihm��\Z��[�:��.��Ī�������e����w_0vv��_PZ��������g�o���~A�`b�V6#4���2�9�U�z����\r��?<:�dl*���_Y��`m�\"���u��+�@H������qI�����\'g�OH���򿦱7�hj8�ԏ +C\"��Gǧ�f���)���]P�?-�l@]s&Cai}�p�ϐ�� H2���Qy���ʖ��e�`MIi���;&e2������{3�\Z��f��V�u�qYu����:`5L���I�ׯ_��>4�_yMн=`\\Q�	���m]S����n/_[��1�=HqmS��������`?u�Nǌ�&�ϟ?7Ϟ��\nH1�Y \rY���5@5�q��#\'�\05�9	�\rh\Z��>}9��s�cP��\0����s�<��3-���\0����\0	h��e_\0\0\0\0IEND�B`�','Dirección 1'),(2,'Colegio 2','�PNG\r\n\Z\n\0\0\0\rIHDR\0\0\0\r\0\0\0\0\0\0�&�\0\0�IDATx�c`��L���q%ISMso}HD����R�5�5�Nj��!aq�CCCy�*\rM�)\0*|�\0�Cchh�=P�{HD�\'�-�/a\Z\"bR���{h����9�4�r�F&���O�u���S@ɦ]��iHJ��~�ƭ�@13�:����d��踴x��e�6^Jj��&L�SX������k�bJ ����l�:�7��+�\0m�����P��7o�5����d�^�؋�^���S2�!���JRZ�w�v������� �Ԭ��y�uG�j:/�l��PT�%e���<�0oѪU ��E\'W��\0�H��Y�W3|���+3��(�J:����M9�������W! ��ܺ}o5H\0�tz��J@3��04\"�,(v�;|_@����o6�vN~����I��O��x�΂-t���ڍۧ.]s��s��)3=ܹ����?.����0PR�#���S��=���#)ȯ�}\0\0\0\0IEND�B`�','Dirección 2');
/*!40000 ALTER TABLE `colegio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso`
--

DROP TABLE IF EXISTS `curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` VALUES (1,'Primero A',1,1,1),(2,'Primero B',1,1,1),(3,'Segundo A',2,1,1);
/*!40000 ALTER TABLE `curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso_alumno`
--

DROP TABLE IF EXISTS `curso_alumno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curso_alumno` (
  `curso_ID` bigint(20) NOT NULL,
  `alumnos_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`curso_ID`,`alumnos_ID`),
  KEY `FK_CURSO_ALUMNO_alumnos_ID` (`alumnos_ID`),
  CONSTRAINT `FK_CURSO_ALUMNO_alumnos_ID` FOREIGN KEY (`alumnos_ID`) REFERENCES `alumno` (`ID`),
  CONSTRAINT `FK_CURSO_ALUMNO_curso_ID` FOREIGN KEY (`curso_ID`) REFERENCES `curso` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso_alumno`
--

LOCK TABLES `curso_alumno` WRITE;
/*!40000 ALTER TABLE `curso_alumno` DISABLE KEYS */;
/*!40000 ALTER TABLE `curso_alumno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ejetematico`
--

DROP TABLE IF EXISTS `ejetematico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ejetematico`
--

LOCK TABLES `ejetematico` WRITE;
/*!40000 ALTER TABLE `ejetematico` DISABLE KEYS */;
INSERT INTO `ejetematico` VALUES (1,'Eje temático 1',1,1),(2,'Eje temático 2',1,1),(3,'Eje temático 3',1,1),(4,'Eje temático 4',2,1),(5,'Eje temático 5',2,1);
/*!40000 ALTER TABLE `ejetematico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacionejetematico`
--

DROP TABLE IF EXISTS `evaluacionejetematico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evaluacionejetematico` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `NRORANGOMIN` float DEFAULT NULL,
  `NRORANGOMAX` float DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacionejetematico`
--

LOCK TABLES `evaluacionejetematico` WRITE;
/*!40000 ALTER TABLE `evaluacionejetematico` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacionejetematico` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacionprueba`
--

DROP TABLE IF EXISTS `evaluacionprueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacionprueba`
--

LOCK TABLES `evaluacionprueba` WRITE;
/*!40000 ALTER TABLE `evaluacionprueba` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacionprueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluacionprueba_pruebarendida`
--

DROP TABLE IF EXISTS `evaluacionprueba_pruebarendida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `evaluacionprueba_pruebarendida` (
  `evaluacionprueba_ID` bigint(20) NOT NULL,
  `pruebasRendidas_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`evaluacionprueba_ID`,`pruebasRendidas_ID`),
  KEY `EVALUACIONPRUEBA_PRUEBARENDIDA_pruebasRendidas_ID` (`pruebasRendidas_ID`),
  CONSTRAINT `EVALUACIONPRUEBA_PRUEBARENDIDA_evaluacionprueba_ID` FOREIGN KEY (`evaluacionprueba_ID`) REFERENCES `evaluacionprueba` (`ID`),
  CONSTRAINT `EVALUACIONPRUEBA_PRUEBARENDIDA_pruebasRendidas_ID` FOREIGN KEY (`pruebasRendidas_ID`) REFERENCES `pruebarendida` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluacionprueba_pruebarendida`
--

LOCK TABLES `evaluacionprueba_pruebarendida` WRITE;
/*!40000 ALTER TABLE `evaluacionprueba_pruebarendida` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluacionprueba_pruebarendida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formas`
--

DROP TABLE IF EXISTS `formas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formas` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FORMA` int(11) DEFAULT NULL,
  `ORDEN` varchar(255) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `PRUEBA_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_FORMAS_PRUEBA_ID` (`PRUEBA_ID`),
  CONSTRAINT `FK_FORMAS_PRUEBA_ID` FOREIGN KEY (`PRUEBA_ID`) REFERENCES `prueba` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formas`
--

LOCK TABLES `formas` WRITE;
/*!40000 ALTER TABLE `formas` DISABLE KEYS */;
INSERT INTO `formas` VALUES (2,1,'1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30','Forma 1',1);
/*!40000 ALTER TABLE `formas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `habilidad`
--

DROP TABLE IF EXISTS `habilidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `habilidad` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habilidad`
--

LOCK TABLES `habilidad` WRITE;
/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` VALUES (1,'Habilidad 1','Habilidad 1'),(2,'Habilidad 2','Habilidad 2'),(3,'Habilidad 3','Habilidad 3'),(4,'Habilidad 4','Habilidad 4');
/*!40000 ALTER TABLE `habilidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nivelevaluacion`
--

DROP TABLE IF EXISTS `nivelevaluacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nivelevaluacion` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `NRORANGOS` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nivelevaluacion`
--

LOCK TABLES `nivelevaluacion` WRITE;
/*!40000 ALTER TABLE `nivelevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion` VALUES (1,'Alto / Medio Alto / Medio Bajo / Bajo',4);
/*!40000 ALTER TABLE `nivelevaluacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nivelevaluacion_rangoevaluacion`
--

DROP TABLE IF EXISTS `nivelevaluacion_rangoevaluacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nivelevaluacion_rangoevaluacion` (
  `nivelevaluacion_ID` bigint(20) NOT NULL,
  `rangos_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`nivelevaluacion_ID`,`rangos_ID`),
  KEY `FK_NIVELEVALUACION_RANGOEVALUACION_rangos_ID` (`rangos_ID`),
  CONSTRAINT `FK_NIVELEVALUACION_RANGOEVALUACION_rangos_ID` FOREIGN KEY (`rangos_ID`) REFERENCES `rangoevaluacion` (`ID`),
  CONSTRAINT `NIVELEVALUACION_RANGOEVALUACION_nivelevaluacion_ID` FOREIGN KEY (`nivelevaluacion_ID`) REFERENCES `nivelevaluacion` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nivelevaluacion_rangoevaluacion`
--

LOCK TABLES `nivelevaluacion_rangoevaluacion` WRITE;
/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion_rangoevaluacion` VALUES (1,1),(1,2),(1,3),(1,4);
/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesor`
--

DROP TABLE IF EXISTS `profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profesor` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  `PATERNO` varchar(255) DEFAULT NULL,
  `RUT` varchar(255) DEFAULT NULL,
  `MATERNO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesor`
--

LOCK TABLES `profesor` WRITE;
/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` VALUES (1,'Eliecer Enrique','Osorio','10613781-1','Verdugo'),(2,'Susan Angela','Farías','12623503-8','Zavala');
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prueba`
--

DROP TABLE IF EXISTS `prueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prueba`
--

LOCK TABLES `prueba` WRITE;
/*!40000 ALTER TABLE `prueba` DISABLE KEYS */;
INSERT INTO `prueba` VALUES (1,NULL,5,16332,1,1,NULL,30,'Prueba 1',1,1,1,1,1);
/*!40000 ALTER TABLE `prueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pruebarendida`
--

DROP TABLE IF EXISTS `pruebarendida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pruebarendida` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MALAS` int(11) DEFAULT NULL,
  `OMITIDAS` int(11) DEFAULT NULL,
  `RESPUESTAS` varchar(255) DEFAULT NULL,
  `NOTA` float DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `FORMA` int(11) DEFAULT NULL,
  `BUENAS` int(11) DEFAULT NULL,
  `ALUMNO_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRUEBARENDIDA_ALUMNO_ID` (`ALUMNO_ID`),
  CONSTRAINT `FK_PRUEBARENDIDA_ALUMNO_ID` FOREIGN KEY (`ALUMNO_ID`) REFERENCES `alumno` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pruebarendida`
--

LOCK TABLES `pruebarendida` WRITE;
/*!40000 ALTER TABLE `pruebarendida` DISABLE KEYS */;
/*!40000 ALTER TABLE `pruebarendida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rangoevaluacion`
--

DROP TABLE IF EXISTS `rangoevaluacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rangoevaluacion` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ABREVIACION` varchar(255) DEFAULT NULL,
  `MINIMO` float DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `MAXIMO` float DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rangoevaluacion`
--

LOCK TABLES `rangoevaluacion` WRITE;
/*!40000 ALTER TABLE `rangoevaluacion` DISABLE KEYS */;
INSERT INTO `rangoevaluacion` VALUES (1,'BA',0,'Bajo',25),(2,'MB',25,'Medio Bajo',50),(3,'MA',50,'Medio Alto',75),(4,'AL',75,'Alto',100);
/*!40000 ALTER TABLE `rangoevaluacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `respuestasesperadasprueba`
--

DROP TABLE IF EXISTS `respuestasesperadasprueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `respuestasesperadasprueba`
--

LOCK TABLES `respuestasesperadasprueba` WRITE;
/*!40000 ALTER TABLE `respuestasesperadasprueba` DISABLE KEYS */;
INSERT INTO `respuestasesperadasprueba` VALUES (31,5,'5',0,'E',0,1,2,1),(32,24,'24',0,'D',0,1,2,1),(33,28,'28',0,'C',0,1,2,1),(34,3,'3',0,'C',0,1,2,1),(35,12,'12',0,'B',0,1,2,1),(36,25,'25',0,'E',0,1,2,1),(37,16,'16',0,'A',0,1,2,1),(38,17,'17',0,'B',0,1,2,1),(39,2,'2',0,'B',0,1,1,3),(40,18,'18',0,'C',0,1,2,1),(41,20,'20',0,'E',0,1,2,1),(42,22,'22',0,'B',0,1,2,1),(43,4,'4',0,'D',0,1,2,1),(44,26,'26',0,'A',0,1,2,1),(45,15,'15',0,'E',0,1,2,1),(46,23,'23',0,'C',0,1,2,1),(47,29,'29',0,'D',0,1,2,1),(48,8,'8',0,'C',0,1,2,1),(49,10,'10',0,'E',0,1,2,1),(50,7,'7',0,'B',0,1,2,1),(51,1,'1',0,'A',0,1,4,2),(52,14,'14',0,'D',0,1,2,1),(53,6,'6',0,'A',0,1,2,1),(54,27,'27',0,'B',0,1,2,1),(55,21,'21',0,'A',0,1,2,1),(56,11,'11',0,'A',0,1,2,2),(57,19,'19',0,'D',0,1,2,1),(58,13,'13',0,'C',0,1,2,1),(59,9,'9',0,'D',0,1,2,1),(60,30,'30',0,'E',0,1,2,1);
/*!40000 ALTER TABLE `respuestasesperadasprueba` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipocurso`
--

DROP TABLE IF EXISTS `tipocurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipocurso` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipocurso`
--

LOCK TABLES `tipocurso` WRITE;
/*!40000 ALTER TABLE `tipocurso` DISABLE KEYS */;
INSERT INTO `tipocurso` VALUES (1,'Primero Básico'),(2,'Segundo Básico'),(3,'Tercero Básico'),(4,'Cuarto Básico'),(5,'Quinto Básico'),(6,'Sexto Básico'),(7,'Septimo Básico'),(8,'Octavo Básico');
/*!40000 ALTER TABLE `tipocurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipoprueba`
--

DROP TABLE IF EXISTS `tipoprueba`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipoprueba` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoprueba`
--

LOCK TABLES `tipoprueba` WRITE;
/*!40000 ALTER TABLE `tipoprueba` DISABLE KEYS */;
INSERT INTO `tipoprueba` VALUES (1,'General'),(2,'SIMCE'),(3,'PSU');
/*!40000 ALTER TABLE `tipoprueba` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-10-02  0:41:39
