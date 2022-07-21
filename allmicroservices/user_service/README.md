Medicine Adherence Application 
===

We would like to create an APP for users which will serve as a medicine adherence module.
Many of the diseases require the patient to take medicines at a regular interval. Some of the diseases like BP, Diabetes, Tuberculosis have really strict norms about taking medicines. In case the medicine is not taken in time then the patient’s health is impacted negatively. 

## Getting Started

### Prerequisites

* Java 11
* Maven 3.0+: https://maven.apache.org/install.html
* An IDE (Intellij Idea Community Edition) : https://www.jetbrains.com/idea/download/#section=linux
* Gradle : https://docs.gradle.org/current/userguide/installation.html
* MySQL : https://dev.mysql.com/downloads/installer/

### Installing
To create your own Spring Boot-based project use
* Spring Initializr : https://start.spring.io/

  Initializr generates spring boot project with just what you need to start quickly!


Building the project
====================
To do the full build, do: `mvn clean install`

Building a single module
====================
To build a specific module run the command: `mvn clean install` in the module directory


Running a Spring Boot module
====================
To run a Spring Boot module run the command: `mvn spring-boot:run` in the module directory

# Set up the RabbitMQ Broker

Before you can build your email service application, you need to set up a server to handle receiving and sending messages.

RabbitMQ is an AMQP server. The server is freely available at https://www.rabbitmq.com/download.html. You can download it manually or, if you use a Mac with Homebrew, by running the following command in a terminal window:

``brew install rabbitmq``

Unpack the server and launch it with default settings by running the following command in a terminal window:

`rabbitmq-server`
# Project-Structure
```
    │   ├── com
    │   │   └── example
    │   │       └── user_service
    │   │           ├── config
    │   │           │   ├── filter
    │   │           │   │   └── UserDetailService.class
    │   │           │   ├── ImageConfig.class
    │   │           │   ├── PdfMailSender.class
    │   │           │   ├── RabbitmqConfiguration.class
    │   │           │   ├── RedisConfig.class
    │   │           │   ├── SwaggerConfig.class
    │   │           │   └── ThreadConfig.class
    │   │           ├── controller
    │   │           │   ├── CaretakerController.class
    │   │           │   ├── MedicineController.class
    │   │           │   ├── UserController.class
    │   │           │   └── UserDetailController.class
    │   │           ├── exception
    │   │           │   ├── DataAccessExceptionMessage.class
    │   │           │   ├── UserCaretakerException.class
    │   │           │   ├── UserExceptionMessage.class
    │   │           │   ├── UserExceptions.class
    │   │           │   └── UserMedicineException.class
    │   │           ├── model
    │   │           │   ├── Image.class
    │   │           │   ├── Medicine.class
    │   │           │   ├── MedicineHistory.class
    │   │           │   ├── UserCaretaker.class
    │   │           │   ├── User.class
    │   │           │   ├── UserDetails.class
    │   │           │   └── UserMedicines.class
    │   │           ├── pojos
    │   │           │   ├── authentication
    │   │           │   │   └── AuthenticationResponse.class
    │   │           │   ├── dto
    │   │           │   │   ├── request
    │   │           │   │   │   ├── DetailsDTO.class
    │   │           │   │   │   ├── LoginDTO.class
    │   │           │   │   │   ├── MedicineHistoryDTO.class
    │   │           │   │   │   ├── MedicinePojo.class
    │   │           │   │   │   ├── SendImageDto.class
    │   │           │   │   │   ├── UserCaretakerDTO.class
    │   │           │   │   │   ├── UserDetailsDTO.class
    │   │           │   │   │   └── UserEntityDTO.class
    │   │           │   │   └── response
    │   │           │   │       ├── caretaker
    │   │           │   │       │   ├── CaretakerDelete.class
    │   │           │   │       │   ├── CaretakerResponse1.class
    │   │           │   │       │   ├── CaretakerResponse.class
    │   │           │   │       │   └── CaretakerResponsePage.class
    │   │           │   │       ├── image
    │   │           │   │       │   ├── ImageListResponse.class
    │   │           │   │       │   ├── ImageResponse.class
    │   │           │   │       │   └── SendImageResponse.class
    │   │           │   │       ├── medicine
    │   │           │   │       │   ├── MedicineResponse.class
    │   │           │   │       │   ├── MedicineResponsePage.class
    │   │           │   │       │   └── SyncResponse.class
    │   │           │   │       ├── RefreshTokenResponse.class
    │   │           │   │       ├── sql
    │   │           │   │       │   └── SqlErrorResponse.class
    │   │           │   │       └── user
    │   │           │   │           ├── UserDetailResponse.class
    │   │           │   │           ├── UserProfileResponse.class
    │   │           │   │           ├── UserResponse.class
    │   │           │   │           └── UserResponsePage.class
    │   │           │   ├── MailInfo.class
    │   │           │   └── Notificationmessage.class
    │   │           ├── repository
    │   │           │   ├── ImageRepository.class
    │   │           │   ├── Medrepo.class
    │   │           │   ├── UserCaretakerRepository.class
    │   │           │   ├── UserDetailsRepository.class
    │   │           │   ├── UserMedHistoryRepository.class
    │   │           │   ├── UserMedicineRepository.class
    │   │           │   └── UserRepository.class
    │   │           ├── security
    │   │           │   ├── AuthenticationHandler.class
    │   │           │   ├── SecurityLayer.class
    │   │           │   ├── TokenRefreshRequest.class
    │   │           │   ├── WebConfiguration$1.class
    │   │           │   └── WebConfiguration.class
    │   │           ├── service
    │   │           │   ├── CareTakerService.class
    │   │           │   ├── CareTakerServiceImpl.class
    │   │           │   ├── MailService.class
    │   │           │   ├── UserDetailService.class
    │   │           │   ├── UserDetailServiceImpl.class
    │   │           │   ├── UserMedicineService.class
    │   │           │   ├── UserMedicineServiceImpl.class
    │   │           │   ├── UserService.class
    │   │           │   └── UserServiceImpl.class
    │   │           ├── UserServiceApplication$1.class
    │   │           ├── UserServiceApplication.class
    │   │           ├── util
    │   │           │   ├── Constants.class
    │   │           │   ├── DateHelper.class
    │   │           │   └── JwtUtil.class
    │   │           └── validators
    │   │               ├── ImageValidator.class
    │   │               └── ImageValidCheck.class

```
# Security

Integration with Spring Security and add other filter for jwt token process.

The secret key is stored in `application.properties`.

# Database

It uses MySql database, can be changed easily in the `application.properties` for any other database.

![img.png](img.png)

# System Design

![img_2.png](img_2.png)

# Swimlane Diagram

Flowchart that show both process from start to finish and who is responsible for each step in the process.
![img_3.png](img_3.png)

Working with the IDE
====================
This repo contains a large number of modules.
When you're working with an individual module, there's no need to import all of them (or build all of them) - you can simply import that particular module in either Eclipse or IntelliJ. 

## Deployment

Add additional notes about how to deploy this on a live system
### Try it out with [Docker](https://www.docker.com/)

You'll need Docker and Ngrok installed and create docker image for your project
        
     ngrok http 8080
     sudo docker run --network "host" -p 800:8080 user_service:0.0.1-SNAPSHOT

# Try it out with a RealWorld frontend

The entry point address of the backend API is at http://localhost:8080

## Help

Any advise for common problems or issues.

* Clear the system cache﻿
* From the main menu, select File | Invalidate Caches.
* In the Invalidate Caches dialog, you can select additional actions that the IDE will perform while removing the cache files
* Click Invalidate and Restart.

## Authors

Contributors names and contact info

* Yatin Tripathi : https://github.com/yatintri
* Nikunj Bisht : https://github.com/Nikunj-bisht
* Vinay Kumar Soni : https://github.com/Vinaysoni1206

## Acknowledgments

* https://www.baeldung.com/
* https://spring.io/guides/gs/caching/
* https://www.rabbitmq.com/
