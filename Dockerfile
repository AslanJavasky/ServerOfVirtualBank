# Используйте основной образ Java от AdoptOpenJDK
FROM adoptopenjdk/openjdk11:alpine-slim

# Установка директории приложения в контейнере
WORKDIR /app

# Копирование JAR-файла в контейнер
COPY target/ServerOfVirtualBank.jar /app

# Команда для запуска приложения
CMD ["java", "-jar", "ServerOfVirtualBank.jar"]
