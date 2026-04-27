FROM eclipse-temurin:17-jdk

WORKDIR /app

# Descargar las dos librerías necesarias
RUN curl -L -o java-websocket.jar https://repo1.maven.org/maven2/org/java-websocket/Java-WebSocket/1.5.7/Java-WebSocket-1.5.7.jar && \
    curl -L -o slf4j-api.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar

COPY . .

# Compilar incluyendo ambas librerías
RUN find . -name "*.java" > sources.txt && \
    javac -cp ".:java-websocket.jar:slf4j-api.jar" -d . @sources.txt

# Ejecutar incluyendo ambas librerías
CMD ["java", "-cp", ".:java-websocket.jar:slf4j-api.jar", "servidor.ServidorWeb"]
