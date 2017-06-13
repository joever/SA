--
-- Table structure for table `publicado`
--

DROP TABLE IF EXISTS `publicado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publicado` (
  `idpublicado` int(11) NOT NULL,
  `idrede` int(11) NOT NULL,
  `cpf` varchar(11) NOT NULL,
  `idsensor` int(11) NOT NULL,
  `idtm` int(11) NOT NULL,
  `dthrgeracao` varchar(45) NOT NULL,
  `dthrenvio` varchar(45) NOT NULL,
  `valor` double NOT NULL,
  `coverage` double DEFAULT NULL,
  `up_to_dateness` double DEFAULT NULL,
  `accuracy` double DEFAULT NULL,
  `completeness` double DEFAULT NULL,
  `significance` double DEFAULT NULL,
  `qoc_geral` double DEFAULT NULL,
  PRIMARY KEY (`idpublicado`,`idrede`,`cpf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publicado`
--

LOCK TABLES `publicado` WRITE;
/*!40000 ALTER TABLE `publicado` DISABLE KEYS */;
/*!40000 ALTER TABLE `publicado` ENABLE KEYS */;
UNLOCK TABLES;