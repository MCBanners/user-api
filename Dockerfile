# Prepare environment
FROM alpine:3.20
RUN apk add openjdk16

# Download source code
RUN git clone https://github.com/MCBanners/user-api.git /app
WORKDIR /app

# Build the source into a binary
RUN ./gradlew clean build shadowJar

# Package the application
ENV USERAPI_MARIADB_NAME=""
ENV USERAPI_MARIADB_USER=""
ENV USERAPI_MARIADB_PASS=""
ENV USERAPI_JWT_SECRET=""
CMD /bin/sh -c "USERAPI_MARIADB_NAME=$USERAPI_MARIADB_NAME USERAPI_MARIADB_USER=$USERAPI_MARIADB_USER USERAPI_MARIADB_PASS=$USERAPI_MARIADB_PASS USERAPI_JWT_SECRET=$USERAPI_JWT_SECRET java -Xms128M -Xmx128M -jar build/libs/userapi.jar"
