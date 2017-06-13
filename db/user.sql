--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `nome` varchar(255) NOT NULL,
  `apelido` varchar(20) DEFAULT NULL,
  `cpf` varchar(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `ultimoLogin` varchar(19) DEFAULT NULL,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Tabela de cadastro do usuário.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('Joéver Silva Hoffman','JohnForever','08603179786','joeversh@gmail.com',NULL,'joever','491de86cad249c2cf8815a9c95ceea8f');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;