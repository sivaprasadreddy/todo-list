#!/bin/sh

declare project_dir=$(dirname $0)
declare docker_compose_file=${project_dir}/docker-compose.yml
declare docker_compose_platform_file=${project_dir}/etc/docker-compose-platform.yml
declare todo_api="todo-api-spring-boot"
declare sonarqube="sonarqube"

function restart() {
    stop
    start
}

function start() {
    echo 'Starting todo-api-spring-boot....'
    build_api
    docker-compose -f ${docker_compose_file} up --build --force-recreate -d ${todo_api}
    docker-compose -f ${docker_compose_file} logs -f
}

function start_all() {
    echo 'Starting todo_api and dependencies....'
    build_api
    docker-compose -f ${docker_compose_file} up --build --force-recreate -d
    docker-compose -f ${docker_compose_file} logs -f
}

function stop() {
    echo 'Stopping todo_api....'
    docker-compose -f ${docker_compose_file} stop
    docker-compose -f ${docker_compose_file} rm -f
}

function build_api() {
    ./mvnw clean verify
}

function sonar() {
    echo 'Starting sonarqube....'
    docker-compose -f ${docker_compose_platform_file} up --build --force-recreate -d ${sonarqube}
    docker-compose -f ${docker_compose_platform_file} logs -f
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
