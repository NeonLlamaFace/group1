FROM mysql/mysql-server:latest
COPY world.sql /docker-entrypoint-initdb.d
# USE THE CORRECT VARIABLE FOR INITIAL ROOT PASSWORD
ENV MYSQL_ROOT_PASSWORD=example
# Mac Fix - This is generally fine, but not the cause of the failure
ENV MYSQL_ROOT_HOST=%