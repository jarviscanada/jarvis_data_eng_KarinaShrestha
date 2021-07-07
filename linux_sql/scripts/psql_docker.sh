#!/bin/bash

#positional arguments
cmd=$1
db_username=$2
db_password=$3
#start docker if not already running
sudo systemctl status docker || sudo systemctl start docker
container_created=$(docker container ls -a -f name=jrvs-psql | wc -l)

case "$cmd" in
  "create" )
    if [ $container_created -eq 2 ]; then
      echo "The jrvs-psql docker container has already been created. View usage:"
      docker container ls -a -f name=jrvs-psql
      exit 0
    fi
    if [ -z "$db_username" ] || [ -z "$db_password" ]; then
      echo "No username and/or password passed as arguments. View the correct usage below:"
      echo "./scripts/psql_docker.sh create db_username db_password"
      exit 1
    fi
    #No errors, create and run the container
    docker volume create pgdata
    docker run --name jrvs-psql -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_USER=${db_username} -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
    #exit status of the last executed command
    exit $?
    ;;

  "start" )
    #start command given but container is not created; start the container
    if [ $container_created -ne 2 ]; then
      echo "Error: the jrvs-psql container does not exist. View correct usage:"
      echo "./scripts/psql_docker.sh create db_username db_password"
      exit 1
    fi
    docker container start jrvs-psql
    exit $?
  ;;

  "stop" )
    #stop command given but container is not created; stop the container
    if [ $container_created -ne 2 ]; then
      echo "Error: the jrvs-psql container does not exist. View correct usage:"
      echo "./scripts/psql_docker.sh create db_username db_password"
      exit 1
    fi
    docker container stop jrvs-psql
    exit $?
  ;;
  *)
    #default case (invalid command)
    echo "Error: invalid command. View correct usage: ./scripts/psql_docker.sh create db_username db_password"
    exit 1
  ;;