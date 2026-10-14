FROM eclipse-temurin:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# (주의) 소스 코드는 Dockerfile 내부에서 COPY하지 않고, docker run 실행 시 -v 옵션으로 볼륨 마운트합니다.
# Spring Boot DevTools를 활용하여 소스 코드 변경 시 자동 재시작되도록 bootRun을 실행합니다.
CMD ["sh", "./gradlew", "bootRun", "--no-daemon"]
