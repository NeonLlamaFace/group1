FROM mysql/mysql-server:latest
COPY world.sql /docker-entrypoint-initdb.d

ENV MYSQL_ROOT_PASSWORD example
# Allows root access from any host, required for Docker container networking
ENV MYSQL_ROOT_HOST=%

# CRITICAL FIX: Ensure permissions are explicitly set for the root user
# to connect from any host ('%') after initialization, which is necessary for the Java app.
RUN echo "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'example' WITH GRANT OPTION;" >> /docker-entrypoint-initdb.d/init_permissions.sql
RUN echo "FLUSH PRIVILEGES;" >> /docker-entrypoint-initdb.d/init_permissions.sql

# Healthcheck to ensure MySQL is fully up and running before the app connects
HEALTHCHECK --interval=5s --timeout=3s \
  CMD mysqladmin ping -h localhost --silent || exit 1