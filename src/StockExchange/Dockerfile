FROM nginx:latest
RUN apt-get update && apt-get install -y openjdk-11-jdk
RUN mkdir "Verteiltes_System"
WORKDIR "Verteiltes_System"
COPY src/StockExchange/src/* src/StockExchange/src/
COPY src/* src/

RUN javac src/Stock.java src/StockExchange/src/StockExchange.java
CMD ["java", "-cp", "src:src/StockExchange/src", "StockExchange"]




