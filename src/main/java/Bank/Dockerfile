FROM nginx:latest
RUN apt-get update && apt-get install -y openjdk-11-jdk
RUN mkdir "Verteiltes_System"
WORKDIR "Verteiltes_System"
COPY src/main/java/Bank/src/* src/main/java/Bank/src/
COPY src/main/java/TCP/src/* src/main/java/TCP/src/
COPY src/main/java/Stock/* src/main/java/Stock/
COPY src/* src/

RUN javac src/main/java/Stock/Stock.java src/main/java/TCP/src/TCPInterface.java src/main/java/Bank/src/BankTCP.java src/main/java/Bank/src/Bank.java
CMD ["java", "-cp", "src/main/java/Stock:src/main/java/TCP/src:src/main/java/", "Bank.src.Bank"]




