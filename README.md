# Database
## Docker Setup
Get Docker image with Postgres inside

`docker pull postgres:alpine`

Create a container from the image

`docker run --name intouch -e POSTGRES_PASSWORD=jazzification -d -p 5432:5432 postgres:alpine`
- '--name NAME' specifies a string name for the container
- '-e POSTGRES_PASSWORD=PASSWORD' sets an environment variable
- '-d' runs the container in detached mode i.e in the background
- '-p HOST_PORT:CONTAINER_PORT' connect the port 5432 inside the container to the port 5432 on the local machine so we can connect to the DB from outside the container

Start bash in the container 

`docker exec -it intouch bash`
- `ls` or `pwd` reveals the container's filesystem (i.e not the same fileystem as our local machine's)

Run Postgres CLI

`psql -U postgres`

Cheatsheet

- `docker ps` - shows running containers
- `docker image ls` - shows all images
- `docker image rm IMAGE` - removes image; use `-f` to force if necessary

## Connecting from Outside Container
`sudo psql -h localhost -p 5432 -U postgres`
- Note that the container **needs** to be running, but **not** Postgres Application on the local machine itself (since the container takes care of starting Postgres)
- if prompted for `password: `, enter local machine password 
- if prompted for `password for user postgres: `, enter DB password

## Postgres CLI
Cheatsheet

- `\l` - lists all databases
- `\c DATABASE` - connects to specified database
- `create database NAME;` - creates a database with the specified name 
- `\d` - lists all tables (only works after connected to a database)