# Student Analytics – Spring Boot Project

Proyecto desarrollado como trabajo práctico para la asignatura relacionada con arquitecturas de microservicios, programación reactiva y procesos batch.

La aplicación **Student Analytics** carga información de estudiantes desde un fichero CSV mediante un proceso batch, la almacena en una base de datos en memoria y la expone a través de una API reactiva.

---

## 📌 Tecnologías utilizadas

- Java 21  
- Spring Boot 3.2.1  
- Spring WebFlux  
- Spring Batch 5  
- Spring Data JPA  
- Base de datos H2 (en memoria)  
- Resilience4j (Circuit Breaker)  
- Maven  
- Netty  

---

## 📂 Estructura del proyecto

- `batch` → Configuración y ejecución del proceso Spring Batch  
- `model` → Entidad `Student`  
- `repository` → Repositorio JPA  
- `service` → Lógica de negocio y cliente WebClient  
- `web` → Controladores REST y API Gateway  
- `resources` → Configuración y fichero `students.csv`  

---

## ⚙️ Proceso Batch

Al iniciar la aplicación se ejecuta automáticamente un **Job de Spring Batch** que:

1. Lee el fichero `students.csv`
2. Procesa cada registro
3. Inserta los estudiantes en la base de datos H2

Formato del CSV:

```csv
id,name,averageGrade
1,Ana,7.5
2,Luis,5.2
3,Marta,8.9
4,Carlos,6.4
5,Lucia,9.1
```

---

## 🧪 Base de datos

- H2 en memoria  
- Creación automática al arrancar  
- Datos cargados desde el proceso batch  

---

## 🔁 API Reactiva

### Obtener todos los estudiantes

```
GET http://localhost:8080/students
```

Devuelve todos los estudiantes almacenados.

### Obtener estudiantes con nota mínima

```
GET http://localhost:8080/students/top?min=7
```

---

## 🌐 API Gateway y Microservicios

La aplicación simula una arquitectura de microservicios:

- API Gateway:
```
GET http://localhost:8080/api/public/students
```

- Comunicación interna mediante `WebClient`
- Circuit Breaker implementado con Resilience4j

---

## 🧾 Trazabilidad

Cada petición genera un `traceId` que se registra en los logs, simulando trazabilidad distribuida.

---

## ▶️ Ejecución del proyecto

1. Importar el proyecto como **Maven Project**
2. Ejecutar la clase principal:
```
OscarPozuelo156799AnalyticsApplication
```
3. Acceder a los endpoints desde navegador o Postman

---

## ✅ Funcionalidades implementadas

- Proceso batch
- Lectura de CSV
- Persistencia de datos
- API reactiva
- API Gateway
- Circuit Breaker
- Trazabilidad
