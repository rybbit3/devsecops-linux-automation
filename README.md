# sonar-jacoco-test

## 🎯 프로젝트 개요
---
**"개발자가 작성한 코드가 얼마나 '보안적으로 안전한지', '테스트로 검증되었는지'를 자동으로 분석하고 시각화"**하는 CI/CD 파이프라인을 구축했습니다.

개발자가 코드를 Push하거나 Pull Request(PR)를 생성할 때마다 GitHub Actions가 자동으로 코드의 **정적 분석(버그, 취약점)**과 **테스트 커버리지(JaCoCo)**를 검사하고, 그 결과를 SonarCloud 대시보드에 리포트하여 팀 전체가 코드 품질을 관리할 수 있도록 자동화한 프로젝트입니다.

## 기술스택
---
- **CI/CD:** GitHub Actions
    
- **정적 분석 (SAST):** SonarCloud
    
- **테스트 커버리지:** JaCoCo (Java Code Coverage)
    
- **빌드 도구:** Gradle
    
- **언어:** Java (JUnit)

## 아키텍처 및 핵심 플로우
---
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
  
## 배운점
---
- 배운점: SonarQube를 통해 테스트 커버리지와 SAST(정적분석)을 취합하여 확인하므로써 안전한 코드품질관리가 가능할수있다는것을 배웠습니다.
- 어려웠던점: gradle과 sonar간 버전 호환성 문제로 인해, gradle 빌드과정에서 에러 해결에 어려움을 겪었습니다. Sonar의 공식문서를 통해 해당 문제를 해결했고, AI에 의존하지않고, 공식문서의 중요성을 느낄수있었습니다.
- 향후 개선 계획: **Self-Hosted 적용:** 이 경험을 바탕으로, SonarCloud 대신 **AWS EC2/ECS에 SonarQube 서버를 직접 구축**하고 Jenkins 또는 GitHub Actions와 연동하는 파이프라인을 구축해 볼 예정입니다.
