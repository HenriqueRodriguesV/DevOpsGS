FROM eclipse-temurin:21-jre-alpine

WORKDIR /h2

RUN addgroup -S h2group && adduser -S h2user -G h2group

RUN apk add --no-cache curl

ENV H2_VERSION=2.2.224
ENV H2_TCP_PORT=9092
ENV H2_WEB_PORT=8082

RUN curl -L -o h2.jar https://repo1.maven.org/maven2/com/h2database/h2/${H2_VERSION}/h2-${H2_VERSION}.jar

RUN mkdir -p /data && chown -R h2user:h2group /data /h2

USER h2user

EXPOSE 9092
EXPOSE 8082

CMD ["sh", "-c", "java -cp h2.jar org.h2.tools.Server -tcp -tcpAllowOthers -tcpPort ${H2_TCP_PORT} -web -webAllowOthers -webPort ${H2_WEB_PORT} -ifNotExists -baseDir /data"]
