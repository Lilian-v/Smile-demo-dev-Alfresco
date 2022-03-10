#!/bin/sh

export COMPOSE_FILE_PATH="${PWD}/target/classes/docker/docker-compose.yml"

if [ -z "${M2_HOME}" ]; then
  export MVN_EXEC="mvn"
else
  export MVN_EXEC="${M2_HOME}/bin/mvn"
fi

start() {
    docker volume create smile-demo-dev-alfresco-acs-volume
    docker volume create smile-demo-dev-alfresco-db-volume
    docker volume create smile-demo-dev-alfresco-ass-volume
    docker-compose -f "$COMPOSE_FILE_PATH" up --build -d
}

start_share() {
    docker-compose -f "$COMPOSE_FILE_PATH" up --build -d smile-demo-dev-alfresco-share
}

start_acs() {
    docker-compose -f "$COMPOSE_FILE_PATH" up --build -d smile-demo-dev-alfresco-acs
}

down() {
    if [ -f "$COMPOSE_FILE_PATH" ]; then
        docker-compose -f "$COMPOSE_FILE_PATH" down
    fi
}

purge() {
    docker volume rm -f smile-demo-dev-alfresco-acs-volume
    docker volume rm -f smile-demo-dev-alfresco-db-volume
    docker volume rm -f smile-demo-dev-alfresco-ass-volume
}

build() {
    $MVN_EXEC clean package
}

build_share() {
    docker-compose -f "$COMPOSE_FILE_PATH" kill smile-demo-dev-alfresco-share
    yes | docker-compose -f "$COMPOSE_FILE_PATH" rm -f smile-demo-dev-alfresco-share
    $MVN_EXEC clean package -pl smile-demo-dev-alfresco-share,smile-demo-dev-alfresco-share-docker
}

build_acs() {
    docker-compose -f "$COMPOSE_FILE_PATH" kill smile-demo-dev-alfresco-acs
    yes | docker-compose -f "$COMPOSE_FILE_PATH" rm -f smile-demo-dev-alfresco-acs
    $MVN_EXEC clean package -pl smile-demo-dev-alfresco-platform,smile-demo-dev-alfresco-platform-docker
}

tail() {
    docker-compose -f "$COMPOSE_FILE_PATH" logs -f
}

tail_all() {
    docker-compose -f "$COMPOSE_FILE_PATH" logs --tail="all"
}


case "$1" in
  build_start)
    down
    build
    start
    tail
    ;;
  start)
    start
    tail
    ;;
  stop)
    down
    ;;
  purge)
    down
    purge
    ;;
  tail)
    tail
    ;;
  reload_share)
    build_share
    start_share
    tail
    ;;
  reload_acs)
    build_acs
    start_acs
    tail
    ;;
  *)
    echo "Usage: $0 {build_start|build_start_it_supported|start|stop|purge|tail|reload_share|reload_acs}"
esac