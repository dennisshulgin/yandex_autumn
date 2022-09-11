FROM openjdk:8-jdk-alpine

COPY target/yandex_autumn-0.0.1-SNAPSHOT.jar /yandex_autumn-0.0.1-SNAPSHOT.jar

ADD entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh

CMD ["sh", "/entrypoint.sh"]