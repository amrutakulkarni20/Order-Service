# Order tracking solution

This project contains REST API microservice which creates an order and also tracks the orders.

###  Pre-requisites

* JDK 17
* Gradle

### Build

To build the project please execute below command in the terminal from the root location of the project folder.
This command pulls the required dependencies from the central repository and thus builds the spring boot jar file.
```
./gradlew clean build
```

### Tests

To run the tests please execute below command in the terminal from the root location of the project folder.

```
./gradlew test
```

### Run

To run the spring boot project please execute the below command from the terminal from the root location of the project.
```
./gradlew bootRun
```
### Swagger Documentation

You can access the Swagger API documentation [here](http://localhost:8080/swagger-ui/index.html#/)

Please note :- In order to access the swagger api documentation at the mentioned link, the application should be in running state.

# Problem statement - Order tracking solution

Please implement a solution to record user shopping order from an online shop. 

Your application will receive order data from shops consisting of an order reference, payment type and online shop URL as well as some information about the user (first name, last name, address and email). There are three valid payment types: credit_card, paypal and cash_on_delivery.

Your application should validate each request. First, an order can only be processed if it contains a shop URL, order reference, payment type and user email. Otherwise, the request cannot be processed. Second, users are unique by email address, so the application may not create a new user database entry if the user already exists. Third, if the payment type is cash_on_delivery then the user's address is required and needs to be persisted. For other payment types, the address is not required. 

After the order is persisted, your application must return an order id. 

In addition, please provide an SQL DML statement to retrieve the total number of orders that has been made by a user grouped by shop and write it in the file ```number_of_orders_by_user_and_shop.sql```.

**Requirements:**
* Implement the solution using spring boot.
* Expose a REST API interface to the order tracking process defined.
* Expose a REST API interface to the retrieve orders for a given shop.
* Provide unit tests for all methods that are involved in the user creation process.

![alt text](task.png?raw=true "Task")


**Notes:**
* Don't hesitate to contact us in case you have any questions.
* Pay attention, the design of your solution will be discussed.
* You can modify any file in this coding challenge, this is just a skeleton.
* H2 Console can be reached under ```http://localhost:8080/h2-console``` username is ```sa``` and password should be blank. JDBC URL is ```jdbc:h2:mem:app_db;DB_CLOSE_ON_EXIT=FALSE;DATABASE_TO_UPPER=false```
* It is estimated that this task won't take more than 8 hours of work (4-6 hours for Senior).
