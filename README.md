# Compilation and Run Instructions

## Requirements
  **JDK-23**, 
  **Node.js**
  
### JDK-23 Installation
  Navigate to: https://www.oracle.com/java/technologies/downloads/ and install the package for your operating system.
  
### NodeJS
  Navigate to: https://nodejs.org/en and install the latest LTS package.


## Starting the Servers
  ### Backend Server
    1. Navigate to the backend directory
    2. Execute the command : `./gradlew clean`
    3. Execute the command : `./gradlew build`
    4. Execute the command: `./gradlew bootRun`
    5. Open a browser and navigate to http://localhost:8080

  ### Frontend Server
    1. Navigate to the frontend directory
    2. Execute the command: `npm install` to install the necessary dependencies
    3. Execute the command: `npm start`
    4. Open a browser and navigate to http://localhost:3000

## Running Tests
If using Visual Studio Code -> Install extension "Test Runner for Java" authored by Microsoft. Once installed click on the new testing extension in the extensions bar. Execute the backend test suite.
To run front end tests, navigate to frontend directory and execute the command `npm run test`
