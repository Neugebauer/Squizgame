CREATE DATABASE  IF NOT EXISTS `squizgame` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `squizgame`;
-- MySQL dump 10.13  Distrib 5.5.16, for Win32 (x86)
--
-- Host: localhost    Database: squizgame
-- ------------------------------------------------------
-- Server version	5.5.21

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
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `answer` (
  `idAnswer` int(11) NOT NULL AUTO_INCREMENT,
  `answerText` varchar(45) NOT NULL,
  `isCorrect` tinyint(1) NOT NULL DEFAULT '0',
  `Question_idQuestion` int(11) NOT NULL,
  PRIMARY KEY (`idAnswer`,`Question_idQuestion`),
  UNIQUE KEY `idAnswer_UNIQUE` (`idAnswer`),
  KEY `fk_Answer_Question1` (`Question_idQuestion`),
  CONSTRAINT `fk_Answer_Question1` FOREIGN KEY (`Question_idQuestion`) REFERENCES `question` (`idQuestion`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,'Handel',1,1),(2,'Bach',0,1),(3,'Vivaldi',0,1),(4,'Verdi',0,1),(5,'Red',1,2),(6,'Blue',0,2),(7,'Violet',0,2),(8,'White',0,2),(9,'Sweden',1,3),(10,'Norway',0,3),(11,'Denmark',0,3),(12,'Finland',0,3),(13,'Mortimer Mouse',1,4),(14,'Maurice Mouse',0,4),(15,'Melvin Mouse',0,4),(16,'Milo Mouse',0,4),(17,'Aluminum',1,5),(18,'Boron',0,5),(19,'Silicon',0,5),(20,'Magnesium',0,5),(21,'Blue Whale',1,6),(22,'Elephant',0,6),(23,'Sperm Whale',0,6),(24,'Hippo',0,6),(25,'Rosemary and Thyme',1,7),(26,'Basil and Chives',0,7),(27,'Lavendar and Nutmeg',0,7),(28,'Curry and Safron',0,7);
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `idCategory` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(45) NOT NULL,
  PRIMARY KEY (`idCategory`),
  UNIQUE KEY `idCategory_UNIQUE` (`idCategory`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Music'),(2,'Classical'),(3,'Movies'),(4,'Sports'),(5,'Technology'),(6,'Politics'),(7,'Religion'),(8,'Entertainment'),(9,'Cartoons'),(10,'Science'),(11,'Geography'),(12,'Animals');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `idQuestion` int(11) NOT NULL AUTO_INCREMENT,
  `questionText` varchar(500) NOT NULL,
  `pointValue` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`idQuestion`),
  UNIQUE KEY `idQuestion_UNIQUE` (`idQuestion`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'Which composer wrote The Water Music?',1),(2,'What colour does acid turn Litmus paper?',1),(3,'What\'s the largest Scandinavian country?',1),(4,'What was Mickey Mouse\'s original name?',1),(5,'Which metal do you get from bauxite?',1),(6,'Which animal produces the biggest baby?',1),(7,'Which two herbs go with \'Parsley & Sage\' in the song Scarborough Fair?',1);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questioncategory`
--

DROP TABLE IF EXISTS `questioncategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questioncategory` (
  `idQuestion` int(11) NOT NULL,
  `idCategory` int(11) NOT NULL,
  PRIMARY KEY (`idQuestion`,`idCategory`),
  KEY `fk_Question_has_Category_Category1` (`idCategory`),
  KEY `fk_Question_has_Category_Question1` (`idQuestion`),
  CONSTRAINT `fk_Question_has_Category_Question1` FOREIGN KEY (`idQuestion`) REFERENCES `question` (`idQuestion`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Question_has_Category_Category1` FOREIGN KEY (`idCategory`) REFERENCES `category` (`idCategory`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questioncategory`
--

LOCK TABLES `questioncategory` WRITE;
/*!40000 ALTER TABLE `questioncategory` DISABLE KEYS */;
INSERT INTO `questioncategory` VALUES (1,1),(7,1),(4,8),(4,9),(2,10),(5,10),(3,11),(6,12);
/*!40000 ALTER TABLE `questioncategory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-03-16 17:32:57
