#!/bin/bash

# Swagger 문서를 정적 파일로 생성하는 스크립트
# Script to generate static Swagger documentation

echo "Starting application to generate API documentation..."

# 1. 애플리케이션 실행 (백그라운드)
# Start application in background
mvn spring-boot:run &
APP_PID=$!

# 2. 애플리케이션 시작 대기 (30초)
# Wait for application to start
echo "Waiting for application to start..."
sleep 30

# 3. OpenAPI JSON 다운로드
# Download OpenAPI JSON
echo "Downloading OpenAPI JSON..."
curl http://localhost:8080/api/v1/v3/api-docs -o src/main/resources/static/openapi.json

# 4. OpenAPI YAML 다운로드
# Download OpenAPI YAML
echo "Downloading OpenAPI YAML..."
curl http://localhost:8080/api/v1/v3/api-docs.yaml -o src/main/resources/static/openapi.yaml

# 5. 애플리케이션 종료
# Stop application
echo "Stopping application..."
kill $APP_PID

echo "API documentation generated successfully!"
echo "Files saved in src/main/resources/static/"