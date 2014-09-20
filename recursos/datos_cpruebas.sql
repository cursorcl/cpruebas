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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alumno`
--

LOCK TABLES `alumno` WRITE;
/*!40000 ALTER TABLE `alumno` DISABLE KEYS */;
INSERT INTO `alumno` VALUES (1,'Osorio','Verdugo','10613781-1','Marinero Fuentealba 614','Eliecer Enrique',1,1),(2,'Osorio','Espinoza','21021606-5','Progreso 1000 casa 10','Viviana',1,1),(3,'Osorio','Espinoza','21652190-0','Progreso 1000 casa 10','Eliecer',1,1),(4,'Osorio','Espinoza','22148346-4','Progreso 1000 casa 10','Evelyn',1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignatura`
--

LOCK TABLES `asignatura` WRITE;
/*!40000 ALTER TABLE `asignatura` DISABLE KEYS */;
INSERT INTO `asignatura` VALUES (1,'Lenguaje y Comunicaci√≥n'),(2,'Matem√°ticas');
/*!40000 ALTER TABLE `asignatura` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colegio`
--

LOCK TABLES `colegio` WRITE;
/*!40000 ALTER TABLE `colegio` DISABLE KEYS */;
INSERT INTO `colegio` VALUES (1,'Colegio 1','âPNG\r\n\Z\n\0\0\0\rIHDR\0\0\0\0\0\0\0\0\0‡w=¯\0\0KIDATx⁄≈÷kLõUpñ(Jπ¥–2Ç‡∆DÓ-êÇ_ÿ ⁄““ñ˜•@◊Æ•ÖR\n¥ÖruÃ9G¢∆ò,Ÿ5ŒòI6ñÅ‹±‹-Ü Ö.ŸGçYÃ‘òÏì˙˜P¬Õ1¯aorÚ~:œÔ<Á<Ôs^üó˝–4Ì[@ç	e;Ø£≠πZ™\\î/WäÑB°ﬂ±Ü≤.O£˘$}Tﬂı§bú	„‘ITL˙¡‡BYWÃcıÕÃ~e•∏X*ï2è∂ÍK‚Ìg¸)ì”6˜I‘-≥`]CÕ<fï”!0:®òˆ!ÍY˝4-„*xQ•8OˇuÙØ÷•W–∞ Ö}ô€\"µﬂá¡<ÀÜ……B≈x(Ùé`hÉ†s¢¸[ËæJ|TT*=w 5˘ô˙ﬁË\'uÏ+ˇ‘/qQøƒ!@∏®ﬁ&Ba	Ån(Í&T›Å0L‰v“#Z&Àÿ7¯∆>joÒ\\6∑ÔÆ‡/4Ω∏àí{Ë\'|†˛8ª_,á>î4Â\ZÕ≥æhl>–Ô∫°ºÄ“n?ËáÿP\ZÚ5{ÇK$í\0Ìó<W›\nıÓ„P∑˝°Òf1HJ8hêïä3*OÌ´·ˇ	~t@’s⁄;qøJ•i€\0U+(6M3ˆŸû#\0ù–˛Pv˙B◊À˝´¢§pßzör,ÊÔ^%áÀ˘ﬂ@—ùì–\r0Aƒ;4ÊX≠sl¥xbP∑∂pø\0Ë€ä;vA†Ù\"„P#PY\\\\\\»C£;j/‚8∞<P˜1P⁄ÂÔ=É¢Å»ıpˇ¶U˘ m@°fëO7Äk´|4-od¬&#¸π@Ÿ⁄!&É\\TúÜ©Ô,å]düÇ∂#Ó±B¢»⁄©\"ô,DÛyÍÉ∂µX¥Øe‡Ω’4\\^y\rÓHo6÷E6ÈC,“*BQÈ&@L„ë∞åΩ€X,ﬂ$†z ¶ÓxTM≤†˙0{Lí#	€Û-(mπñ⁄Y⁄¶yÅÎkÈ‰Õ«O\"Z‹±∞/ê¨Ê»ò=ÉÜôx4N\'¡>ôÎhjá„aÓãGıp, 9(TãÃœˆ!°˛î7ﬂÍâB˚èõYlA◊<ixgâ`<º=œCÛl\n\ZùI®üH‹Ü‚Q3HVÔ$’Ù˛[ì\n°\"jˇfwQö´ªÛ{€˙iÇ§o#Ôz¯‡°m!≠Û)HﬁF`u$†fÜÕ≠îü(ôLppª6HJ¥˜^ˇ≠y9íTÌÎ;xÅπT4ª6Ädÿ\'íPÁåÉyÜ	Õ…?Sîº¯pNâÙÇÍf˙|’T(ZW¢qu=WWRqÂáT\\^HAÀ|\"\ZÁŒ¬2√0•dO”r˘2ıƒ°oµÇÇÇ◊ä™Ñv’çå∫Æò?ç£·§BHôN∞Q>Ã¡•ªg˛(˘(”EïâÎ\"EÃÒ/}r¯\nÖÙ•(S^e6Qö|É\\.?Ø»SúzÈ%ˇ7Ò5öÄAÏ^\0\0\0\0IENDÆB`Ç','Camino a casa');
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
  `NIVEL_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CURSO_NIVEL_ID` (`NIVEL_ID`),
  CONSTRAINT `FK_CURSO_NIVEL_ID` FOREIGN KEY (`NIVEL_ID`) REFERENCES `nivel` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso`
--

LOCK TABLES `curso` WRITE;
/*!40000 ALTER TABLE `curso` DISABLE KEYS */;
INSERT INTO `curso` VALUES (1,'Primero A',1),(2,'Primero B',1);
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
  CONSTRAINT `FK_EJETEMATICO_TIPOPRUEBA_ID` FOREIGN KEY (`TIPOPRUEBA_ID`) REFERENCES `tipoprueba` (`ID`),
  CONSTRAINT `FK_EJETEMATICO_ASIGNATURA_ID` FOREIGN KEY (`ASIGNATURA_ID`) REFERENCES `asignatura` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ejetematico`
--

LOCK TABLES `ejetematico` WRITE;
/*!40000 ALTER TABLE `ejetematico` DISABLE KEYS */;
INSERT INTO `ejetematico` VALUES (1,'Eje tem√°tico 1',1,1),(2,'Eje tem√°tico 2',1,1);
/*!40000 ALTER TABLE `ejetematico` ENABLE KEYS */;
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
  KEY `FK_EVALUACIONPRUEBA_CURSO_ID` (`CURSO_ID`),
  KEY `FK_EVALUACIONPRUEBA_PRUEBA_ID` (`PRUEBA_ID`),
  KEY `FK_EVALUACIONPRUEBA_PROFESOR_ID` (`PROFESOR_ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_PROFESOR_ID` FOREIGN KEY (`PROFESOR_ID`) REFERENCES `profesor` (`ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_COLEGIO_ID` FOREIGN KEY (`COLEGIO_ID`) REFERENCES `colegio` (`ID`),
  CONSTRAINT `FK_EVALUACIONPRUEBA_CURSO_ID` FOREIGN KEY (`CURSO_ID`) REFERENCES `curso` (`ID`),
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formas`
--

LOCK TABLES `formas` WRITE;
/*!40000 ALTER TABLE `formas` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `habilidad`
--

LOCK TABLES `habilidad` WRITE;
/*!40000 ALTER TABLE `habilidad` DISABLE KEYS */;
INSERT INTO `habilidad` VALUES (1,'Habilidad 1','La habilidad de leer'),(2,'Habilidad 2','La habilidad de escribir'),(3,'Habilidad 3','La habilidad de contar');
/*!40000 ALTER TABLE `habilidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nivel`
--

DROP TABLE IF EXISTS `nivel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nivel` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nivel`
--

LOCK TABLES `nivel` WRITE;
/*!40000 ALTER TABLE `nivel` DISABLE KEYS */;
INSERT INTO `nivel` VALUES (1,'Ciclo 1'),(2,'Ciclo 2');
/*!40000 ALTER TABLE `nivel` ENABLE KEYS */;
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
INSERT INTO `nivelevaluacion` VALUES (1,'Insuficiete/Suficiente/Avanzado',3);
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
  CONSTRAINT `NIVELEVALUACION_RANGOEVALUACION_nivelevaluacion_ID` FOREIGN KEY (`nivelevaluacion_ID`) REFERENCES `nivelevaluacion` (`ID`),
  CONSTRAINT `FK_NIVELEVALUACION_RANGOEVALUACION_rangos_ID` FOREIGN KEY (`rangos_ID`) REFERENCES `rangoevaluacion` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nivelevaluacion_rangoevaluacion`
--

LOCK TABLES `nivelevaluacion_rangoevaluacion` WRITE;
/*!40000 ALTER TABLE `nivelevaluacion_rangoevaluacion` DISABLE KEYS */;
INSERT INTO `nivelevaluacion_rangoevaluacion` VALUES (1,1),(1,2),(1,3);
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesor`
--

LOCK TABLES `profesor` WRITE;
/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` VALUES (1,'Eliecer Enrique','Osorio','10613781-1','Verdugo');
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
  `PUNTAJEBASE` int(11) DEFAULT NULL,
  `NROPREGUNTAS` int(11) DEFAULT NULL,
  `NROFORMAS` int(11) DEFAULT NULL,
  `FECHA` bigint(20) DEFAULT NULL,
  `ALTERNATIVAS` int(11) DEFAULT NULL,
  `NAME` varchar(100) DEFAULT NULL,
  `ASIGNATURA_ID` bigint(20) DEFAULT NULL,
  `TIPOPRUEBA_ID` bigint(20) DEFAULT NULL,
  `CURSO_ID` bigint(20) DEFAULT NULL,
  `NIVELEVALUACION_ID` bigint(20) DEFAULT NULL,
  `PROFESOR_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_PRUEBA_ASIGNATURA_ID` (`ASIGNATURA_ID`),
  KEY `FK_PRUEBA_NIVELEVALUACION_ID` (`NIVELEVALUACION_ID`),
  KEY `FK_PRUEBA_TIPOPRUEBA_ID` (`TIPOPRUEBA_ID`),
  KEY `FK_PRUEBA_PROFESOR_ID` (`PROFESOR_ID`),
  KEY `FK_PRUEBA_CURSO_ID` (`CURSO_ID`),
  CONSTRAINT `FK_PRUEBA_CURSO_ID` FOREIGN KEY (`CURSO_ID`) REFERENCES `tipocurso` (`ID`),
  CONSTRAINT `FK_PRUEBA_ASIGNATURA_ID` FOREIGN KEY (`ASIGNATURA_ID`) REFERENCES `asignatura` (`ID`),
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
INSERT INTO `prueba` VALUES (1,1,30,3,16332,5,'Prueba 1',1,1,1,1,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rangoevaluacion`
--

LOCK TABLES `rangoevaluacion` WRITE;
/*!40000 ALTER TABLE `rangoevaluacion` DISABLE KEYS */;
INSERT INTO `rangoevaluacion` VALUES (1,'INS',1,'Insuficiente',3),(2,'SUF',3.1,'Suficiente',5),(3,'AV',5.1,'Avanzado',7);
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
  CONSTRAINT `FK_RESPUESTASESPERADASPRUEBA_PRUEBA_ID` FOREIGN KEY (`PRUEBA_ID`) REFERENCES `prueba` (`ID`),
  CONSTRAINT `FK_RESPUESTASESPERADASPRUEBA_EJETEMATICO_ID` FOREIGN KEY (`EJETEMATICO_ID`) REFERENCES `ejetematico` (`ID`),
  CONSTRAINT `FK_RESPUESTASESPERADASPRUEBA_HABILIDAD_ID` FOREIGN KEY (`HABILIDAD_ID`) REFERENCES `habilidad` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `respuestasesperadasprueba`
--

LOCK TABLES `respuestasesperadasprueba` WRITE;
/*!40000 ALTER TABLE `respuestasesperadasprueba` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipocurso`
--

LOCK TABLES `tipocurso` WRITE;
/*!40000 ALTER TABLE `tipocurso` DISABLE KEYS */;
INSERT INTO `tipocurso` VALUES (1,'Primero'),(2,'Segundo');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipoprueba`
--

LOCK TABLES `tipoprueba` WRITE;
/*!40000 ALTER TABLE `tipoprueba` DISABLE KEYS */;
INSERT INTO `tipoprueba` VALUES (1,'Regular'),(2,'SIMCE');
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

-- Dump completed on 2014-09-19 18:17:58
