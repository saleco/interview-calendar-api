[![Build Status](https://travis-ci.com/saleco/interview-calendar-api.svg?branch=master)](https://travis-ci.com/saleco/interview-calendar-api)
[![codecov](https://codecov.io/gh/saleco/interview-calendar-api/branch/master/graph/badge.svg?token=cvjdCr32aQ)](https://codecov.io/gh/saleco/interview-calendar-api)
![docker](https://img.shields.io/docker/v/saleco/interview-calendar-api)
# Interview Calendar API 
## Description 
API designed to match interviewers and candidates timetable

## Assumptions
An interview slot is a 1-hour period of time that spreads from the beginning of any 
hour until the beginning of the next hour. For example, a time span between 9am and
10am is a valid interview slot, whereas between 9:30am and 10:30am it is not.

## Use Cases

- ### As an INTERVIEWER, I would like to set availability slots
    - David is available next week each day from 9am through 4pm without breaks
    - Ingrid is available from 12pm to 6pm on Monday and Wednesday next week, and from 9am to 12pm on Tuesday and Thursday.

- ### As a CANDIDATE, I would like to set availability slots
    - Carl is available for the interview from 9am to 10am any weekday next week and from 10am to 12pm on Wednesday 
    
- ### As a USER, I would like to get a list of possible interview slots for a particular candidate and one or more interviewers
    - In this example, if the API queries for the candidate Carl and interviewers Ines and
      Ingrid, the response should be a collection of 1-hour slots: from 9am to 10am on
      Tuesday, from 9am to 10am on Thursday.
      
## Possible Use Cases

- As an INTERVIEWER, I would like to schedule an interview

- As an INTERVIEWER, I would like to set up myself as unavailable (vacations, sick day)

- As an INTERVIEWER, I would like to cancel an interview

- As an INTERVIEWER, I would like to modify an interview
  
- As a CANDIDATE, I would like to modify an interview

## Local Development Environment
### Pre-requisites
- jdk 11
- maven 3
### Running the application 
`mvn clean install`

`mvn springboot:run -Dspring-boot.run.profiles=local`

### Running Docker Container
### Requirements
- Docker

`./src/main/docker/local/docker-compose up -d`

### Services

| Service  |      Url                |  Description                             |
|----------|:-----------------------:|-----------------------------------------:|
| Kibana   |  [Kibana](http://localhost:5601/) | ELK Stack for Logging |
| Metrics  |  [Spring Boot Admin](http://localhost:1111/wallboard) | Spring Boot Admin UI |
| API      | [Swagger UI](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config) | Open API 3 Description |

## Possible Improvements
- Java doc
- Eureka for service discovery
- Cloud Config for properties
- Gateway to handle requests / security / loadbalancing
- Docker Stack  
- Docker swarm 
