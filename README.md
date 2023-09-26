# littleParserCode
Application using Spring Boot, Angular and Tree-sitter

## Technical prerequisites
* JDK-17+
* maven 3.9.1+

## Run application
Two servers must be started to use this application.  
The first contains the service that calls the tree-sitter library.  
In a command prompt
* cd ${YOUR_PATH}/littleParserCode/code-parser-services
* mvn clean spring-boot:run

The second contains the angular application
In other command prompt
* cd ${YOUR_PATH}/littleParserCode/code-parser-ui
* mvn clean spring-boot:run

## URLs
The ui: http://localhost:8888/parser-ui
The post Rest service: http://localhost:8080/parser/api/upload


