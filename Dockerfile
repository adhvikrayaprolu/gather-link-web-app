FROM eclipse-temurin:17-jdk-jammy@sha256:73ed442231ec66cfe48cae0b263ce619a75ab96d162ab325f38fc0a269ee3aab AS build
WORKDIR /build
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN --mount=type=cache,target=/root/.m2 ./mvnw --batch-mode -Dstyle.color=never dependency:go-offline
COPY src/ src/
# Build the actual JSP-capable executable WAR only after its regression tests pass.
RUN --mount=type=cache,target=/root/.m2 ./mvnw --batch-mode -Dstyle.color=never verify
COPY scripts/container/HttpProbe.java /probe/HttpProbe.java
RUN javac -d /probe /probe/HttpProbe.java

FROM eclipse-temurin:17-jre-jammy@sha256:b3054a20e1a851574cdb1692dd0db5310c647e62214a2a7abecbec1b9e3ae8f1 AS application
RUN groupadd --gid 10001 gatherlink \
    && useradd --uid 10001 --gid 10001 --home-dir /app --no-create-home gatherlink \
    && mkdir -p /app /data \
    && chown gatherlink:gatherlink /data
WORKDIR /app
COPY --from=build /build/target/gather-link-0.0.1-SNAPSHOT.war /app/application.war
COPY --from=build /probe/HttpProbe.class /opt/probe/HttpProbe.class
ENV SPRING_PROFILES_ACTIVE=local \
    SPRING_DATASOURCE_URL="jdbc:h2:file:/data/gatherlink;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=FALSE"
USER 10001:10001
EXPOSE 8080
HEALTHCHECK --interval=5s --timeout=5s --start-period=20s --retries=20 \
    CMD ["java", "-cp", "/opt/probe", "HttpProbe", "http://127.0.0.1:8080/health/ready"]
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/application.war"]
