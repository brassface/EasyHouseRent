-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: easyhouserent
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collection` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `belong_id` int DEFAULT NULL,
  `item` int DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `flag` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collection`
--

LOCK TABLES `collection` WRITE;
/*!40000 ALTER TABLE `collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house_need`
--

DROP TABLE IF EXISTS `house_need`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `house_need` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  `price` decimal(10,2) DEFAULT NULL,
  `author` int DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `flag` int DEFAULT '0',
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `area` decimal(2,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house_need`
--

LOCK TABLES `house_need` WRITE;
/*!40000 ALTER TABLE `house_need` DISABLE KEYS */;
INSERT INTO `house_need` VALUES (1,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(2,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1623.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(3,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(4,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(5,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(6,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(7,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(8,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(9,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(10,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(11,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(12,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(13,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(14,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(15,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(16,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(17,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30);
/*!40000 ALTER TABLE `house_need` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house_rent`
--

DROP TABLE IF EXISTS `house_rent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `house_rent` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  `price` decimal(10,2) DEFAULT NULL,
  `author` int DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `flag` int DEFAULT '0',
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `town` varchar(255) DEFAULT NULL,
  `area` decimal(2,0) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house_rent`
--

LOCK TABLES `house_rent` WRITE;
/*!40000 ALTER TABLE `house_rent` DISABLE KEYS */;
INSERT INTO `house_rent` VALUES (1,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(3,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(4,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(5,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(6,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(7,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(8,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(9,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(10,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(11,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(12,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(13,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(14,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30),(15,'标题','关于此房源\nTUDOR IN THE PINES\n\nTudor in the Pines隐藏在碧瑶市茂密的树叶中，是菲律宾的一个非凡的庄园，在门禁房源内有七（ 7 ）个独特的住宅，最多可住30位房客。 可方便地通往市区和科迪勒拉斯不同高地省份的多条公路。 Tudor in the Pines是游览菲律宾北方奇迹的完美基地。',1523.71,1,'近地铁``近公交',0,'江苏省','南京市','江宁区',30);
/*!40000 ALTER TABLE `house_rent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house_talk`
--

DROP TABLE IF EXISTS `house_talk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `house_talk` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `author` int DEFAULT NULL,
  `content` text,
  `tips` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `flag` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house_talk`
--

LOCK TABLES `house_talk` WRITE;
/*!40000 ALTER TABLE `house_talk` DISABLE KEYS */;
INSERT INTO `house_talk` VALUES (1,'南京租友集合qpofshnghssoaawoiubnaiefjosapjmiapjwipjannmgasokd',1,'【#2025开年好片连连# 精彩不断，期待满满！】@TFBOYS-易烊千玺 领衔主演的《小小的我》；@黄渤 @于适Yosh 等主演的《封神第二部：战火西岐》；@X玖少年团肖战DAYTOY @庄达菲要一直努力 等主演的《射雕英雄传：侠之大者》；@黄轩的微博 @于适Yosh @TFBOYS-王俊凯 等主演的《蛟龙行动》；@刘昊然turbo @王宝强 等主演的《唐探1900》……戳图↓评论区一起分享#2025元旦档春节档电影#你想看哪部吧！那必须是《射雕英雄传：侠之大者》，中国武侠来了，四海，准备接招吧，大年初一，一步江湖[赢牛奶][赢牛奶][赢牛奶][赢牛奶][赢牛奶][赢牛奶][赢牛奶]超级期待射雕英雄传侠之大者新年假期看射雕，好运福气都来到[作揖]射雕纯正的中国武侠味，中国年当然跟中国武侠更配啦[作揖]射雕必看！爱看众志成城保卫城池的家国大义[作揖]期待《射雕英雄传:侠之大者》本次电影周由柬埔寨王国文化与艺术部、福建省电影局、厦门市电影局指导，中共湖里区委宣传部与柬埔寨亚洲电影节组委会主办，旨在为中柬两国电影文化艺术交流发展搭建平台。\n                                                                                                                                                                                                                                                                                                                                                                                                          \n                                                                                                                                                                                                                                                                                                                                                                                                          　　鹭岛之上 星光璀璨\n                                                                                                                                                                                                                                                                                                                                                                                                          \n                                                                                                                                                                                                                                                                                                                                                                                                          　　现场LIVE\n                                                                                                                                                                                                                                                                                                                                                                                                          \n                                                                                                                                                                                                                                                                                                                                                                                                          　　精彩回顾\n                                                                                                                                                                                                                                                                                                                                                                                                          \n                                                                                                                                                                                                                                                                                                                                                                                                          　　12月29日下午，电影周在厦门湖里区海上世界寰映影城举办启动仪式。柬埔寨皇家传统乐队带来了柬埔寨特色表演，点燃了现场气氛。领导嘉宾们一同按下启动键，宣布2024中国·福建厦门-柬埔寨电影周正式启动。首次来到湖里区的意大利籍在柬导演\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               　　亨德森·丹尼尔·吉米点赞湖里\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               　　“这绝对是一个我想拍电影的地方”\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               　　荧幕之中 赏光影之旅\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               　　柬埔寨\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               　　电影放映光影交错 文艺融合\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     　　电影主题\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     　　分享沙龙快闪活动\n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           \n                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           　　12月29-30日，在厦门市湖里区海上世界，中柬两国的音乐家、表演乐队上演了精彩的演出，吸引许多市民驻足聆听。柬埔寨电影衍生市集也是热闹非凡，展示着琳琅满目的电影周边产品，露天电影放映区播放着经典影片，带领观众沉浸在电影的奇妙世界。','南京``短租``学生``实习','2025-01-02 20:25:12',0);
/*!40000 ALTER TABLE `house_talk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `picture` (
  `id` int NOT NULL AUTO_INCREMENT,
  `belong_id` int DEFAULT NULL,
  `no` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `flag` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture`
--

LOCK TABLES `picture` WRITE;
/*!40000 ALTER TABLE `picture` DISABLE KEYS */;
INSERT INTO `picture` VALUES (1,1,0,'http://localhost:9090/files/123',1,0),(2,1,1,'http://localhost:9090/files/234',1,0),(3,3,1,'http://localhost:9090/files/123',1,0),(4,3,0,'http://localhost:9090/files/123',1,0),(5,4,0,'http://localhost:9090/files/123',1,0),(6,5,0,'http://localhost:9090/files/123',1,0),(7,6,0,'http://localhost:9090/files/123',1,0),(8,7,0,'http://localhost:9090/files/123',1,0),(9,8,0,'http://localhost:9090/files/123',1,0),(10,9,0,'http://localhost:9090/files/123',1,0),(11,10,0,'http://localhost:9090/files/123',1,0),(12,11,0,'http://localhost:9090/files/123',1,0),(13,12,0,'http://localhost:9090/files/123',1,0),(14,13,0,'http://localhost:9090/files/123',1,0),(15,14,0,'http://localhost:9090/files/123',1,0),(16,15,0,'http://localhost:9090/files/123',1,0),(17,16,0,'http://localhost:9090/files/123',1,1),(18,17,0,'http://localhost:9090/files/123',1,1),(19,17,0,'http://localhost:9090/files/123',2,0),(20,1,2,'http://localhost:9090/files/234',1,0),(21,1,0,'http://localhost:9090/files/234',3,0),(22,1,1,'http://localhost:9090/files/234',3,0),(23,1,2,'http://localhost:9090/files/123',3,0),(24,1,3,'http://localhost:9090/files/234',3,0),(25,1,4,'http://localhost:9090/files/123',3,0),(26,1,5,'http://localhost:9090/files/234',3,0),(27,1,6,'http://localhost:9090/files/123',3,0),(28,1,7,'http://localhost:9090/files/234',3,0);
/*!40000 ALTER TABLE `picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `talk_item`
--

DROP TABLE IF EXISTS `talk_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `talk_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` text,
  `belong_item` int DEFAULT NULL,
  `author` int DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `flag` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `talk_item`
--

LOCK TABLES `talk_item` WRITE;
/*!40000 ALTER TABLE `talk_item` DISABLE KEYS */;
INSERT INTO `talk_item` VALUES (2,'3',1,1,'2025-01-02 21:49:17',0),(3,'3',1,1,'2025-01-02 21:49:17',0),(4,'3',1,1,'2025-01-02 21:49:17',0),(5,'3',1,1,'2025-01-02 21:49:17',0),(6,'3',1,1,'2025-01-02 21:49:17',0),(7,'3',1,1,'2025-01-02 21:49:17',0),(8,'3',1,1,'2025-01-02 21:49:17',0),(9,'3',1,1,'2025-01-02 21:49:17',0),(10,'3',1,1,'2025-01-02 21:49:17',0),(11,'3',1,1,'2025-01-02 21:49:17',0),(12,'3',1,1,'2025-01-02 21:49:17',0),(13,'3',1,1,'2025-01-02 21:49:17',0),(14,'3',1,1,'2025-01-02 21:49:17',0),(15,'3',1,1,'2025-01-02 21:49:17',0),(16,'3',1,1,'2025-01-02 21:49:17',0),(17,'3',1,1,'2025-01-02 21:49:17',0),(18,'3',1,1,'2025-01-02 21:49:17',0),(19,'3',1,1,'2025-01-02 21:49:17',0),(20,'3',1,1,'2025-01-02 21:49:17',0),(21,'3',1,1,'2025-01-02 21:49:17',0),(22,'23',1,1,'2025-01-02 21:27:50',0),(23,'333',1,1,'2025-01-07 05:45:21',0),(24,'2321312',1,1,'2025-01-07 06:43:20',0);
/*!40000 ALTER TABLE `talk_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `role` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `flag` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','123','管理员A1qsDFsgeshgfdjrtdEdFdsfeSdfd','123','中国',1,'http://localhost:9090/files/a691aca891ed440d9ac27fac87c2a020',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-11 16:28:44
