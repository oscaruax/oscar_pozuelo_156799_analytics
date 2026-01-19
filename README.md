Student Analytics

Proyecto: oscar_pozuelo_156799_analytics

1. Descripción general

Student Analytics es una aplicación desarrollada con Spring Boot 3.x que combina en un único proyecto:

✔ Procesamiento batch con Spring Batch

✔ API reactiva con Spring WebFlux

✔ Simulación de microservicios (API Gateway interno)

✔ Patrones de resiliencia (Circuit Breaker)

✔ Trazas distribuidas mediante traceId

La aplicación gestiona información de estudiantes y sus notas medias, cargadas desde un fichero CSV a una base de datos H2 en memoria.

No incluye interfaz web; todas las pruebas se realizan mediante Postman, cURL o navegador.

2. Tecnologías utilizadas

Java 21

Spring Boot 3.2.x

Spring WebFlux

Spring Batch 5

Spring Data JPA

Base de datos H2 (en memoria)

Resilience4j (Circuit Breaker)

Maven

Netty (servidor reactivo)

3. Estructura del proyecto
oscar_pozuelo_156799_analytics
├── src/main/java/com/oscar/analytics
│   ├── batch        → Configuración Spring Batch
│   ├── model        → Entidad Student
│   ├── repository   → Repositorio JPA
│   ├── service      → Lógica de negocio y cliente WebClient
│   └── web
│       ├── StudentController   → API reactiva
│       └── GatewayController   → API Gateway simulado
├── src/main/resources
│   ├── students.csv
│   └── application.properties
└── README.md

4. Proceso Batch (Spring Batch – UD6)

Al arrancar la aplicación se ejecuta automáticamente un Job de Spring Batch que:

Lee el fichero students.csv

Procesa cada registro (normalización de datos)

Inserta los estudiantes en la tabla students de H2

Formato del CSV
id,name,averageGrade
1,Ana,7.5
2,Luis,5.2
3,Marta,8.9
4,Carlos,6.4
5,Lucia,9.1

Características del Job

ItemReader: FlatFileItemReader<Student>

ItemProcessor: Normalización básica

ItemWriter: Inserción en H2 mediante JPA

Step: Tipo chunk (tamaño 5)

Ejecución automática al iniciar la aplicación

5. API Reactiva (Spring WebFlux – UD4)

La aplicación expone una API completamente reactiva basada en Flux y Mono.

5.1 Obtener todos los estudiantes
GET /students


Ejemplo de respuesta:

[
  { "id": 1, "name": "Ana", "averageGrade": 7.5 },
  { "id": 2, "name": "Luis", "averageGrade": 5.2 }
]

5.2 Filtrar estudiantes por nota mínima
GET /students/top?min=7.0


Devuelve solo los estudiantes cuya nota media es mayor o igual al valor indicado.

5.3 (Opcional) Stream reactivo
GET /students/stream


Devuelve los estudiantes de forma progresiva usando delayElements, simulando un flujo de datos en tiempo real.

6. Mini arquitectura de microservicios (UD5)

Aunque todo se encuentra en un único proyecto, se simula una arquitectura de microservicios.

6.1 API Gateway simulado
GET /api/public/students


Este endpoint actúa como API Gateway:

Recibe la petición externa

Llama internamente al microservicio de estudiantes

Devuelve la respuesta al cliente

La llamada interna se realiza mediante WebClient.

6.2 Circuit Breaker (Resilience4j)

El servicio que consume la API de estudiantes tiene un Circuit Breaker configurado que:

Detecta fallos o timeouts

Evita que el sistema completo se bloquee

Devuelve una respuesta de fallback (lista vacía o datos por defecto)

Esto demuestra el patrón de resiliencia en microservicios.

6.3 Trazas con traceId

En cada petición al API Gateway:

Se genera un traceId único (UUID)

Se incluye en los logs

Se devuelve al cliente en la cabecera HTTP X-Trace-Id

Esto simula Distributed Tracing / Central Log Analysis, permitiendo seguir una petición completa a través del sistema.

7. Cómo ejecutar la aplicación
Requisitos

Java 21

Maven

Eclipse IDE (o cualquier IDE compatible)

Pasos

Clonar o descomprimir el proyecto

Importar como proyecto Maven en Eclipse

Ejecutar la clase:

OscarPozuelo156799AnalyticsApplication


La aplicación arranca en:

http://localhost:8080

8. Endpoints de prueba (Postman / navegador)
Endpoint	Descripción
/students	Lista completa de estudiantes
/students/top?min=7.0	Filtrado por nota
/students/stream	Flujo reactivo
/api/public/students	API Gateway
/h2-console	Consola H2
9. Base de datos H2

URL JDBC: jdbc:h2:mem:studentsdb

Usuario: sa

Password: (vacío)

10. Conclusión

Este proyecto demuestra:

Uso correcto de Spring Batch para procesos ETL

Desarrollo de una API reactiva con Spring WebFlux

Simulación de una arquitectura de microservicios

Implementación de Circuit Breaker

Uso de traceId para trazabilidad

Cumple completamente los requisitos funcionales y tecnológicos indicados en el enunciado.
