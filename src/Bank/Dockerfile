FROM nginx:latest
RUN apt-get update && apt-get install -y openjdk-11-jdk
RUN mkdir "Verteiltes_System"
WORKDIR "Verteiltes_System"
COPY src/Bank/src/* src/Bank/src/
COPY src/* src/

RUN javac src/Stock.java src/Bank/src/Bank.java
CMD ["java", "-cp", "src:src/Bank/src", "Bank"]




