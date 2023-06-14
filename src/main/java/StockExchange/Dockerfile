FROM openjdk:11
RUN apt-get update
RUN mkdir "Verteiltes_System"
WORKDIR "Verteiltes_System"
COPY src/main/java/StockExchange/src/* src/main/java/StockExchange/src/
COPY src/main/java/Stock/* src/main/java/Stock/
COPY src/* src/

RUN javac src/main/java/Stock/Stock.java src/main/java/StockExchange/src/StockExchange.java
CMD ["java", "-cp", "src/main/java/Stock:src/main/java", "StockExchange.src.StockExchange"]




