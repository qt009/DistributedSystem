FROM openjdk:11
RUN apt-get update
RUN mkdir "Verteiltes_System"
WORKDIR "Verteiltes_System"
COPY src/main/java/Bank/src/* src/main/java/Bank/src/
COPY src/main/java/TCP/src/* src/main/java/TCP/src/
COPY src/main/java/Stock/* src/main/java/Stock/
COPY src/main/java/Thrift/* src/main/java/Thrift/
COPY src/* src/
COPY build/libs/* build/libs/

# Copy the Gradle wrapper files
COPY .gradle .gradle
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files
COPY build.gradle .

RUN ./gradlew build

#RUN javac src/main/java/Stock/Stock.java src/main/java/TCP/src/TCPInterface.java src/main/java/Bank/src/BankTCP.java src/main/java/Bank/src/ClientHandler.java src/main/java/Bank/src/Bank.java
#CMD ["java", "-cp", "build/libs:build/classes/java/main/Stock:build/classes/java/main/TCP/src:build/classes/java/main/Bank/src:build/classes/java/main/", "Bank.src.Bank"]

CMD ["java", "-jar", "build/libs/Bank.jar"]


