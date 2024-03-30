# Docker Compose Spring Boot + MySQL Steps

Follow these steps to set up a Spring Boot application with CRUD features integrated with MySQL using Docker Compose.

1. **Create a Spring Boot App**: Develop a Spring Boot application with CRUD functionality and test it thoroughly.

2. **Generate Jar File**: Generate the executable Jar file by executing `mvn clean install`.

3. **Create Dockerfile**: Set up a Dockerfile to containerize your Spring Boot application.

4. **Write docker-compose.yaml**: Define a docker-compose.yaml file to orchestrate the setup of all required services for your application, including the Spring Boot app and MySQL database.

5. **Remove Database Connection**: Remove the database connection details from your application.properties file, as the application will now connect to the MySQL database running in a container.

6. **Build Project**: Run `mvn clean install -DskipTests` to ensure your project is built without running tests.

7. **Start Docker Containers**: Execute `docker-compose up -d` to build and start your containers in detached mode.

8. **Validate Containers**: Re-run `docker-compose up -d` to ensure no duplicate containers are being created or started.

9. **Access Your Application**: Access your Spring Boot application through the provided endpoints.

10. **Connect to Database**:

    **Approach 1:** Connect to the MySQL database using connection details from docker-compose.yml:
    ```
    Host: localhost (or Docker host IP)
    Port: 3307 (or specified port)
    Username: root (or specified username)
    Password: password (or specified password)
    Database: persondb (or specified database name)
    ```
    ![Screenshot 2024-03-30 at 10 14 43 AM](https://github.com/Application4/dc-comp/assets/25712816/65895d83-bd1e-4c7a-9b5c-8dab2ffbed1b)

    ![Screenshot 2024-03-30 at 10 14 27 AM](https://github.com/Application4/dc-comp/assets/25712816/eb57872e-2d6d-4d9b-9317-b64aadbe8e86)



    **Approach 2:** Access MySQL database directly using Docker commands:
    ```
    $ docker ps

    CONTAINER ID   IMAGE                COMMAND                  CREATED          STATUS          PORTS                               NAMES
    ff4bd8bbf9b7   person-service:1.0   "java -jar person-se…"   54 minutes ago   Up 54 minutes   0.0.0.0:7070->7070/tcp              demo-app-application-1
    c8d267aac946   mysql:latest         "docker-entrypoint.s…"   54 minutes ago   Up 54 minutes   33060/tcp, 0.0.0.0:3307->3306/tcp   demo-app-mysql-db-1

    $ docker exec -it c8d267aac946 bash

    bash-4.4# mysql -u root -p

    Enter password: 

    Welcome to the MySQL monitor.  Commands end with ; or \g.

    Your MySQL connection id is 32

    Server version: 8.3.0 MySQL Community Server - GPL

    ...

    mysql> use persondb;

    Database changed

    mysql> select * from Person;

    +----+---------+
    | id | name    |
    +----+---------+
    |  1 | Basant  |
    |  2 | Santosh |
    |  3 | Rakesh  |
    |  4 | Praveen |
    |  5 | Krishna |
    |  6 | Saurabh |
    |  7 | Siva    |
    |  8 | Ranjith |
    |  9 | Ravi    |
    | 10 | Anushri |
    +----+---------+
    10 rows in set (0.00 sec)

    mysql> 
    ```
    Use the MySQL commands to interact with your database as needed.

    ![Screenshot 2024-03-30 at 10 15 35 AM](https://github.com/Application4/dc-comp/assets/25712816/a1af1042-1895-4426-9cdb-a8a68399d056)


    **Docker Compose with Kafka**


```yaml
version: '3.8'

services:
  mysql-db:
    image: 'mysql:latest'
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: persondb
    ports:
      - '3307:3306'

  zookeeper:
    image: wurstmeister/zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"

  kafka:
    image: wurstmeister/kafka
    container_name: kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_HOST_NAME: localhost
    volumes:
      - /var/run/docker.sock:/var/run/docker.sock

  application:
    build:
      context: .
      dockerfile: Dockerfile
    image: person-service:1.0
    depends_on:
      - mysql-db
      - kafka
    ports:
      - '7070:7070'
    environment:
      SPRING_DATASOURCE_URL: 'jdbc:mysql://mysql-db:3306/persondb'
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: password
```



