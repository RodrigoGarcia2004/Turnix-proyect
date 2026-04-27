FROM eclipse-temurin:17-jdk

WORKDIR /app

# Descargar librería Java-WebSocket
RUN curl -L -o java-websocket.jar https://repo1.maven.org/maven2/org/java-websocket/Java-WebSocket/1.5.7/Java-WebSocket-1.5.7.jar

COPY . .

# Compilar con la librería
RUN find . -name "*.java" > sources.txt && \
    javac -cp ".:java-websocket.jar" -d . @sources.txt

# Ejecutar con la librería
CMD ["java", "-cp", ".:java-websocket.jar", "servidor.ServidorWeb"]
