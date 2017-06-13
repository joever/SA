--
-- Table structure for table `redeconf`
--

DROP TABLE IF EXISTS `redeconf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `redeconf` (
  `idredeconf` int(11) NOT NULL AUTO_INCREMENT,
  `idrede` int(11) NOT NULL,
  `idtm` int(11) NOT NULL,
  `idusuario` varchar(11) NOT NULL,
  `a_valormedio` varchar(45) NOT NULL,
  `a_percentualaceitavel` varchar(45) NOT NULL,
  `u_tempovida` varchar(45) DEFAULT NULL,
  `c_inf` varchar(45) DEFAULT NULL,
  `c_sup` varchar(45) DEFAULT NULL,
  `s_op1` varchar(45) DEFAULT NULL,
  `s_valor1` varchar(45) DEFAULT NULL,
  `s_op2` varchar(45) DEFAULT NULL,
  `s_valor2` varchar(45) DEFAULT NULL,
  `f_frequenciageracao` varchar(45) DEFAULT NULL,
  `qoc_pa` varchar(45) DEFAULT NULL,
  `qoc_pu` varchar(45) DEFAULT NULL,
  `qoc_pc` varchar(45) DEFAULT NULL,
  `qoc_ps` varchar(45) DEFAULT NULL,
  `qoc_pf` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idredeconf`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `redeconf`
--

LOCK TABLES `redeconf` WRITE;
/*!40000 ALTER TABLE `redeconf` DISABLE KEYS */;
INSERT INTO `redeconf` VALUES (1,2,1,'08603179786','30','10','9999999','25','40','3','39','','','60','1','1','1','0','1'),(2,1,1,'08603179786','30','15','9999999','25','45','3','39','','','60','1','1','1','0','1'),(3,3,1,'08603179786','30','10','9999999','25','40','3','39','','','60','1','1','1','0','1'),(4,3,3,'08603179786','101325','10','9999999','100000','102999','3','102005','','','60','1','1','1','0','1');
/*!40000 ALTER TABLE `redeconf` ENABLE KEYS */;
UNLOCK TABLES;