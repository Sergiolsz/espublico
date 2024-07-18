### BASE DE DATOS MYSQL

- Schema: katas
- Tabla: Orders

#### Crear el Schema
create database katas;

#### Crear la tabla Orders

CREATE TABLE `orders` (
`order_id` VARCHAR(9) NOT NULL,
`uuid` VARCHAR(255) DEFAULT NULL,
`order_priority` ENUM('L', 'M', 'H', 'C') DEFAULT NULL,
`region` VARCHAR(255) DEFAULT NULL,
`country` VARCHAR(100) DEFAULT NULL,
`item_type` VARCHAR(100) DEFAULT NULL,
`sales_channel` ENUM('Offline', 'Online') DEFAULT NULL,
`order_date` DATE DEFAULT NULL,
`ship_date` DATE DEFAULT NULL,
`units_sold` INT DEFAULT NULL,
`unit_price` DECIMAL(8,2) DEFAULT NULL,
`unit_cost` DECIMAL(8,2) DEFAULT NULL,
`total_revenue` DECIMAL(8,2) DEFAULT NULL,
`total_cost` DECIMAL(8,2) DEFAULT NULL,
`total_profit` DECIMAL(8,2) DEFAULT NULL,
PRIMARY KEY (`order_id`)
);

#### Configuración Spring

- datasource:
  - url: jdbc:mysql://localhost:3306/katas
  - username: root
  - password: root
  - driver-class-name: com.mysql.cj.jdbc.Driver


## JAVA - SPRING

### CONTROLADORES

#### **OrdersController**

**OrdersController** proporciona endpoints RESTful para la gestión de pedidos, interactuando con un servicio de pedidos (OrdersService) para importar, consultar y
resumir datos de pedidos desde una API externa y una base de datos interna.

#### Endpoints

#### URL
```http
  GET /orders/import-summary
```

| Parameter     | Type     | Description                            |
| :--------     | :------- | :------------------------------------- |
| `page`        | `string` | Números de páginas para la petición    |
| `maxPerPage`  | `string` | Máximo de pedidos por página           |

#### Definición

- Importar y Resumir Pedidos
- Método HTTP: GET
- Path: /orders/import-summary
- Descripción: Importa pedidos desde la API externa de Katas, los guarda en la base de datos y genera un resumen de los datos importados.
- Parámetros:
  - page: Número de página para la solicitud (por defecto: 1).
  - maxPerPage: Máximo número de pedidos por página (por defecto: 100).
- Respuesta: SummaryResponse que contiene el resumen de los pedidos importados.

#### URL
```http
  GET /orders/${uuid}
```

| Parameter | Type     | Description                                              |
| :-------- | :------- | :------------------------------------------------------  |
| `uuid`      | `string` | **Required**. Identificador del pedido en formato UUID |

#### Definición

- Obtener un pedido por su Identificador en formato UUID.
- Método HTTP: GET
- Path: /orders/{uuid}
- Descripción: Recupera un pedido específico por su UUID desde la API externa de Katas.
- Parámetros:
  - uuid: Identificador único del pedido.
- Respuesta: OrdersSingleResponse que contiene el pedido

#### URL
```http
  GET /orders
```

#### Definición

- Obtener todos los pedidos de la base de datos.
- Método HTTP: GET
- Path: /orders
- Descripción: Recupera todos los pedidos almacenados en la base de datos interna.
- Respuesta: OrdersListResponse que contiene una lista de todos los pedidos almacenados.

----

#### **OrdersFileController**

OrdersFileController proporciona un endpoint RESTful para generar y descargar un archivo CSV que contiene datos de pedidos.

#### URL
```http
  GET /file/csv
```

#### Definición

- Generar y descarga un archivo CSV de pedidos.
- Método HTTP: GET
- Path: /file/csv
- Descripción: Genera un archivo CSV con los datos de pedidos y permite su descarga.
- Respuesta: Un archivo CSV con los datos de pedidos listo para ser descargado.

---

### SERVICIOS

#### **OrdersServiceImpl**

La clase **OrdersServiceImpl** implementa la interfaz *OrdersService* y proporciona la lógica de negocio para interactuar con pedidos desde una API externa y una base de datos local.

#### Descripción

**OrdersServiceImpl** es un servicio de backend que facilita la importación, gestión y recuperación de pedidos mediante la integración con una API externa (API de Katas) y un repositorio local de base de datos (Schema katas).

#### Atributos
- **OrdersClientService**: Servicio para interactuar con la API externa de pedidos.
- **OrdersRepositoryService**: Servicio para interactuar con el repositorio de base de datos local de pedidos.
- **OrdersMethods**: Utilidades y métodos auxiliares para la transformación y manipulación de pedidos.

#### Metodos

#### **importAndSummarizeOrders(String page, String maxPerPage)**

| Parameter     | Type     | Description                            |
| :--------     | :------- | :------------------------------------- |
| `page`        | `string` | Números de páginas para la petición    |
| `maxPerPage`  | `string` | Máximo de pedidos por página           |

#### Descripción

Importa pedidos desde la API externa, los transforma en entidades de base de datos, los guarda y luego genera un resumen de los pedidos importados.

#### **getOrderByUUID(String uuid)**

| Parameter | Type     | Description                                              |
| :-------- | :------- | :------------------------------------------------------  |
| `uuid`    | `string` | **Required**. Identificador del pedido en formato UUID   |

#### Descripción

Obtiene un pedido específico por su UUID desde la API externa y lo transforma en un DTO de pedido para la respuesta.

#### **getOrders()**

Sin parámetros

#### Descripción

Recupera todos los pedidos almacenados en la base de datos local

---
#### Proceso y uso del Servicio

- **Importación y Almacenamiento:** Utiliza ordersClientService para obtener pedidos desde la API externa, los convierte en entidades Orders y los guarda en la base de datos local mediante ordersRepositoryService.
- **Transformación de Datos:** Usa ordersMethods para convertir entre diferentes representaciones de pedidos (DTOs y entidades).
- **Generación de Resumen:** Calcula estadísticas y genera un resumen de los pedidos importados, que se devuelve en SummaryResponse
---

#### **OrdersFileServiceImpl**

La clase OrdersFileServiceImpl implementa la interfaz OrdersFileService y se encarga de generar archivos CSV a partir de datos de pedidos obtenidos de una base de datos, manejando excepciones específicas de archivo y procesamiento.

#### Descripción

OrdersFileServiceImpl es un servicio que facilita la generación de archivos CSV a partir de datos de pedidos almacenados en una base de datos. Utiliza métodos auxiliares y mapeadores para transformar los datos y gestionar errores durante el proceso de generación del archivo.

#### Metodos

#### **generateOrderFileCSV()**

#### Descripción

Recupera todos los registros de pedidos desde la base de datos, los convierte en DTOs de pedidos utilizando un mapeador, y genera un archivo CSV a partir de estos datos.

---
#### Proceso y uso del Servicio

- **Obtención de Datos:** Utiliza ordersRepositoryService para recuperar todos los registros de pedidos almacenados en la base de datos.
- **Transformación de Datos:** Utiliza ordersMapper para convertir entidades de pedidos en DTOs de pedidos.
- **Generación de Archivo CSV:** Utiliza ordersFileMethods para generar un archivo CSV a partir de los DTOs de pedidos y devuelve una respuesta encapsulada en OrdersFileResponse.
---

### UTILIDADES

#### **OrdersFileMethods**

La clase **OrdersFileMethods** es una utilidad para la generación de archivos CSV a partir de datos de pedidos representados por objetos OrdersDTO, manejando también la configuración de encabezados HTTP necesarios para la descarga del archivo.

#### Descripción

**OrdersFileMethods** proporciona métodos para generar contenido CSV desde una lista de OrdersDTO, configurar encabezados HTTP para la descarga del archivo CSV y manejar excepciones relacionadas con la generación de archivos.

#### Método Público

#### **generateOrderFileCSV(List<OrdersDTO> ordersDtoList, String fileName)**

| Parameter       | Type              | Description                                 |
| :--------       | :-------          | :------------------------------------------ |
| `ordersDtoList` | `List<OrdersDTO>` | **Required**. Listado de Pedidos            |
| `fileName`      | `String`          | **Required**. Nombre del fichero            |

#### Descripción

Genera un archivo CSV completo a partir de una lista de OrdersDTO, configurando los encabezados HTTP y retornando un objeto OrdersFileResponse que encapsula los bytes del archivo y los encabezados.

- ***Return***

**OrdersFileResponse** que contiene los bytes del archivo CSV generado y los encabezados HTTP necesarios para la descarga.

#### Métodos Privados

#### **generateContentCSVFile(List<OrdersDTO> ordersDTOList)**

| Parameter       | Type              | Description                                 |
| :--------       | :-------          | :------------------------------------------ |
| `ordersDtoList` | `List<OrdersDTO>` | **Required**. Listado de Pedidos            |

#### Descripción

Genera el contenido del archivo CSV a partir de una lista de OrdersDTO, utilizando la librería OpenCSV para escribir los datos en formato CSV.

- ***Return***

Método void

#### **getCsvHttpHeaders(byte[] csvBytes, String filename)**
| Parameter  | Type      | Description                                 |
| :--------  | :-------  | :------------------------------------------ |
| `csvBytes` | `(byte[]` | **Required**. Contenido del fichero         |
| `fileName` | `String`  | **Required**. Nombre del fichero            |

#### Descripción

Configura los encabezados HTTP necesarios para la descarga de un archivo CSV, incluyendo el tipo de contenido, disposición de contenido y longitud del archivo.

***Return***

HttpHeaders configurados adecuadamente para el archivo CSV.

#### **generateUniqueFileName(String filename)**

| Parameter  | Type      | Description                                 |
| :--------  | :-------  | :------------------------------------------ |
| `fileName` | `String`  | **Required**. Nombre del fichero            |

#### Descripción

Genera un nombre de archivo único para evitar colisiones usando un número aleatorio.

- ***Return***

Nombre de archivo único con extensión ".csv".

---
#### Proceso y uso del Servicio

- **Generación de Contenido CSV**: Utiliza generateContentCSVFile para generar el contenido del archivo CSV a partir de los datos de pedidos.
- **Configuración de Encabezados HTTP**: Utiliza getCsvHttpHeaders para establecer los encabezados HTTP necesarios para la descarga del archivo CSV.
- **Manejo de Excepciones**: Captura y maneja excepciones relacionadas con errores de entrada/salida durante la generación del archivo CSV.

---

#### **OrdersMethods**

La clase **OrdersMethods** proporciona métodos utilitarios para el procesamiento y manipulación de datos relacionados con pedidos, incluyendo la conversión entre diferentes tipos de DTO y entidades, así como la generación de resúmenes estadísticos basados en los datos de pedidos.

#### Descripción

**OrdersMethods** es una clase utilitaria diseñada para facilitar operaciones específicas relacionadas con pedidos, como la conversión de DTOs a entidades, la generación de resúmenes estadísticos y la manipulación de excepciones específicas del dominio.

#### Métodos Públicos

#### **convertToOrders(PaginatedOrderClientDTO paginatedOrderClientDTO)**

| Parameter | Type     | Description                                 |
| :-------- | :------- | :------------------------------------------ |
| `paginatedOrderClientDTO` | `PaginatedOrderClientDTO` | **Required**. Objeto respuesta API cliente   |

#### Descripción

Convierte objetos PaginatedOrderClientDTO provenientes de la API Katas a una lista de entidades Orders.

- ***Return***

**List<Orders>** - Listado de objetos mapeados a Orders (Entity)

#### **convertToOrdersDTO(List<Orders> orders)**

| Parameter | Type     | Description                                 |
| :-------- | :------- | :------------------------------------------ |
| `orders`  | `List<Orders>` | **Required**. Listado de objetos Orders (Entity)   |

#### Descripción

Convierte una lista de entidades Orders a una lista de objetos OrdersDTO

- ***Return***

**List<OrdersDTO>** - Listado de objetos mapeados a OrdersDTO (DTO de Orders)

#### **generateOrderSummary(List<OrdersDTO> ordersDTOList)**

| Parameter | Type     | Description                                 |
| :-------- | :------- | :------------------------------------------ |
| `ordersDTOList`  | `List<OrdersDTO>` | **Required**. Listado de objetos OrdersDTO |

#### Descripción

Genera un resumen estadístico de los pedidos basado en campos como región, país, tipo de ítem, canal de ventas y prioridad de orden.

- ***Return***

**SummaryDTO** - Objeto resumen para el conteo por cada tipo de los campos:

| Campos |
| :----- | 
| `Region`- `Country` - `Item Type` - `Sales Channel` - `Order Priority` | 

#### **convertContentClientDTOToOrderDTO(ContentClientDTO contentClientDTO)**

#### Descripción

Convierte un objeto ContentClientDTO a un objeto OrdersDTO.

- ***Return***

**OrdersDTO** - Objeto DTO con la información de un pedido

---

#### Métodos Privados

#### **convertToEntityWithExceptionHandling(ContentClientDTO contentClientDTO)**

#### Descripción

Convierte un objeto ContentClientDTO a una entidad Orders, manejando excepciones relacionadas con la validación de datos.

- ***Return***

**Orders** - Objeto Entity con la información de un pedido

#### **generateSummary(List<OrdersDTO> ordersDTOList, Function<OrdersDTO, String> classifier, String logField)**

#### Descripción

Genera un resumen agrupado de los pedidos basado en una función d

- ***Return***

**Map<String, Long>** - Objeto resumen con los datos por cada campo para el conteo por cada tipo de los campos:

| Campos |
| :----- | 
| `Region`- `Country` - `Item Type` - `Sales Channel` - `Order Priority` | 

---
#### Proceso y uso del Servicio

- **Conversión de Datos:** Utiliza métodos como convertToOrders y convertToOrdersDTO para manejar la conversión entre diferentes representaciones de datos de pedidos.
- **Generación de Resúmenes:** Utiliza generateOrderSummary para obtener estadísticas agrupadas de los pedidos según varios criterios.
- **Manejo de Excepciones:** Captura y maneja excepciones específicas del dominio, como *InvalidException* y *ProcessingException*, para asegurar la integridad y la consistencia de los datos procesados.

---

#### **OrdersValidations**

La clase **OrdersValidations** proporciona métodos utilitarios para validar diversos campos de datos utilizados en el servicio de órdenes. Se asegura de que los datos sean correctos y cumplan con los requisitos específicos antes de ser procesados.

#### Descripción

**OrdersValidations** es una clase utilitaria diseñada para realizar validaciones comunes en los datos de órdenes, tales como la validación de fechas, cadenas de texto y números. Las validaciones se aplican tanto a nivel de campo individual como a nivel de objeto completo.

#### Métodos Públicos

#### **validateParseDate(String dateString, String fieldName, String id)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `dateString` | `String` | **Required**. Fecha a validar            |
| `fieldName`  | `String` | **Required**. Nombre del campo para mensaje de excepciones |
| `id`         | `String` | **Required**.  Identificador asociado del objeto JSON |

#### Descripción

Genera un archivo CSV completo a partir de una lista de OrdersDTO, configurando los encabezados HTTP y retornando un objeto OrdersFileResponse que encapsula los bytes del archivo y los encabezados.

- ***Return***

Método void

#### **validateStringNotEmpty(String fieldValue, String fieldName, String id)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `fieldValue` | `String` | **Required**. Valor del campo a validar            |
| `fieldName`  | `String` | **Required**. Nombre del campo para mensaje de excepciones |
| `id`         | `String` | **Required**.  Identificador asociado del objeto JSON |

#### Descripción

Valida que una cadena de texto no esté vacía o sea nula.

- ***Return***

Método void

#### **validatePositiveNumber(Number fieldValue, String fieldName, String id)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `fieldValue` | `Number` | **Required**. Número a validar            |
| `fieldName`  | `String` | **Required**. Nombre del campo para mensaje de excepciones |
| `id`         | `String` | **Required**.  Identificador asociado del objeto JSON |

#### Descripción

Valida que un número sea positivo.

- ***Return***

Método void

#### **validateContentClientDTO(ContentClientDTO contentClientDTO)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `contentClientDTO` | `ContentClientDTO` | **Required**. Objeto a validar            |


#### Descripción

Validador de todos los campos del objeto ContentClientDTO, que es el contenido del JSON del pedido recibido en la llamada a la API de Katas.

***Return***

Método void

---
#### Consideraciones
- **Formato de Fecha:** Valida de que las cadenas de fecha cumplan con el formato "dd/MM/yyyy" antes de procesarlas.
- **Cadena No Vacía:** Verifique que las cadenas de texto no estén vacías o sean nulas antes de procesarlas.
- **Número Positivo:** Valida que los números sean positivos para evitar datos inválidos.
- **Manejo de Excepciones:** La clase lanza excepciones específicas para cada tipo de validación fallida, lo que facilita el manejo de errores en niveles superiores de la aplicación.
---

### MAPEADOR

#### **OrdersMapper**

La interfaz **OrdersMapper** proporciona métodos de mapeo entre los objetos de transferencia de datos (DTO) y las entidades del dominio relacionadas con las órdenes.
Utiliza MapStruct para generar automáticamente las implementaciones de los métodos de mapeo.

#### Descripción

**OrdersMapper** es una interfaz que define métodos para convertir entre ContentClientDTO, Orders, y OrdersDTO.

Estos métodos aseguran que los datos se transformen correctamente entre las diferentes capas de la aplicación.

#### Métodos Públicos

#### **contentClientDTOToOrders(ContentClientDTO contentClientDTO)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `contentClientDTO` | `ContentClientDTO` | **Required**. Objeto que mapear             |


#### Descripción

Convierte un objeto ContentClientDTO en una entidad Orders.

- ***Return***

Objeto Orders (Entity)

#### **ordersToOrderDTO(Orders orders)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `orders` | `Orders` | **Required**. Objeto que mapear            |


#### Descripción

Convierte una entidad Orders en un objeto OrdersDTO.

- ***Return***

Objeto OrderDTO

#### **contentClientDTOToOrderDTO(ContentClientDTO contentClientDTO)**

| Parameter    | Type     | Description                                 |
| :--------    | :------- | :------------------------------------------ |
| `contentClientDTO` | `ContentClientDTO` | **Required**. Objeto que mapear            |


#### Descripción

Convierte un objeto ContentClientDTO en un OrdersDTO.

- ***Return***

Objeto OrderDTO

---

#### Consideraciones
- **Formato de Fecha:** Asegúrese de que las fechas en los DTO se ajusten al formato dd/MM/yyyy para que los mapeos se realicen correctamente.
- **Gestión de Componentes de Spring:** La anotación @Mapper(componentModel = "spring") permite que el mapper sea gestionado por Spring, lo que facilita su inyección en otros componentes de la aplicación.

---

### EXCEPCIONES

#### **InvalidException**

#### Descripción
InvalidException es una excepción personalizada que extiende de RuntimeException.

Su uso está destinado para manejar diferentes tipos de errores de validación y procesamiento en la aplicación relacionados con formatos de fecha inválidos, valores de cadena no válidos y valores numéricos no válidos.

Proporciona constructores para manejar mensajes descriptivos de error y causas originales.

#### Constructores

#### **InvalidDateFormatException(String message, Throwable cause)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |
| `cause`   | `Throwable` | **Required**. Causa de la excepción         |

#### Descripción

Subclase de *ProcessingException* específica para errores relacionados con formatos de fecha inválidos.

#### **InvalidStringValueException(String message, Throwable cause)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |
| `cause`   | `Throwable` | **Required**. Causa de la excepción         |

#### Descripción:

Subclase de *ProcessingException* específica para errores relacionados con valores de cadena no válidos.

#### **InvalidNumberValueException(String message)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |

#### Descripción:

Subclase de *ProcessingException* específica para errores relacionados con valores numéricos no válidos.

---

#### **ProcessingException**

#### Descripción:
ProcessingException es una excepción propia que extiende de RuntimeException.

Su uso está destinado para representar errores generales durante el procesamiento de operaciones en la aplicación.

Proporciona constructores para manejar mensajes descriptivos de error y causas originales.

#### Constructores

#### **ProcessingException(String message)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |

#### Descripción

Construye una nueva ProcessingException con el mensaje detallado especificado.

#### **ProcessingException(String message, Throwable cause)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |
| `cause`   | `Throwable` | **Required**. Causa de la excepción         |

#### Descripción

Construye una nueva ProcessingException con el mensaje detallado especificado.

---

#### **FileException**

#### Descripción:
FileException es una excepción propia que extiende de RuntimeException.

Su uso está destinado para representar errores relacionados con operaciones de ficheros en la aplicación.

Proporciona constructores para manejar mensajes descriptivos de error y causas originales.

#### Constructores

#### **FileException(String message)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |

#### Descripción

Construye una nueva FileException con el mensaje detallado especificado.

#### **FileException(String message, Throwable cause)**

| Parameter | Type        | Description                                 |
| :-------- | :-------    | :------------------------------------------ |
| `message` | `String`    | **Required**. Mensaje de la excepción       |
| `cause`   | `Throwable` | **Required**. Causa de la excepción         |

#### Descripción:

Construye una nueva FileException con el mensaje detallado especificado y la causa de la excepción.

### CLIENT

#### **KatasClientFeign**

La interfaz **KatasClientFeign** define un cliente Feign para comunicarse con la API de Katas. Utiliza Feign para simplificar las llamadas HTTP, proporcionando una manera declarativa de interactuar con los servicios web externos.

#### Descripción

**KatasClientFeign** es una interfaz anotada con @FeignClient, lo que permite que Spring Boot gestione la configuración y las llamadas a la API de Katas. Define métodos para obtener órdenes paginadas y órdenes específicas por UUID.

#### Métodos Públicos

#### **getClientOrders(String page, String maxPerPage)**

| Parameter     | Type     | Description                            |
| :--------     | :------- | :------------------------------------- |
| `page`        | `string` | Números de páginas para la petición    |
| `maxPerPage`  | `string` | Máximo de pedidos por página           |

#### Características

- **Descripción:** Obtiene una lista paginada de órdenes desde la API de Katas.
- **Método HTTP:** GET
- **Ruta:** https://kata-espublicotech.g3stiona.com/v1/orders
- **Parámetros:**
  - page (query parameter): Número de página a recuperar. Valor por defecto es "1".
  - maxPerPage (query parameter): Número máximo de órdenes por página. Valor por     defecto es "100".
- **Return:**
  - PaginatedOrderClientDTO: DTO que contiene la lista paginada de órdenes.

#### **getClientOrderByUUID(String uuid)**

| Parameter       | Type              | Description                                 |
| :--------       | :-------          | :------------------------------------------ |
| `uuid` | `String` | **Required**. Identificador del pedido formato UUID           |

#### Características

- **Descripción:** Obtiene una orden específica por su UUID desde la API de Katas.
- **Método HTTP:** GET
- **Ruta:** https://kata-espublicotech.g3stiona.com/v1/orders/{uuid}
- **Parámetros:**
  - uuid (path variable): UUID de la orden a recuperar.
- **Return:**
  - ContentClientDTO: DTO que contiene la información de la orden específica.

#### Consideraciones

- **Parámetros por defecto:** Los parámetros page y maxPerPage tienen valores por defecto definidos en defaultValue. Esto asegura que las solicitudes tengan siempre un valor válido.
- **Anotaciones Feign:** Las anotaciones @GetMapping, @RequestParam, y @PathVariable se utilizan para definir las rutas y los parámetros de las solicitudes HTTP.

### REPOSITORY

#### **OrdersRepository**

La interfaz **OrdersRepository** extiende JpaRepository y proporciona métodos CRUD para la entidad Orders. Utiliza Spring Data JPA para simplificar las interacciones con la base de datos.

#### Descripción

**OrdersRepository** es una interfaz marcada con @Repository, lo que permite que Spring Boot la trate como un bean de repositorio. Al extender JpaRepository, hereda una serie de métodos para operaciones comunes de persistencia.

### SERVICE

#### **OrdersClientService**

La clase **OrdersClientService** es un servicio de Spring que proporciona métodos para interactuar con el servicio externo Katas utilizando un cliente Feign. Maneja la comunicación con el servicio externo y gestiona posibles excepciones que puedan surgir durante las llamadas.

#### Métodos

#### **getPagedOrdersClient(String page, String maxPerPage)**

Obtiene pedidos paginados del servicio externo.

| Parameter     | Type     | Description                            |
| :--------     | :------- | :------------------------------------- |
| `page`        | `string` | Números de páginas para la petición    |
| `maxPerPage`  | `string` | Máximo de pedidos por página           |

- #### Return

*PaginatedOrderClientDTO*: DTO paginado de órdenes obtenidos del servicio externo.

- #### Excepciones

*ProcessingException*: Lanzada si ocurre un error al obtener las órdenes paginadas, indicando que falló la llamada al servicio externo.

- #### Implementación

Llama al método getClientOrders del cliente Feign.
Maneja excepciones FeignException y otras excepciones, registrando el error y lanzando ProcessingException.

#### **getOrderByUUIDClient(String uuid)**

Obtiene un pedido por su UUID.

| Parameter     | Type     | Description                       |
| :--------     | :------- | :-------------------------------- |
| `uuid`        | `string` | Identificador único del pedido    |

- #### Return

*ContentClientDTO*: DTO que contiene los datos del pedido.

- #### Implementación

Llama al método getClientOrderByUUID del cliente Feign.
Registra la llamada y el resultado.

---
#### Consideraciones

- **Gestión de Errores:** Los métodos manejan excepciones y lanzan ProcessingException para errores de comunicación o cualquier otro fallo durante la obtención de datos.
- **Uso de Feign:** Utiliza Feign como cliente HTTP para realizar las llamadas al servicio externo de manera declarativa.
---

#### **OrdersRepositoryService**

La clase **OrdersRepositoryService** es un servicio de Spring que proporciona métodos para interactuar con la base de datos a través del repositorio OrdersRepository. Esta clase maneja las operaciones de persistencia y recuperación de datos de la tabla Orders.

#### Métodos

#### **saveAllOrders(List<Orders> ordersList)**

Obtiene pedidos paginados del servicio externo.

| Parameter     | Type     | Description                            |
| :--------     | :------- | :------------------------------------- |
| `ordersList`        | `List<Orders>` | Lista de Orders a guardar    |


- #### Return

Sin retorno

- #### Excepciones

*ProcessingException*: Lanzada si ocurre un error durante el proceso de guardado.

- #### Implementación

  - Intenta guardar la lista de pedidos utilizando el método saveAll del repositorio.
  - Registra el intento de guardado y el resultado.
  - Maneja cualquier excepción lanzada durante el proceso, registrándola y lanzando ProcessingException.

#### **findById(Long id)**

Recupera un pedido por su ID de la base de datos.

| Parameter     | Type     | Description                       |
| :--------     | :------- | :-------------------------------- |
| `id`        | `Long` | Identificador único del pedido asociado a la tabla (OrderID)    |

- #### Return

*Optional<Orders>*: El pedido encontrado, o un Optional vacío si no se encuentra

- #### Implementación

Utiliza el método findById del repositorio para recuperar el pedido por su ID.

#### **findAll()**

Recupera todos los pedidos de la base de datos.

- #### Return

*List<Orders>*: Listado de todos los pedidos

- #### Implementación

Utiliza el método findAll del repositorio para recuperar todos los pedidos.

---
#### Consideraciones

- **Gestión de Errores:** El método saveAllOrders maneja excepciones y lanza ProcessingException para cualquier fallo durante el guardado.
- **Uso del Repositorio:** Utiliza métodos proporcionados por OrdersRepository (saveAll, findById, findAll) para realizar operaciones de persistencia y recuperación de datos.
---