FROM openjdk:latest
LABEL authors="stdatrann"

#create a new app directory for application files
RUN mkdir /app

#copy app files from host machine to image filesystem
COPY out/production/Verteiltes_System/ /app

#set the directory for excecuting  future commands
WORKDIR /app

#run the Main class
CMD java Main
#ENTRYPOINT ["top", "-b"]