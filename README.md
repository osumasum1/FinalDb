# Final Project

## [](https://github.com/osumasum1/FinalDB#diagram)Diagram

[![Diagram](https://github.com/osumasum1/FinalDB/blob/master/FinalDiagram.png)](https://github.com/osumasum1/FinalDB/blob/master/FinalDiagram.png)

## [](https://github.com/osumasum1/FinalDB#kudos-service)Kudos Service

Kudos service was developed with DropWizard and Java. It uses Cassandra. The following end-points were developed for this service.

- POST /kudos/createkudo
- DELETE /kudos/deleteKudo/{id}
- GET /kudos/allKudos 	with "page" and "size" query parameters.


### [](https://github.com/osumasum1/KudosProject#influxdb)InfluxDB

InfluxDB was used to store a log with the resources that were called by the user.

## [](https://github.com/osumasum1/KudosProject#users-service)Users Service

Users service was developed with DropWizard and Java. It uses MongoDB. The following end-points were developed for this service.

-   GET /users/{id}
-   GET /users/
-   GET /users/simple
-   POST /users/createUser?nickname={nicknameValue}&userName={nameValue}&firstName={firstNameValue}&lastName={lastNameValue}
-  GET /users/allUsers?pagenumber=1&pagesize=3
-  GET /users/searchUsers?pagenumber=1&pagesize=3&nickname=liam&firstName=reina
-   DELETE /users/{id}


### [](https://github.com/osumasum1/KudosProject#influxdb-1)InfluxDB

InfluxDB was used to store a log with the resources that were called by the user.


### [](https://github.com/osumasum1/KudosProject#influxdb-2)InfluxDB

InfluxDB was used to store a log with the resources that were called by the user.
