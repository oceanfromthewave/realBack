FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew installDist -PmainClass=practice.phase19.AppServer --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/install/realBack /app
ENV APP_PORT=8080
EXPOSE 8080
ENTRYPOINT ["/app/bin/realBack"]
