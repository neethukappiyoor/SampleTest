# SampleTest - Pet Store Application

Pet store is Cucumber BDD Page Object Model framework based REST API Automation project. Framework is built with an intention to test RestAPIs with RestAssured library and Cucumber BDD. Cucumber is an open-source testing framework that supports Behavior Driven Development for automation testing of web applications. The tests are first written in a simple scenario form that describes the expected behavior of the system from the user’s perspective.REST Assured is a Java library that provides a domain-specific language (DSL) for writing powerful, maintainable tests for RESTful APIs
# Prerequisite
JAVA 8 or higher
As Integrated Development Environment - Used IntelliJ
A Build Tool - Used Maven

# How to run the project 
Follow commands to run tests from eclipse or intelliJ
git clone https://github.com/neethukappiyoor/SampleTest.git
Import project in IDE as a maven project
Right click on pom.xml -> Run As -> Maven install or Navigate to \src\test\java\runner and Run As TestRunner.java as JUnit

# Project Structure
src/test/java - includes 
1. Feature file for Pet APIs
2. Page Object Model for Request/Response Boday
3. Runner Class
4. Step Definition and Parent Class
5. Helper Class to build requests

src/test/resources - includes
1. Test Data
