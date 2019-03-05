# feedme-kafka-mongodb
A Spring Boot-based microservice that consumes Kafka records concurrently and persists them against MongoDB. 

# Instructions
1. Clone the repo: `git clone https://github.com/dipeti/feedme-kafka-mongodb.git`

2. Build the app (java 11 required): `mvn clean install`

3. Create a docker image: `docker build -t feedme-db .`

If you wish to spin up the whole infrastructure you will need to complete the instructions in [feedme-tcp-kafka](https://github.com/dipeti/feedme-tcp-kafka) too.

# Endpoints
The service exposes a REST endpoint which dumps the data stored in MongoDB. 
`localhost:8081/events`
