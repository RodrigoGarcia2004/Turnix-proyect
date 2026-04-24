FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Compilamos todos los archivos Java
RUN find . -name "*.java" > sources.txt && \
    javac -d . @sources.txt

# Ejecutamos el servidor
CMD ["java", "servidor.ServidorWeb"]
