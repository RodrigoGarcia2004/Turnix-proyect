FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Compilamos todos los archivos Java (incluyendo subcarpetas)
RUN find . -name "*.java" > sources.txt && \
    javac -d . @sources.txt

# Ejecutamos el servidor
CMD ["java", "servidor.ServidorWeb"]