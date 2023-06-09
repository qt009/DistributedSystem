***************** Distributed System Project *****************

Author: Dang Quang Tran
        Manh Tan Doan
Group: group_h_9 Do1y

1. Docker configuration

to build an image use:

        docker build -f src/../.. -t distributed_system:1.x.x .

to run that image use

        docker run -f src/../.. distributed_system:1.x.x

to run that image in detached mode use

        docker run -d distributed_system:1.x.x

to view a container running in detached mode:

        docker logs ${CONTAINER ID}

to remove dangling images:

        docker rmi $(docker images -a | grep "^<none>" | awk '{print $3}')

2. Docker compose configuration

to run and build all images in docker-compose.yml use:

        docker-compose up --build

to run, build and run in detached mode all images and see logs of selected container using name use:
        
        docker-compose up --build -d
       
        docker-compose logs --follow ${service_name}
  
        Example: docker-compose logs --follow bank01 webclient browser

3. Docker HTML run configuration

open browser and type:

        http://localhost:8080/Browser.html

4. Install Thrift on local UNIX machine:

        sudo apt-get install automake bison flex g++ git libboost-all-dev libevent-dev libssl-dev libtool make pkg-config

        sudo apt install thrift-compiler