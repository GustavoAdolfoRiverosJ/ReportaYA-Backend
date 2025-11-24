# Etapa 1: Build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (para aprovechar el caché de Docker)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación (skip tests para build más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Variables de entorno - Configuración de Base de Datos
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://ep-soft-poetry-a8g4qwp1-pooler.eastus2.azure.neon.tech/BackendReporte?sslmode=require
ENV SPRING_DATASOURCE_USERNAME=neondb_owner
ENV SPRING_DATASOURCE_PASSWORD=npg_MnrKfq09AQSa
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver

# Variables de entorno - Configuración JPA/Hibernate
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=update
ENV SPRING_JPA_SHOW_SQL=true
ENV SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL=true
ENV SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# Variables de entorno - Configuración de la aplicación
ENV SPRING_APPLICATION_NAME=incidencia-urbana

# Variables de entorno - Configuración de Java
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
