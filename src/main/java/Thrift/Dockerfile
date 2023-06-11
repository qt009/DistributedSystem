# Use an official OpenJDK base image
FROM openjdk:11-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY build.gradle .

# Copy the source code
COPY src/main/java/Thrift/src/* src/main/java/Thrift/src
# Build the application using Gradle
RUN ./gradlew build
CMD ["ls", "build/classes/java/main/Thrift/src"]
# Set the command to run the application
CMD ["java","-cp", "build/classes/java/main", "Thrift.Thrift"]
