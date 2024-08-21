FROM --platform=s390x eclipse-temurin:21
COPY . .
ENTRYPOINT [ "./gradlew", "assemble" , "--stacktrace"]
