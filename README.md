## Ejemplo de integración JPA y ZK

### Estructura del proyecto

Se trata de un proyecto Maven principal, `pedidos`, con dos módulos (ver declaración `modules` en `pom.xml`), que emplea _packaging_ de tipo `pom`.
* Subproyecto `pedidos-persistencia`: proyecto con las entidades JPA de la aplicación y objetos DAO para implementar las operaciones sobre las entidades. (Es  una versión con pequeños ajustes del proyecto `jpainicial` disponible en <https://github.com/mei2018si/jpainicial>)
* Subproyecto `pedidos-web`: proyecto ZK con la capa de presetación de aplicación. En us fichero `pom.xml`se declara una depencia con el proyecto `pedidos-persistencia`.

```
tree -L 4 pedidos
pedidos
├── pedidos-persistencia
│   ├── pom.xml
│   └── src
│       └── main
│           ├── java
│           └── resources
├── pedidos-web
│   ├── pom.xml
│   └── src
│       └── main
│           ├── assembly
│           ├── java
│           └── webapp
├── pom.xml
└── README.md

11 directories, 4 files
```

#### Compilación y ejecución

##### Compilación/empaquetado
Para asegurar que se verifican las dependencias entre los proyectos lo más sencillo es compilar, empaquetar e ''instalar'' el proyecto principal `pedidos` (sólo necesario cuando se realicen cambios en la configuración o en las clases de `pedidos-persistencia`)

```
(desde pedidos)

mvn install
```
* Se compilarán y empaquetarán los dos subproyectos, asegurando que la dependencia de `pedidos-web` respecto de `pedidos-persistencia`.

##### Ejecución del proyecto ZK
Hecho lo anterios, si no hay cambios en el proyecto `pedidos-persistencia`, puede compilarse el proyecto `pedidos-web` de forma independiente.

Para acceder al interfaz ZK se puede desplegar el proyecto desde línea de comandos con `mvn jetty:run` (usando el servidor Jetty) o desde el IDE Eclipse con la opción `Run As > run in Server` (usando el servidor Tomcat 9)
```
(desde pedidos-web)

mvn jetty:run
```

La aplicación ZK estará disponible en la URL <http://localhost:8080/pedidos-web>

### Detalles de `pedidos-persistencia`
Se trata de una versión con pequeños ajustes del proyecto `jpainicial` disponible en <https://github.com/mei2018si/jpainicial>

#### Cambios relevantes

1. En el fichero `src/main/resources/META-INF/persistence.xml` con la configuración de la _persistence unit_ de JPA, se ha añadido la opción `sql-load-script-source` para que una vez creada la BD se carguen unas entidades de partida desde el script `META-INF/entidades-iniciales.sql`

   **Nota:** En la configuración de `persistence.xml` se establece el valor `drop-and-create` en el parámetro `schema-generation.database.action`. 
      * Esto hará que cada vez que se despluiegue la aplicación se eliminen y vuelvan a crear las tablas de la Base de Datos.
      * Es una configuración válida sólo para entornos de prueba. En la aplicación en producción se usaría el valor `none` y la Base de Datos y sus tablas ya existirían.
2. Se ha añadido el paquete `src/main/java/es/uvigo/mei/pedidos/servicios/` donde se ubican las clases Java responsables de gestionar las entidades de la aplicación.
   * Se implementa una aproximación al patrón DAO (_Data Access Object_). Por simplicidad se ha omitido separar en un _interface_ la definción de las operaciones, quedando únicamente la clase de implemenatación usando JPA.
   * Las operaciones sobre las entidades (creación, modificación, borrado y consultas) se ejecutan mediante una instancia
    de un `EntityManager` de JPA que las clases DAO esperan recibir en su constructor.
   * Esa instancia de`EntityManager` debe estar configurada para acceder la la _persistence unit_ del proyecto. Será responsabilidad del entorno donde se usen estos DAO proporcionar ese `EntityManager` (en nuestro caso será responsabilidad del proyecto `pedidos-web`).


### Detalles de `pedidos-web`
Se trata de un proyecto ZK creado con el arquetipo `zk-archetype-webapp`, haciendo uso del patrón MVVM (_Model-View-ViewModel_).

En el fichero `pom.xml` se declara una dependencia con el proyecto `pedidos-persistencia`, para que sean accesibles las entidades y los DAOs definidos en el mismo.

#### Organización de las vistas
* Como ejemplo del uso de "plantillas" en ZK, las vistas desarrolladas toman como base el fichero `src/main/webapp/WEB-INF/plantillas/base.zul`, que define dos "puntos de inserción", `menu` y `contenido`.
* Cada vista instancia esta "plantilla" `base.zul` especificando el contenido a incluir en ambos puntos de inserción. 
* En `src/main/webapp/WEB-INF/plantillas/menu.zul` se define el menú general de la aplicación, con las URL de acceso a las diferentes vistas.

#### Gestión del _EntityManager_
* Las clases de respaldo de cada vista (su/s _ViewModel_) se ubican en el paquete `es.uvigo.mei.pedidos.vista` y se usa la convención `[nombre_vista]VM.java`
* Los _ViewModel_ que los necesiten deben mantiener una referencia a los DAOs que vayan a ser utilizados. Estos DAOs se crean e inicializan en un método de inicialización anotado con `@Init`.
* Para proporcionar un `EntityManager` a los DAOs utilizados se hace uso de la clase de utilidad `org.zkoss.zkplus.jpa.JpaUtil`, que ofrece el método `getEntityManager()`.
* Este método de `org.zkoss.zkplus.jpa.JpaUtil` require que se proporciona el nombre de la _Persistence Unit_ definida en el fichero `persistence.xml` (en este caso el nombre es `jpainicial.PU`) mediante una propiedad _preference_ declarada en el fichero de configuración de ZK `zk.xml` ubicando en `src/main/webapp/WEB-INF/`.

```
cat src/main/webapp/WEB-INF/zk.xml
<?xml version="1.0" encoding="UTF-8"?>
<zk>
        <preference>
                <name>JpaUtil.PersistenceUnitName</name>
                <value>jpainicial.PU</value>
        </preference>
</zk>

```