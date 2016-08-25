# MobileUp_InterviewProject

Required:

  Gradle  	- to build the project
  
  MySQL   	- a database in localhost with the credentials specified in application.properties, located in src/main/resources
          	- a table inside the database, created with the query inside the file CreateTables.sql.
  
  CSV file 	- an initial CSV file containing the initial information. Right now, default CSV file is located inside src/main/resources 

Running:

  Inside project directory, run the following commands:
  
  ./gradlew build
  java -jar build/libs/mobileup-interviewproject-0.1.0.jar

  The first command builds the project, and the second command runs the web application and starts up a server.
  The default running port is 8080.

  There are two main pages that could be accessed:
  
  http://localhost:8080/index
  http://localhost:8080/details
  
  The first page will show all the listings currently inside the database.
