# [DevSecOps] SonarCloud(SAST) & JaCoCo 기반 CI/CD 파이프라인 구축

## 🎯 프로젝트 개요
안전하고 신뢰도 높은 소프트웨어 개발을 위해 **CI/CD 파이프라인 내 코드 품질 자동 검증 시스템을 구축**했습니다. 본 프로젝트는 개발자가 코드를 Push하거나 Pull Request(PR) 생성 시, GitHub Actions가 자동으로 **정적 분석(SAST)**과 **테스트 커버리지(JaCoCo)**를 실행하고, 그 결과를 SonarCloud 대시보드에 통합 리포트하여 지속적인 코드 품질 관리 및 보안 강화를 자동화한 경험입니다.

## 아키텍처 및 핵심 플로우
<img width="690" height="172" alt="image" src="https://github.com/user-attachments/assets/1fe415a3-6793-4bfe-8ad6-d52f49d327c1" />

1. **[GitHub 개발자]:** `main` 브랜치에 코드를 `Push`하거나 Pull Request를 생성합니다.

2. **[GitHub Actions] CI 트리거:** `.github/workflows/sonar-analysis.yml` 워크플로우가 자동으로 실행됩니다.
    
3. **[Gradle + JaCoCo] 빌드 및 테스트:**
    
    - `./gradlew build` 명령어가 실행되며, 이 과정에서 `test` 태스크가 동작합니다.
        
    - `test` 태스크가 실행될 때 **JaCoCo 플러그인**이 코드의 어느 부분이 테스트되었는지 측정하여 `jacocoTestReport.xml` 리포트 파일을 생성합니다.
        
4. **[Gradle + Sonar] 분석 및 리포트:**
    
    - `./gradlew sonar` 명령어가 실행됩니다.
        
    - Sonar 스캐너가 코드 자체의 **정적 분석**(버그, 코드 스멜)을 수행합니다.
        
    - 동시에 3단계에서 생성된 **`jacocoTestReport.xml` 파일을 읽어들여** 테스트 커버리지 데이터를 함께 수집합니다.
        
5. **[SonarCloud] 시각화:**
    
    - Sonar 스캐너가 모든 분석 데이터(정적 분석 + 커버리지)를 SonarCloud 서버로 전송합니다.
        
    - 개발자는 SonarCloud 대시보드에서 분석 결과와 테스트 커버리지 현황을 한눈에 확인합니다.


## 기술스택
- **CI/CD:** GitHub Actions
    
- **정적 분석 (SAST):** SonarCloud
    
- **테스트 커버리지:** JaCoCo (Java Code Coverage)
    
- **빌드 도구:** Gradle
    
- **언어:** Java (JUnit)


## 실습결과
1. JaCoCo가 생성한 코드 커버리지 리포트를 SonarCloud 대시보드에 성공적으로 연동하여, 각 메서드/라인별 커버리지 현황을 시각적으로 확인.
<img width="1004" height="551" alt="image" src="https://github.com/user-attachments/assets/6b883216-b4e7-4d7b-8faf-06e8691bcb87" />

2. SonarCloud의 정적 분석 기능을 통해 '하드코딩된 비밀번호'와 같은 잠재적 보안 취약점을 실제 코드에서 정확히 탐지하고 리포트함.
<img width="1011" height="493" alt="image" src="https://github.com/user-attachments/assets/15f72c5e-5812-4f2f-ad7d-461eeb5e85d5" />


## 배운점
- 배운점: 본 프로젝트를 통해 테스트 커버리지(JaCoCo)와 정적 분석(SonarCloud SAST) 데이터를 CI 파이프라인에서 통합 관리함으로써, 개발 초기 단계부터 체계적이고 자동화된 코드 품질 및 보안 관리의 중요성을 깊이 이해했습니다. 이는 안전하고 유지보수하기 쉬운 코드를 만드는 데 필수적인 역량임을 깨달았습니다.
- 어려웠던점: 가장 큰 어려움은 Gradle버전과 SonarQube Gradle Plugin버전 간의 호환성 문제였습니다. Plugin [id: 'org.sonarqube'] was not found와 같은 다양한 빌드 오류에 직면했으나, 여러 차례의 시행착오와 SonarQube/Gradle 공식 문서를 통해 문제를 성공적으로 해결했습니다. 이 과정에서 AI 도구에 전적으로 의존하기보다, 공식 문서와 디버깅을 통한 근본적인 문제 해결 능력의 중요성을 체감하며 성장할 수 있었습니다.
- 향후 개선 계획: 이번 SonarCloud 연동 경험을 기반으로, Self-Hosted SonarQube 서버 구축 및 CI/CD 파이프라인 통합을 다음 목표로 설정했습니다. 구체적으로는 AWS EC2/ECS 환경에 SonarQube 서버를 직접 배포하고, Jenkins 또는 GitHub Actions를 활용하여 완전한 온프레미스/클라우드 기반 DevSecOps 파이프라인을 구축함으로써, 시스템 운영 및 관리 역량을 더욱 확장해 나갈 계획입니다.
