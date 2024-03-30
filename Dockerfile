FROM openjdk:17
WORKDIR /myApp
COPY ./target/person-service.jar /myApp
EXPOSE 7070
CMD ["java","-jar","person-service.jar"]