# Stage 1: Build the application
FROM openjdk:18-jdk-alpine AS builder

# Copy necessary files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
#COPY libs libs

# Set execution permissions
RUN chmod +x ./gradlew

# Clean the application
RUN ./gradlew clean

# Build the application
RUN ./gradlew bootJar

# Stage 2: Create a minimal image with the runnable JAR
FROM openjdk:18-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the previous stage
COPY --from=builder build/libs/*.jar underwriting-service-1.0.m1.jar

# Copy the local libs/*.jar file
#COPY libs/*.jar /app/libs/

# Expose the necessary port(s) for your Spring Boot application
EXPOSE 8080

# TODO - s:차후 확인
# 프로파일 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=dev

#RUN apk --update add postgresql-client
#RUN apk add --no-cache freetype fontconfig ttf-dejavu
# TODO - e:차후 확인

# set timezone
RUN apk add tzdata
ENV TZ=Asia/Seoul

# Set the entry point command to run the application
CMD ["java", "-jar", "underwriting-service-1.0.m1.jar"]