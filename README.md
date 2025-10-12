# 🏙️ ReportaYA - Backend API REST

Sistema de gestión de reportes ciudadanos de incidencias urbanas con arquitectura basada en principios SOLID.

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.5.6**
- **Spring Data JPA**
- **PostgreSQL** (Azure Database)
- **Maven**

## ✨ Características

✅ **Creación automática de tablas** con Hibernate  
✅ **API REST completa** para gestión de reportes  
✅ **Arquitectura en capas** siguiendo principios SOLID  
✅ **Relaciones JPA** (OneToMany, ManyToOne, OneToOne)  
✅ **Herencia de entidades** con estrategia JOINED  
✅ **DTOs** para separación de capas  
✅ **Manejo de transacciones**  
✅ **CORS habilitado**

## 📁 Estructura del Proyecto

```
src/main/java/com/ulima/incidenciaurbana/
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── repository/     # Acceso a datos
├── service/        # Lógica de negocio (interfaces)
├── service/impl/   # Implementaciones de servicios
└── controller/     # API REST endpoints
```

## 🎯 Principios SOLID Aplicados

- **S** - Single Responsibility: Cada clase tiene una única responsabilidad
- **O** - Open/Closed: Abierto para extensión, cerrado para modificación
- **L** - Liskov Substitution: Herencia correcta de Persona
- **I** - Interface Segregation: Interfaces específicas por dominio
- **D** - Dependency Inversion: Dependencia de abstracciones

Ver detalles completos en [ARQUITECTURA_SOLID.md](ARQUITECTURA_SOLID.md)

## ⚙️ Configuración

### Base de Datos PostgreSQL (Azure)

La configuración ya está lista en `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://prograweb-202402-1507-db.postgres.database.azure.com:5432/BackendReporteYA
spring.datasource.username=postgres
spring.datasource.password=rendimiento456R
spring.jpa.hibernate.ddl-auto=update
```

### ✅ Las tablas se crean automáticamente al iniciar la aplicación

## 🏃 Ejecutar la Aplicación

### Windows (CMD):
```cmd
mvnw.cmd spring-boot:run
```

### Linux/Mac:
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 🌐 API Endpoints

### Ciudadanos
- `POST /api/ciudadanos` - Crear ciudadano
- `GET /api/ciudadanos` - Listar todos
- `GET /api/ciudadanos/{id}` - Obtener por ID
- `GET /api/ciudadanos/dni/{dni}` - Buscar por DNI
- `PUT /api/ciudadanos/{id}` - Actualizar
- `DELETE /api/ciudadanos/{id}` - Eliminar

### Reportes
- `POST /api/reportes` - Crear reporte
- `GET /api/reportes` - Listar todos
- `GET /api/reportes/{id}` - Obtener por ID
- `GET /api/reportes/ciudadano/{id}` - Reportes de un ciudadano
- `GET /api/reportes/estado/{estado}` - Reportes por estado
- `PATCH /api/reportes/{id}/estado` - Cambiar estado
- `PATCH /api/reportes/{id}/prioridad` - Cambiar prioridad

### Asignaciones
- `POST /api/asignaciones` - Crear asignación
- `GET /api/asignaciones` - Listar todas
- `GET /api/asignaciones/tecnico/{id}` - Por técnico
- `GET /api/asignaciones/operador/{id}` - Por operador
- `PATCH /api/asignaciones/{id}/aceptar` - Aceptar
- `PATCH /api/asignaciones/{id}/completar` - Completar

Ver ejemplos completos en [EJEMPLOS_API.md](EJEMPLOS_API.md)

## 🧪 Probar la API

### Ejemplo con cURL:
```cmd
curl -X POST http://localhost:8080/api/ciudadanos ^
  -H "Content-Type: application/json" ^
  -d "{\"nombres\":\"Juan\",\"apellidos\":\"Pérez\",\"dni\":\"12345678\",\"telefono\":\"987654321\",\"correo\":\"juan@email.com\"}"
```

### Herramientas recomendadas:
- Postman
- Insomnia
- Thunder Client (VS Code)

## 📊 Modelo de Datos

```
Persona (abstracta)
├── Ciudadano → Reportes
├── OperadorMunicipal → Asignaciones
├── Tecnico → Asignaciones
└── Administrador

Reporte → Imagenes + Asignaciones
Asignacion → Reporte + Tecnico + OperadorMunicipal
Cuenta ←→ Persona (1:1)
```

## 🔧 Próximos Pasos

- [ ] Implementar Spring Security + JWT
- [ ] Agregar validaciones con Bean Validation
- [ ] Documentación con Swagger/OpenAPI
- [ ] Tests unitarios e integración
- [ ] Almacenamiento de imágenes en Azure Blob
- [ ] Paginación de resultados

## 📖 Documentación

- [Arquitectura SOLID](ARQUITECTURA_SOLID.md)
- [Ejemplos de API](EJEMPLOS_API.md)

## 👥 Equipo

Proyecto desarrollado para el curso de Software 2 - Universidad de Lima
Proyecto de software II en la universidad de Lima. Se basa en la implementación del Backend para que los ciudadanos reporten incidencias urbanas. 
