# 베이스 이미지로 OpenJDK 17 사용
FROM openjdk:17-jdk-slim

# 로컬의 JAR 파일을 컨테이너 안으로 복사, 복사한 파일의 이름은 user-api.jar
COPY build/libs/order-api.jar order-api.jar
# AWS 경로와 매핑하여 정확한 JAR 경로 지정
#COPY /var/lib/jenkins/workspace/cms-pipeline/order-api/build/libs/order-api.jar order-api.jar

ENTRYPOINT ["java", "-DSpring.profiles.active=prod","-jar","order-api.jar"]
