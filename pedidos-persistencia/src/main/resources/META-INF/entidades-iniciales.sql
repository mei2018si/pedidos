--
-- Dumping data for table `Familia`
--

LOCK TABLES `Familia` WRITE;
INSERT INTO `Familia` VALUES (1,'tubos de todas clases','tubos');
UNLOCK TABLES;

--
-- Dumping data for table `Articulo`
--

LOCK TABLES `Articulo` WRITE;
INSERT INTO `Articulo` VALUES (1,'tubo de acero','tubo acero',10,1),(2,'tubo de plastico','tubo plastico',5,1),(3,'codo de plastico 90','codo plastico 90',20,1);
UNLOCK TABLES;

--
-- Dumping data for table `Cliente`
--

LOCK TABLES `Cliente` WRITE;
INSERT INTO `Cliente` VALUES ('11111111A','codigoPostal1','domicilio1','localidad1','provincia1','telefono1','nombre1 apelido1'),('22222222B','codigoPostal2','domicilio2','localidad2','provincia2','telefono2','nombre2 apellido2'),('33333333C','codigoPostal3','domicilio3','localidad3','provincia3','telefono3','nombre3 apellido3');
UNLOCK TABLES;


--
-- Dumping data for table `Pedido`
--

LOCK TABLES `Pedido` WRITE;
INSERT INTO `Pedido` VALUES (1,'PENDIENTE','2018-11-19','11111111A');
UNLOCK TABLES;


--
-- Dumping data for table `LineaPedido`
--

LOCK TABLES `LineaPedido` WRITE;
INSERT INTO `LineaPedido` VALUES (1,2,10,1,1),(2,5,5,2,1),(3,1,18,3,1);
UNLOCK TABLES;

