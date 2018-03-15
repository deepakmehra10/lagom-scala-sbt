# Lagom Scala Example

Building Reactive Scala application with Lagom framework. This is a classic CRUD application which persist events in Cassandra Database. Here we are using embedded Cassandra to persist events and embedded kafka for publishing and subscribing between microservices.

##### Prerequisites

* Java Development Kit (JDK), version 8 or higher.

* sbt 0.13.5 or higher

##### Getting the Project
https://github.com/deepakmehra10/lagom-scala-sbt.git

##### Command to start the project

**sbt runAll**

Json Formats for different Rest services are mentioned below :

**1. Create Product:**

> Route(Method - POST) : localhost:9000/api/product/addProduct

Rawdata(json): { "id": "1", "title": "Trimmer", "price": "100", "description": "Panasonic Trimmer. It is incredible and trims your hair safely" }

**2. Update Product:**

>Route(Method - PUT) : localhost:9000/api/product/updateProduct/:id

**3. Delete Product:**

> Route(Method - DELETE) : localhost:9000/api/product/deleteProduct/:id

**4. Get Product details with id:**

> Route(Method - GET) : localhost:9000/api/product/getProduct/:id

**5. Get All Product details:**

>Route(Method - GET) : localhost:9000/api/product/getAllProducts


