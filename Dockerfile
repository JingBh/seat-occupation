FROM eclipse-temurin:21-jre

LABEL org.opencontainers.image.source=https://github.com/JingBh/seat-occupation

WORKDIR application

COPY build/libs/dependencies/ ./
COPY build/libs/spring-boot-loader/ ./
COPY build/libs/snapshot-dependencies/ ./
COPY build/libs/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
