# Introduction
This Twitter application is an implementation of a Java app that performs CRUD 
(create, read, update, and delete) operations, allowing the user to post|show|delete a Tweet, using the Twitter
REST API. It also utilizes the OAuthConsumer and HttpClient java libraries for user authentication and
the Jackson library to serialize java objects to JSON. This app implements the Spring framework to manage all the
dependencies, JUnit and Mockito for testing the app, Docker for deployment.

# Quick Start
This project was built and packaged by Maven and can be executed using docker. In order to run the docker
container, the 4 keys/tokens (consumerKey, consumerSecret, accessToken, tokenSecret) must be obtained and specified.

```
#package app using maven
// mvn clean package
mvn package -Dmaven.skip.test

# pull image from Docker Hub
docker pull localhostksh/twitter

# run the docker container
docker run --rm \
-e consumerKey=YOUR_VALUE \
-e consumerSecret=YOUR_VALUE \
-e accessToken=YOUR_VALUE \
-e tokenSecret=YOUR_VALUE \
localhostksh/twitter post post|show|delete [options]
```

# Design
## UML diagram
## explain each component(app/main, controller, service, DAO) (30-50 words each)

## Models
A simplified version of the Twitter Tweet model was created using 5 of the following objects:
- ``Tweet``: The Tweet object contains 'root-level' attributes, which include fundamental attributes such as
  id, created_at, entities, coordinates, and more.
- ``User Mention``: The User Mention object contains properties related to the user involved with a tweet.
- ``Hashtag``: The Hashtag object displays the hashtags included in a tweet.
- ``Entities``: The Entities object contains hashtags and user mentions.
- ``Coordinates``: The Coordinates object contains the longitude and latitude of the Tweet's location.

The JSON of the simplified Tweet model is shown below:
``` 
//Simplified Tweet Object 
{
   "created_at":"Mon Feb 18 21:24:39 +0000 2019",
   "id":1097607853932564480,
   "id_str":"1097607853932564480",
   "text":"test with loc223",
   "entities":{
      "hashtags":[],      
      "user_mentions":[]  
   },
   "coordinates":null,    
   "retweet_count":0,
   "favorite_count":0,
   "favorited":false,
   "retweeted":false
}
```  

## Spring
The Twitter app uses a number of dependencies and manually creates all the components to set up the
dependency relationship, which is a complicated process in cases where there are numerous components 
which each have their own set of dependencies. To solve this dependency-management issue, the Spring framework 
was implemented to handle all class dependencies through the ``TwitterCLIApp``. The beans (components/dependencies):
``TwitterDao``, ``TwitterService``, ``TwitterController``, and ``TwitterHttpHelper`` are specified within this file.
Each of the components are annotated in their respective classes using the annotations ``@Components``, ``@Service``,
``@Controller``, and ``@Repository``.

# Test
Using JUnit 4, integration tests and unit tests were performed for all of the components. Mockito was used to
perform unit testing.

## Deployment
A Dockerfile was created and used to build a docker image which was then pushed onto Dockerhub.
```
# create a dockerfile
cat > Dockerfile << EOF
FROM openjdk:8-alpine
COPY target/twitter*.jar /usr/local/app/twitter/lib/twitter.jar
ENTRYPOINT ["java","-jar","/usr/local/app/twitter/lib/twitter.jar"]
EOF

# create docker image
docker build -t localhostksh/twitter .

# verify the image
docker image ls | grep "twitter"

# push the image to DockerHub
docker push localhostksh/twitter
```

# Improvements
- Implement a simpler way to perform the ``show`` operation on a Tweet, by obtaining the date/time a tweet was posted
  rather than the id of a tweet.
- Allow the user to edit and update a posted tweet.
- Allow the user to filter out tweets based on a hashtag.