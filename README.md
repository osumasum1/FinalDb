# Final Proyect

## [](https://github.com/osumasum1/FinalDB#diagram)Diagram

[![Diagram](https://github.com/osumasum1/FinalDB/blob/master/FinalDiagram.png)](https://github.com/osumasum1/FinalDB/blob/master/FinalDiagram.png)

## [](https://github.com/osumasum1/FinalDB#kudos-service)Kudos Service

Kudos service was developed with DropWizard and Java. It uses Cassandra. The following end-points were developed fot this service.

-   POST /kudos/createkudo
-   GET /kudos
-   GET /kudos/simpleKudos
-   GET /kudos/destino/{id}
-   DELETE /kudos/{id}
-   DELETE /kudos/user/{id}

### [](https://github.com/osumasum1/KudosProject#rabbitmq)RabbitMQ

-   Messaging - pattern reply-correlation pattern was used to send the kudos of the user to Users service. The server is located in Kudos service.
-   Point-to-point pattern used to delete all kudos of a user that was deleted. Receiver is located in Kudos.
-   Point-to-point pattern used to request the calculation of kudos of a user. Sender is located in Kudos.

### [](https://github.com/osumasum1/KudosProject#influxdb)InfluxDB

InfluxDB was used to store a log with the resources that were called by the user.

## [](https://github.com/osumasum1/KudosProject#users-service)Users Service

Users service was developed with DropWizard and Java. It uses MongoDB. The following end-points were developed fot this service.

-   GET /users/{id}
-   GET /users/
-   GET /users/simple
-   POST /users/createUser?nickname={nicknameValue}&userName={nameValue}&firstName={firstNameValue}&lastName={lastNameValue}
-  GET /users/allUsers?pagenumber=1&pagesize=3
-  GET /users/searchUsers?pagenumber=1&pagesize=3&nickname=liam&firstName=reina
-   DELETE /users/{id}

### [](https://github.com/osumasum1/KudosProject#rabbitmq-1)RabbitMQ

-   Messaging - pattern reply-correlation pattern was used to receive all kudos of a user from Kudos service. The client is located in users service.
-   Point-to-point pattern used to delete all kudos of a user that was deleted. Sender is located in Users.

### [](https://github.com/osumasum1/KudosProject#influxdb-1)InfluxDB

InfluxDB was used to store a log with the resources that were called by the user.

## [](https://github.com/osumasum1/KudosProject#stats-service)Stats Service

Stats service was developed with DropWizard and Java. It uses MYSQL DB and Mongo. These DBs are the same that users and kudos services are usinng. The following end-points were developed fot this service.

-   GET /stats/kudos/{id}
-   PUT /stats

### [](https://github.com/osumasum1/KudosProject#rabbitmq-2)RabbitMQ

-   Point-to-point pattern used to calculated the kudos of a user. Receiver is located in Stats.

### [](https://github.com/osumasum1/KudosProject#influxdb-2)InfluxDB

InfluxDB was used to store a log with the resources that were called by the user.
