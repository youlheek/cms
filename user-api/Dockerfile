# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# 로컬의 JAR 파일을 컨테이너 안으로 복사, 복사한 파일의 이름은 user-api.jar
COPY build/libs/user-api.jar user-api.jar

ENTRYPOINT ["java", "-DSpring.profiles.active=prod","-Dmailgun.key=${MAILGUN_KEY}","-jar","user-api.jar"]
