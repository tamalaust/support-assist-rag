# Simple Dockerfile that copies a prebuilt jar into a lightweight JRE image.
# Mirrors a real-world flow where the jar is already built by CI (e.g. Jenkins)
# and published to an artifact repository (e.g. JFrog Artifactory) before
# this image is built — no Maven build happens inside this Dockerfile.

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Build the jar locally first: mvn clean package -DskipTests
# Then this COPY pulls it in, same as CI would pull it from JFrog.
COPY staa/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
